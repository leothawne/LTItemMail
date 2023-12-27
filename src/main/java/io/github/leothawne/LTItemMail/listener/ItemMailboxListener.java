package io.github.leothawne.LTItemMail.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.inventory.MailboxInventory;
import io.github.leothawne.LTItemMail.item.MailboxItem;
import io.github.leothawne.LTItemMail.item.model.Item;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.MailboxLogModule;
import io.github.leothawne.LTItemMail.module.PermissionModule;

public final class ItemMailboxListener implements Listener {
	final Item mailbox = new MailboxItem();
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onClick(final PlayerInteractEvent event) {
		if(event.hasBlock() && event.getAction().equals(Action.RIGHT_CLICK_BLOCK) && event.getHand().equals(EquipmentSlot.HAND)) {
			final Player player = event.getPlayer();
			final Block block = event.getClickedBlock();
			if(block.getType().equals(mailbox.getMaterial())) if(DatabaseModule.Block.isMailboxBlock(block.getLocation())) {
				event.setCancelled(true);
				if(PermissionModule.hasPermission(player, PermissionModule.Type.BLOCK_PLAYER_USE)) {
					final OfflinePlayer owner = Bukkit.getOfflinePlayer(DatabaseModule.Block.getMailboxOwner(block.getLocation()));
					if(owner.getUniqueId().equals(player.getUniqueId())) {
						player.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_SELFERROR));
					} else player.openInventory(MailboxInventory.getMailboxInventory(MailboxInventory.Type.OUT, null, owner, null));
				} else player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.BLOCK_USEERROR));
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onPlace(final BlockPlaceEvent event) {
		if(!event.isCancelled()) {
			final Player player = event.getPlayer();
			final ItemStack item = event.getItemInHand();
			if(item != null && item.getItemMeta() != null) if(item.getType().equals(mailbox.getMaterial())) if(item.getItemMeta().getLore().size() == 1) if(item.getItemMeta().getLore().get(0).contains("Mailbox@") && item.getItemMeta().getLore().get(0).split("@").length == 2) {
				if(PermissionModule.hasPermission(player, PermissionModule.Type.BLOCK_PLAYER_PLACE)) {
					final Block block = event.getBlockPlaced();
					final Block blockBelow = new Location(block.getLocation().getWorld(), block.getLocation().getBlockX(), (block.getLocation().getBlockY() - 1), block.getLocation().getBlockZ()).getBlock();
					if(blockBelow.getType().toString().toLowerCase().endsWith("_fence")) {
						if(!DatabaseModule.Block.isMailboxBlock(block.getLocation())) if(DatabaseModule.Block.placeMailbox(player.getUniqueId(), block.getLocation())) {
							MailboxLogModule.log(player.getUniqueId(), null, MailboxLogModule.Action.PLACED, null, block.getLocation());
						} else event.setCancelled(true);
					} else {
						event.setCancelled(true);
						player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.BLOCK_BELOWERROR));
					}
				} else {
					event.setCancelled(true);
					player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.BLOCK_PLACEERROR));
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onBreak(final BlockBreakEvent event) {
		if(!event.isCancelled()) {
			final Player player = event.getPlayer();
			final Block block = event.getBlock();
			if(block.getType().equals(mailbox.getMaterial())) if(DatabaseModule.Block.isMailboxBlock(block.getLocation())) {
				if(PermissionModule.hasPermission(player, PermissionModule.Type.BLOCK_PLAYER_BREAK)) {
					if(DatabaseModule.Block.isMailboxOwner(player.getUniqueId(), block.getLocation())) {
						if(DatabaseModule.Block.breakMailbox(block.getLocation())) {
							event.setDropItems(false);
							block.getWorld().dropItemNaturally(block.getLocation(), new MailboxItem().getItem(null));
							MailboxLogModule.log(player.getUniqueId(), null, MailboxLogModule.Action.BROKE, null, block.getLocation());
						} else event.setCancelled(true);
					} else if(PermissionModule.hasPermission(player, PermissionModule.Type.BLOCK_ADMIN_BREAK)){
						final OfflinePlayer owner = Bukkit.getOfflinePlayer(DatabaseModule.Block.getMailboxOwner(block.getLocation()));
						if(DatabaseModule.Block.breakMailbox(block.getLocation())) {
							event.setDropItems(false);
							block.getWorld().dropItemNaturally(block.getLocation(), new MailboxItem().getItem(null));
							MailboxLogModule.log(player.getUniqueId(), owner.getUniqueId(), MailboxLogModule.Action.ADMIN_BROKE, null, block.getLocation());
							player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.BLOCK_ADMINBROKE) + " " + owner.getName());
						} else event.setCancelled(true);
					} else {
						event.setCancelled(true);
						player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.BLOCK_OWNERERROR));
					}
				} else {
					event.setCancelled(true);
					player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.BLOCK_BREAKERROR));
				}
			}
		}
	}
}