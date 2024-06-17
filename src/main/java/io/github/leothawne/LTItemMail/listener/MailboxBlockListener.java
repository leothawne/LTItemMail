package io.github.leothawne.LTItemMail.listener;

import java.util.HashMap;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
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

import io.github.leothawne.LTItemMail.LTPlayer;
import io.github.leothawne.LTItemMail.inventory.MailboxInventory;
import io.github.leothawne.LTItemMail.item.MailboxItem;
import io.github.leothawne.LTItemMail.item.model.Item;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.IntegrationModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.MailboxModule;
import io.github.leothawne.LTItemMail.module.PermissionModule;
import io.github.leothawne.LTItemMail.module.api.LTBlueMap;
import io.github.leothawne.LTItemMail.module.api.LTDecentHolograms;
import io.github.leothawne.LTItemMail.module.api.LTDynmap;
import io.github.leothawne.LTItemMail.module.api.LTGriefPrevention;
import io.github.leothawne.LTItemMail.module.api.LTRedProtect;
import io.github.leothawne.LTItemMail.module.api.LTTownyAdvanced;
import io.github.leothawne.LTItemMail.module.api.LTWorldGuard;
import net.md_5.bungee.api.ChatColor;

public final class MailboxBlockListener implements Listener {
	private final Item mailbox = new MailboxItem();
	private final LTDynmap dynmap = (LTDynmap) IntegrationModule.getInstance().get(IntegrationModule.Function.DYNMAP);
	private final LTBlueMap blueMap = (LTBlueMap) IntegrationModule.getInstance().get(IntegrationModule.Function.BLUEMAP);
	private final LTDecentHolograms decentHolograms = (LTDecentHolograms) IntegrationModule.getInstance().get(IntegrationModule.Function.DECENTHOLOGRAMS);
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onClick(final PlayerInteractEvent event) {
		final Player player = event.getPlayer();
		if(event.hasBlock() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getHand().equals(EquipmentSlot.HAND)) {
			final Block block = event.getClickedBlock();
			if(block.getType().toString().endsWith("_SHULKER_BOX")) if(DatabaseModule.Block.isMailboxBlock(block.getLocation())) {
				event.setCancelled(true);
				event.setUseInteractedBlock(Result.DENY);
				event.setUseItemInHand(Result.DENY);
				if(player.isSneaking()) return;
				if(canInteract(player, block.getLocation())) {
					if(PermissionModule.hasPermission(player, PermissionModule.Type.BLOCK_PLAYER_USE)) {
						final LTPlayer owner = LTPlayer.fromUUID(DatabaseModule.Block.getMailboxOwner(block.getLocation()));
						if(owner.getUniqueId().equals(player.getUniqueId()) && !PermissionModule.hasPermission(player, PermissionModule.Type.CMD_ADMIN_BYPASS)) {
							HashMap<Integer, String> mailboxes = null;
							if((mailboxes = DatabaseModule.Virtual.getMailboxesList(player.getUniqueId())).size() > 0) {
								Boolean first = false;
								for(final Integer id : mailboxes.keySet()) if(DatabaseModule.Virtual.getStatus(id).equals(DatabaseModule.Virtual.Status.PENDING)) {
									first = true;
									break;
								}
								if(first) {
									player.performCommand("ltitemmail:itemmail");
								} else player.performCommand("ltitemmail:itemmail list");
							} else player.performCommand("ltitemmail:itemmail list");
						} else player.openInventory(MailboxInventory.getInventory(MailboxInventory.Type.OUT, null, owner, null, player.getUniqueId(), "", false));
					} else player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.BLOCK_USEERROR));
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onPlace(final BlockPlaceEvent event) {
		final ItemStack item = event.getItemInHand();
		if(item != null && item.getItemMeta() != null) if(item.getType().toString().endsWith("_SHULKER_BOX")) if(item.getItemMeta().getLore() != null && item.getItemMeta().getLore().size() == 1) if(item.getItemMeta().getLore().get(0).contains(mailbox.getDescription(null).get(0)) && item.getItemMeta().getLore().get(0).split("@").length == 2) {
			final Player player = event.getPlayer();
			final Block block = event.getBlockPlaced();
			if(canBuildBreak(player, block.getLocation()) && canBuild(player, block.getLocation())) {
				if(PermissionModule.hasPermission(player, PermissionModule.Type.BLOCK_PLAYER_PLACE)) {
					final Block blockBelow = new Location(block.getLocation().getWorld(), block.getLocation().getBlockX(), (block.getLocation().getBlockY() - 1), block.getLocation().getBlockZ()).getBlock();
					if(blockBelow.getType().toString().endsWith("_FENCE") || blockBelow.getType().toString().endsWith("_WALL")) {
						if(!DatabaseModule.Block.isMailboxBlock(block.getLocation()) && DatabaseModule.Block.placeMailbox(player.getUniqueId(), block.getLocation())) {
							if(dynmap != null) dynmap.createMarker(player, block.getLocation());
							if(blueMap != null) blueMap.createMarker(player, block.getLocation());
							if(decentHolograms != null) decentHolograms.createHolo(player, block.getLocation());
							MailboxModule.log(player.getUniqueId(), null, MailboxModule.Action.PLACED, null, null, null, block.getLocation());
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
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onBreak(final BlockBreakEvent event) {
		final Block block = event.getBlock();
		if(block != null && block.getType().toString().endsWith("_SHULKER_BOX") && DatabaseModule.Block.isMailboxBlock(block.getLocation())) {
			final ItemStack newMailbox = mailbox.getItem(null);
			newMailbox.setType(block.getType());
			final Player player = event.getPlayer();
			if(canBuildBreak(player, block.getLocation()) && canBreak(player, block.getLocation())) {
				if(PermissionModule.hasPermission(player, PermissionModule.Type.BLOCK_PLAYER_BREAK)) {
					if(DatabaseModule.Block.isMailboxOwner(player.getUniqueId(), block.getLocation())) {
						if(DatabaseModule.Block.breakMailbox(block.getLocation())) {
							event.setDropItems(false);
							block.getWorld().dropItem(block.getLocation(), newMailbox);
							if(dynmap != null) dynmap.deleteMarker(player, block.getLocation());
							if(blueMap != null) blueMap.deleteMarker(player, block.getLocation());
							if(decentHolograms != null) decentHolograms.deleteHolo(player, block.getLocation());
							MailboxModule.log(player.getUniqueId(), null, MailboxModule.Action.BROKE, null, null, null, block.getLocation());
						} else event.setCancelled(true);
					} else if(PermissionModule.hasPermission(player, PermissionModule.Type.BLOCK_ADMIN_BREAK)){
						final LTPlayer owner = LTPlayer.fromUUID(DatabaseModule.Block.getMailboxOwner(block.getLocation()));
						if(DatabaseModule.Block.breakMailbox(block.getLocation())) {
							event.setDropItems(false);
							block.getWorld().dropItem(block.getLocation(), newMailbox);
							if(dynmap != null) dynmap.deleteMarker(owner.getBukkitPlayer(), block.getLocation());
							if(blueMap != null) blueMap.deleteMarker(owner.getBukkitPlayer(), block.getLocation());
							if(decentHolograms != null) decentHolograms.deleteHolo(owner.getBukkitPlayer(), block.getLocation());
							MailboxModule.log(player.getUniqueId(), owner.getUniqueId(), MailboxModule.Action.ADMIN_BROKE, null, null, null, block.getLocation());
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
		} else if(block != null && (block.getType().toString().endsWith("_FENCE") || block.getType().toString().endsWith("_WALL"))) {
			final Block blockAbove = new Location(block.getLocation().getWorld(), block.getLocation().getBlockX(), (block.getLocation().getBlockY() + 1), block.getLocation().getBlockZ()).getBlock();
			if(blockAbove.getType().toString().endsWith("_SHULKER_BOX") && DatabaseModule.Block.isMailboxBlock(blockAbove.getLocation())) event.setCancelled(true);
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onInventoryMove(final InventoryMoveItemEvent event) {
		final Location block = event.getDestination().getLocation();
		if(block != null && block.getBlock().getType().toString().endsWith("_SHULKER_BOX") && DatabaseModule.Block.isMailboxBlock(block)) if(event.getSource().getType().equals(InventoryType.HOPPER)) event.setCancelled(true);
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onPistonExtend(final BlockPistonExtendEvent event) {
		Boolean cancel = false;
		final List<Block> blocks = event.getBlocks();
		for(final Block block : blocks) {
			if(block != null) {
				if(block.getType().toString().endsWith("_SHULKER_BOX") && DatabaseModule.Block.isMailboxBlock(block.getLocation())) cancel = true;
				if(block.getType().toString().endsWith("_FENCE") || block.getType().toString().endsWith("_WALL")) {
					final Block blockAbove = new Location(block.getLocation().getWorld(), block.getLocation().getBlockX(), (block.getLocation().getBlockY() + 1), block.getLocation().getBlockZ()).getBlock();
					if(blockAbove.getType().toString().endsWith("_SHULKER_BOX") && DatabaseModule.Block.isMailboxBlock(blockAbove.getLocation())) cancel = true;
				}
			}
		}
		event.setCancelled(cancel);
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onPistonRetract(final BlockPistonRetractEvent event) {
		Boolean cancel = false;
		final List<Block> blocks = event.getBlocks();
		for(final Block block : blocks) {
			if(block != null) {
				if(block.getType().toString().endsWith("_SHULKER_BOX") && DatabaseModule.Block.isMailboxBlock(block.getLocation())) cancel = true;
				if(block.getType().toString().endsWith("_FENCE") || block.getType().toString().endsWith("_WALL")) {
					final Block blockAbove = new Location(block.getLocation().getWorld(), block.getLocation().getBlockX(), (block.getLocation().getBlockY() + 1), block.getLocation().getBlockZ()).getBlock();
					if(blockAbove.getType().toString().endsWith("_SHULKER_BOX") && DatabaseModule.Block.isMailboxBlock(blockAbove.getLocation())) cancel = true;
				}
			}
		}
		event.setCancelled(cancel);
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onBlockExplode(final BlockExplodeEvent event) {
		Boolean cancel = false;
		final List<Block> blocks = event.blockList();
		for(final Block block : blocks) {
			if(block != null) {
				if(block.getType().toString().endsWith("_SHULKER_BOX") && DatabaseModule.Block.isMailboxBlock(block.getLocation())) {
					blocks.remove(block);
					cancel = true;
				}
				if(block.getType().toString().endsWith("_FENCE") || block.getType().toString().endsWith("_WALL")) {
					final Block blockAbove = new Location(block.getLocation().getWorld(), block.getLocation().getBlockX(), (block.getLocation().getBlockY() + 1), block.getLocation().getBlockZ()).getBlock();
					if(blockAbove.getType().toString().endsWith("_SHULKER_BOX") && DatabaseModule.Block.isMailboxBlock(blockAbove.getLocation())) {
						blocks.remove(block);
						blocks.remove(blockAbove);
						cancel = true;
					}
				}
			}
		}
		if(cancel) {
			event.getBlock().getWorld().playSound(event.getBlock().getLocation(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1, 1);
			for(final Block block : blocks) {
				block.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, block.getLocation(), 1);
				block.breakNaturally();
			}
		}
		event.setCancelled(cancel);
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onEntityExplode(final EntityExplodeEvent event) {
		Boolean cancel = false;
		final List<Block> blocks = event.blockList();
		for(final Block block : blocks) {
			if(block != null) {
				if(block.getType().toString().endsWith("_SHULKER_BOX") && DatabaseModule.Block.isMailboxBlock(block.getLocation())) {
					blocks.remove(block);
					cancel = true;
				}
				if(block.getType().toString().endsWith("_FENCE") || block.getType().toString().endsWith("_WALL")) {
					final Block blockAbove = new Location(block.getLocation().getWorld(), block.getLocation().getBlockX(), (block.getLocation().getBlockY() + 1), block.getLocation().getBlockZ()).getBlock();
					if(blockAbove.getType().toString().endsWith("_SHULKER_BOX") && DatabaseModule.Block.isMailboxBlock(blockAbove.getLocation())) {
						blocks.remove(block);
						blocks.remove(blockAbove);
						cancel = true;
					}
				}
			}
		}
		if(cancel) {
			event.getLocation().getWorld().playSound(event.getLocation(), Sound.ENTITY_GENERIC_EXPLODE, SoundCategory.BLOCKS, 1, 1);
			for(final Block block : blocks) {
				block.getLocation().getWorld().spawnParticle(Particle.EXPLOSION_HUGE, block.getLocation(), 1);
				block.breakNaturally();
			}
		}
		event.setCancelled(cancel);
	}
	private final LTGriefPrevention griefPrevention = (LTGriefPrevention) IntegrationModule.getInstance().get(IntegrationModule.Function.GRIEFPREVENTION);
	private final LTRedProtect redProtect = (LTRedProtect) IntegrationModule.getInstance().get(IntegrationModule.Function.REDPROTECT);
	private final LTTownyAdvanced townyAdvanced = (LTTownyAdvanced) IntegrationModule.getInstance().get(IntegrationModule.Function.TOWNYADVANCED);
	private final LTWorldGuard worldGuard = (LTWorldGuard) IntegrationModule.getInstance().get(IntegrationModule.Function.WORLDGUARD);
	private final boolean canBuildBreak(final Player player, final Location location) {
		Boolean canBuildBreak = true;
		if(canBuildBreak && griefPrevention != null) canBuildBreak = griefPrevention.canBuildBreak(player, location);
		if(canBuildBreak && redProtect != null) canBuildBreak = redProtect.canBuildBreak(player, location);
		return canBuildBreak;
	}
	private final boolean canBuild(final Player player, final Location location) {
		Boolean canBuild = true;
		if(canBuild && worldGuard != null) canBuild = worldGuard.canBuild(player, location);
		if(canBuild && townyAdvanced != null) canBuild = townyAdvanced.canBuild(player, location);
		return canBuild;
	}
	private final boolean canBreak(final Player player, final Location location) {
		Boolean canBreak = true;
		if(canBreak && worldGuard != null) canBreak = worldGuard.canBreak(player, location);
		if(canBreak && townyAdvanced != null) canBreak = townyAdvanced.canBreak(player, location);
		return canBreak;
	}
	private final boolean canInteract(final Player player, final Location location) {
		Boolean canInteract = true;
		if(canInteract && worldGuard != null) canInteract = worldGuard.canInteract(player, location);
		if(canInteract && townyAdvanced != null) canInteract = townyAdvanced.canInteract(player, location);
		if(canInteract && griefPrevention != null) canInteract = griefPrevention.canInteract(player, location);
		if(canInteract && redProtect != null) canInteract = redProtect.canInteract(player, location);
		return canInteract;
	}
}