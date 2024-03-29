package io.github.leothawne.LTItemMail.listener;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockExplodeEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.inventory.MailboxInventory;
import io.github.leothawne.LTItemMail.item.MailboxItem;
import io.github.leothawne.LTItemMail.item.model.Item;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.MailboxModule;
import io.github.leothawne.LTItemMail.module.PermissionModule;
import io.github.leothawne.LTItemMail.module.integration.GriefPreventionAPI;
import io.github.leothawne.LTItemMail.module.integration.IntegrationModule;
import io.github.leothawne.LTItemMail.module.integration.RedProtectAPI;
import io.github.leothawne.LTItemMail.module.integration.TownyAdvancedAPI;
import io.github.leothawne.LTItemMail.module.integration.WorldGuardAPI;
import net.md_5.bungee.api.ChatColor;

public final class MailboxBlockListener implements Listener {
	final Item mailbox = new MailboxItem();
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onClick(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if(event.hasBlock() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getHand().equals(EquipmentSlot.HAND)) {
			final Block block = event.getClickedBlock();
			if(block.getType().equals(mailbox.getMaterial())) if(DatabaseModule.Block.isMailboxBlock(block.getLocation())) {
				event.setCancelled(true);
				if(player.isSneaking()) return;
				if(PermissionModule.hasPermission(player, PermissionModule.Type.BLOCK_PLAYER_USE)) {
					final OfflinePlayer owner = Bukkit.getOfflinePlayer(DatabaseModule.Block.getMailboxOwner(block.getLocation()));
					if(owner.getUniqueId().equals(player.getUniqueId()) && !PermissionModule.hasPermission(player, PermissionModule.Type.CMD_ADMIN_BYPASS)) {
						player.performCommand("itemmail list");
					} else player.openInventory(MailboxInventory.getMailboxInventory(MailboxInventory.Type.OUT, null, owner, null, ""));
				} else player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.BLOCK_USEERROR));
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlace(final BlockPlaceEvent event) {
		final ItemStack item = event.getItemInHand();
		if(item != null && item.getItemMeta() != null) if(item.getType().equals(mailbox.getMaterial())) if(item.getItemMeta().getLore() != null && item.getItemMeta().getLore().size() == 1) if(item.getItemMeta().getLore().get(0).contains("Mailbox@") && item.getItemMeta().getLore().get(0).split("@").length == 2) {
			final Player player = event.getPlayer();
			final Block block = event.getBlockPlaced();
			if(canBuildBreak(player, block.getLocation()) && canBuild(player, block.getLocation())) {
				if(PermissionModule.hasPermission(player, PermissionModule.Type.BLOCK_PLAYER_PLACE)) {
					final Block blockBelow = new Location(block.getLocation().getWorld(), block.getLocation().getBlockX(), (block.getLocation().getBlockY() - 1), block.getLocation().getBlockZ()).getBlock();
					if(blockBelow.getType().toString().toLowerCase().endsWith("_fence")) {
						if(!DatabaseModule.Block.isMailboxBlock(block.getLocation()) && DatabaseModule.Block.placeMailbox(player.getUniqueId(), block.getLocation())) {
							MailboxModule.log(player.getUniqueId(), null, MailboxModule.Action.PLACED, null, block.getLocation());
						} else event.setCancelled(true);
					} else {
						event.setCancelled(true);
						player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.BLOCK_BELOWERROR));
					}
				} else {
					event.setCancelled(true);
					player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.BLOCK_PLACEERROR));
				}
			} else event.setCancelled(true);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onBreak(final BlockBreakEvent event) {
		final Block block = event.getBlock();
		if(block != null && block.getType().equals(mailbox.getMaterial()) && DatabaseModule.Block.isMailboxBlock(block.getLocation())) {
			final Player player = event.getPlayer();
			if(canBuildBreak(player, block.getLocation()) && canBreak(player, block.getLocation())) {
				if(PermissionModule.hasPermission(player, PermissionModule.Type.BLOCK_PLAYER_BREAK)) {
					if(DatabaseModule.Block.isMailboxOwner(player.getUniqueId(), block.getLocation())) {
						if(DatabaseModule.Block.breakMailbox(block.getLocation())) {
							event.setDropItems(false);
							block.getWorld().dropItemNaturally(block.getLocation(), new MailboxItem().getItem(null));
							MailboxModule.log(player.getUniqueId(), null, MailboxModule.Action.BROKE, null, block.getLocation());
						} else event.setCancelled(true);
					} else if(PermissionModule.hasPermission(player, PermissionModule.Type.BLOCK_ADMIN_BREAK)){
						final OfflinePlayer owner = Bukkit.getOfflinePlayer(DatabaseModule.Block.getMailboxOwner(block.getLocation()));
						if(DatabaseModule.Block.breakMailbox(block.getLocation())) {
							event.setDropItems(false);
							block.getWorld().dropItemNaturally(block.getLocation(), new MailboxItem().getItem(null));
							MailboxModule.log(player.getUniqueId(), owner.getUniqueId(), MailboxModule.Action.ADMIN_BROKE, null, block.getLocation());
							player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.BLOCK_ADMINBROKE) + " " + owner.getName());
						} else event.setCancelled(true);
					} else {
						event.setCancelled(true);
						player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.BLOCK_OWNERERROR));
					}
				} else {
					event.setCancelled(true);
					player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.BLOCK_BREAKERROR));
				}
			} else event.setCancelled(true);
		} else if(block != null && block.getType().toString().toLowerCase().endsWith("_fence")) {
			final Block blockAbove = new Location(block.getLocation().getWorld(), block.getLocation().getBlockX(), (block.getLocation().getBlockY() + 1), block.getLocation().getBlockZ()).getBlock();
			if(blockAbove.getType().equals(mailbox.getMaterial()) && DatabaseModule.Block.isMailboxBlock(blockAbove.getLocation())) event.setCancelled(true);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onInventoryMove(final InventoryMoveItemEvent event) {
		final Location block = event.getDestination().getLocation();
		if(block != null && block.getBlock().getType().equals(mailbox.getMaterial()) && DatabaseModule.Block.isMailboxBlock(block)) if(event.getSource().getType().equals(InventoryType.HOPPER)) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPistonExtend(final BlockPistonExtendEvent event) {
		Boolean cancel = false;
		final List<Block> blocks = event.getBlocks();
		for(final Block block : blocks) {
			if(block != null) {
				if(block.getType().equals(mailbox.getMaterial()) && DatabaseModule.Block.isMailboxBlock(block.getLocation())) cancel = true;
				if(block.getType().toString().toLowerCase().endsWith("_fence")) {
					final Block blockAbove = new Location(block.getLocation().getWorld(), block.getLocation().getBlockX(), (block.getLocation().getBlockY() + 1), block.getLocation().getBlockZ()).getBlock();
					if(blockAbove.getType().equals(mailbox.getMaterial()) && DatabaseModule.Block.isMailboxBlock(blockAbove.getLocation())) cancel = true;
				}
			}
		}
		event.setCancelled(cancel);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPistonRetract(final BlockPistonRetractEvent event) {
		Boolean cancel = false;
		final List<Block> blocks = event.getBlocks();
		for(final Block block : blocks) {
			if(block != null) {
				if(block.getType().equals(mailbox.getMaterial()) && DatabaseModule.Block.isMailboxBlock(block.getLocation())) cancel = true;
				if(block.getType().toString().toLowerCase().endsWith("_fence")) {
					final Block blockAbove = new Location(block.getLocation().getWorld(), block.getLocation().getBlockX(), (block.getLocation().getBlockY() + 1), block.getLocation().getBlockZ()).getBlock();
					if(blockAbove.getType().equals(mailbox.getMaterial()) && DatabaseModule.Block.isMailboxBlock(blockAbove.getLocation())) cancel = true;
				}
			}
		}
		event.setCancelled(cancel);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onBlockExplode(final BlockExplodeEvent event) {
		Boolean cancel = false;
		final List<Block> blocks = event.blockList();
		for(final Block block : blocks) {
			if(block != null) {
				if(block.getType().equals(mailbox.getMaterial()) && DatabaseModule.Block.isMailboxBlock(block.getLocation())) cancel = true;
				if(block.getType().toString().toLowerCase().endsWith("_fence")) {
					final Block blockAbove = new Location(block.getLocation().getWorld(), block.getLocation().getBlockX(), (block.getLocation().getBlockY() + 1), block.getLocation().getBlockZ()).getBlock();
					if(blockAbove.getType().equals(mailbox.getMaterial()) && DatabaseModule.Block.isMailboxBlock(blockAbove.getLocation())) cancel = true;
				}
			}
		}
		event.setCancelled(cancel);
	}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onEntityExplode(final EntityExplodeEvent event) {
		Boolean cancel = false;
		final List<Block> blocks = event.blockList();
		for(final Block block : blocks) {
			if(block != null) {
				if(block.getType().equals(mailbox.getMaterial()) && DatabaseModule.Block.isMailboxBlock(block.getLocation())) cancel = true;
				if(block.getType().toString().toLowerCase().endsWith("_fence")) {
					final Block blockAbove = new Location(block.getLocation().getWorld(), block.getLocation().getBlockX(), (block.getLocation().getBlockY() + 1), block.getLocation().getBlockZ()).getBlock();
					if(blockAbove.getType().equals(mailbox.getMaterial()) && DatabaseModule.Block.isMailboxBlock(blockAbove.getLocation())) cancel = true;
				}
			}
		}
		event.setCancelled(cancel);
	}
	private final GriefPreventionAPI griefPrevention = (GriefPreventionAPI) IntegrationModule.getInstance().get(IntegrationModule.FPlugin.GRIEF_PREVENTION_API);
	private final RedProtectAPI redProtect = (RedProtectAPI) IntegrationModule.getInstance().get(IntegrationModule.FPlugin.RED_PROTECT_API);
	private final TownyAdvancedAPI townyAdvanced = (TownyAdvancedAPI) IntegrationModule.getInstance().get(IntegrationModule.FPlugin.TOWNY_ADVANCED_API);
	private final WorldGuardAPI worldGuard = (WorldGuardAPI) IntegrationModule.getInstance().get(IntegrationModule.FPlugin.WORLD_GUARD_API);
	private final boolean canBuildBreak(final Player player, final Location location) {
		Boolean canBuildBreak = true;
		if(canBuildBreak && griefPrevention != null) canBuildBreak = griefPrevention.canBuild(player, location);
		if(canBuildBreak && redProtect != null) canBuildBreak = redProtect.canBuild(player, location);
		if(canBuildBreak && worldGuard != null) canBuildBreak = worldGuard.canBuild(player, location);
		return canBuildBreak;
	}
	private final boolean canBuild(final Player player, final Location location) {
		Boolean canBuild = true;
		if(canBuild && townyAdvanced != null) canBuild = townyAdvanced.canBuild(player, location);
		return canBuild;
	}
	private final boolean canBreak(final Player player, final Location location) {
		Boolean canBreak = true;
		if(canBreak && townyAdvanced != null) canBreak = townyAdvanced.canBreak(player, location);
		return canBreak;
	}
}