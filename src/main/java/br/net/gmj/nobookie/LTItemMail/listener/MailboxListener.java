package br.net.gmj.nobookie.LTItemMail.listener;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.Event.Result;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import br.net.gmj.nobookie.LTItemMail.LTItemMail;
import br.net.gmj.nobookie.LTItemMail.entity.LTPlayer;
import br.net.gmj.nobookie.LTItemMail.event.EntitySendMailEvent;
import br.net.gmj.nobookie.LTItemMail.inventory.MailboxInventory;
import br.net.gmj.nobookie.LTItemMail.item.MailboxItem;
import br.net.gmj.nobookie.LTItemMail.module.ConfigurationModule;
import br.net.gmj.nobookie.LTItemMail.module.ConsoleModule;
import br.net.gmj.nobookie.LTItemMail.module.DatabaseModule;
import br.net.gmj.nobookie.LTItemMail.module.EconomyModule;
import br.net.gmj.nobookie.LTItemMail.module.LanguageModule;
import br.net.gmj.nobookie.LTItemMail.module.MailboxModule;
import br.net.gmj.nobookie.LTItemMail.module.PermissionModule;
import br.net.gmj.nobookie.LTItemMail.util.BukkitUtil;
import net.md_5.bungee.api.ChatColor;

public final class MailboxListener implements Listener {
	public MailboxListener() {
		Bukkit.getServer().getPluginManager().registerEvents(this, LTItemMail.getInstance());
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onInventoryClose(final InventoryCloseEvent event) {
		final Player player = (Player) event.getPlayer();
		final Inventory inventory = event.getInventory();
		final InventoryView inventoryView = player.getOpenInventory();
		if(inventoryView.getTitle().contains(MailboxInventory.getName(MailboxInventory.Type.IN, null, null)) && inventoryView.getTitle().split("#").length == 2) {
			final Integer mailboxID = Integer.valueOf(inventoryView.getTitle().split("#")[1]);
			final ItemStack[] contents = inventory.getContents();
			final Boolean isEmpty = BukkitUtil.isMailboxEmpty(contents);
			if(!isEmpty) {
				final LinkedList<ItemStack> items = BukkitUtil.getMailboxContents(contents);
				DatabaseModule.Virtual.updateMailbox(mailboxID, items);
			} else DatabaseModule.Virtual.updateMailbox(mailboxID, new LinkedList<ItemStack>());
			player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_CLOSED));
			inventory.clear();
		}
		if(inventoryView.getTitle().contains(MailboxInventory.getName(MailboxInventory.Type.OUT, null, null)) && inventoryView.getTitle().split("@").length == 2) {
			final Player sender = (Player) event.getPlayer();
			final LTPlayer receiver = LTPlayer.fromName(inventoryView.getTitle().split("@")[1]);
			final ItemStack[] contents = inventory.getContents();
			final boolean isEmpty = BukkitUtil.isMailboxEmpty(contents);
			if(!isEmpty) {
				final LinkedList<ItemStack> items = BukkitUtil.getMailboxContents(contents);
				final int count = BukkitUtil.getItemsCount(items);
				inventory.clear();
				final String label = contents[30].getItemMeta().getLore().get(1).replace(ChatColor.COLOR_CHAR + "e", "");
				double newcost = 0.0;
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_TYPE_COST)) {
					newcost = (Double) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_COST) * count;
				} else newcost = (Double) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_COST);
				final EconomyModule economy = EconomyModule.getInstance();
				if(economy != null) {
					if(economy.has(sender, newcost)) {
						if(economy.withdraw(player, newcost)) {
							MailboxModule.log(sender.getUniqueId(), null, MailboxModule.Action.PAID, null, newcost, null, null);
							final EntitySendMailEvent sendEvent = new EntitySendMailEvent(sender, receiver, items, true, newcost);
							Bukkit.getPluginManager().callEvent(sendEvent);
							if(!sendEvent.isCancelled()) {
								final String[] mailboxPaid = LanguageModule.get(LanguageModule.Type.TRANSACTION_PAID).split("%");
								sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + mailboxPaid[0] + "" + ChatColor.GREEN + newcost + "" + ChatColor.YELLOW + "" + mailboxPaid[1]);
								MailboxModule.send(sender, receiver, items, label);
							} else {
								economy.deposit(sender, newcost);
								MailboxModule.log(sender.getUniqueId(), null, MailboxModule.Action.REFUNDED, null, newcost, null, null);
								sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_BLOCKED));
								MailboxModule.log(sender.getUniqueId(), null, MailboxModule.Action.CANCELED, null, null, null, null);
								MailboxModule.send(Bukkit.getConsoleSender(), LTPlayer.fromUUID(sender.getUniqueId()), items, sendEvent.getCancelReason());
							}
						} else {
							sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.TRANSACTION_ERROR));
							MailboxModule.log(sender.getUniqueId(), null, MailboxModule.Action.CANCELED, null, null, null, null);
							MailboxModule.send(Bukkit.getConsoleSender(), LTPlayer.fromUUID(sender.getUniqueId()), items, label);
						}
					} else {
						final String[] transactionNoMoney = LanguageModule.get(LanguageModule.Type.TRANSACTION_NOMONEY).split("%");
						sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + transactionNoMoney[0] + "" + ChatColor.GREEN + newcost + "" + ChatColor.YELLOW + "" + transactionNoMoney[1]);
						MailboxModule.log(sender.getUniqueId(), null, MailboxModule.Action.CANCELED, null, null, null, null);
						MailboxModule.send(Bukkit.getConsoleSender(), LTPlayer.fromUUID(sender.getUniqueId()), items, label);
					}
				} else {
					final EntitySendMailEvent sendEvent = new EntitySendMailEvent(sender, receiver, items, false, 0.0);
					Bukkit.getPluginManager().callEvent(sendEvent);
					if(!sendEvent.isCancelled()) {
						MailboxModule.send(sender, receiver, items, label);
					} else {
						sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_BLOCKED));
						MailboxModule.log(sender.getUniqueId(), null, MailboxModule.Action.CANCELED, null, null, null, null);
						MailboxModule.send(Bukkit.getConsoleSender(), LTPlayer.fromUUID(sender.getUniqueId()), items, sendEvent.getCancelReason());
					}
				}
			} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_ABORTED));
		}
		if(inventoryView.getTitle().contains(MailboxInventory.getName(MailboxInventory.Type.IN_PENDING, null, null)) && inventoryView.getTitle().split("!").length == 2) inventory.clear();
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onItemDrop(final PlayerDropItemEvent event) {
		final LTPlayer player = LTPlayer.fromUUID(event.getPlayer().getUniqueId());
		final ItemStack ee = event.getItemDrop().getItemStack();
		if(PermissionModule.hasPermission(player.getBukkitPlayer().getPlayer(), PermissionModule.Type.CMD_ADMIN_BYPASS) && ee.getType().equals(Material.PAPER) && ee.getItemMeta() != null && ee.getItemMeta().getDisplayName().equalsIgnoreCase("give me a mailbox")) {
			int i = 0;
			for(i = 0; i < ee.getAmount(); i++) if(!player.giveMailboxBlock()) break;
			ee.setAmount(ee.getAmount() - i);
			ConsoleModule.info(ChatColor.RESET + "" + ChatColor.ITALIC + "Gave " + i + " x " + new MailboxItem().getName() + ChatColor.RESET + ChatColor.ITALIC + " to " + player.getName());
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onInventoryDrag(final InventoryDragEvent event) {
		final Player player = (Player) event.getWhoClicked();
		final InventoryView inventoryView = player.getOpenInventory();
		if((inventoryView.getTitle().contains(MailboxInventory.getName(MailboxInventory.Type.IN, null, null)) && inventoryView.getTitle().split("#").length == 2) || (inventoryView.getTitle().contains(MailboxInventory.getName(MailboxInventory.Type.IN_PENDING, null, null)) && inventoryView.getTitle().split("!").length == 2)) {
			final Inventory top = inventoryView.getTopInventory();
			final Inventory dragged = event.getInventory();
			if(top == dragged) {
				event.setResult(Result.DENY);
				event.setCancelled(true);
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onInventoryClick(final InventoryClickEvent event) {
		final Player player = (Player) event.getWhoClicked();
		final InventoryView inventoryView = player.getOpenInventory();
		if(inventoryView.getTitle().contains(MailboxInventory.getName(MailboxInventory.Type.IN, null, null)) && inventoryView.getTitle().split("#").length == 2) {
			final Inventory top = inventoryView.getTopInventory();
			final Inventory bottom = inventoryView.getBottomInventory();
			final Inventory clicked = event.getClickedInventory();
			final InventoryAction action = event.getAction();
			if(clicked == top && (action == InventoryAction.PLACE_SOME || action == InventoryAction.PLACE_ALL || action == InventoryAction.PLACE_ONE || action == InventoryAction.SWAP_WITH_CURSOR || action == InventoryAction.HOTBAR_SWAP || action == InventoryAction.HOTBAR_MOVE_AND_READD)) {
				event.setResult(Result.DENY);
				event.setCancelled(true);
			} else if(clicked == bottom && action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
				event.setResult(Result.DENY);
				event.setCancelled(true);
			}
		}
		if(inventoryView.getTitle().contains(MailboxInventory.getName(MailboxInventory.Type.IN_PENDING, null, null)) && inventoryView.getTitle().split("!").length == 2) {
			final InventoryAction action = event.getAction();
			if(action == InventoryAction.PICKUP_ALL || action == InventoryAction.PICKUP_HALF || action == InventoryAction.PICKUP_ONE || action == InventoryAction.PICKUP_SOME || action == InventoryAction.PLACE_SOME || action == InventoryAction.PLACE_ALL || action == InventoryAction.PLACE_ONE || action == InventoryAction.SWAP_WITH_CURSOR || action == InventoryAction.HOTBAR_SWAP || action == InventoryAction.HOTBAR_MOVE_AND_READD || action == InventoryAction.MOVE_TO_OTHER_INVENTORY) {
				event.setResult(Result.DENY);
				event.setCancelled(true);
			}
		}
		if((inventoryView.getTitle().contains(MailboxInventory.getName(MailboxInventory.Type.IN_PENDING, null, null)) && inventoryView.getTitle().split("!").length == 2) || (inventoryView.getTitle().contains(MailboxInventory.getName(MailboxInventory.Type.IN, null, null)) && inventoryView.getTitle().split("#").length == 2) || (inventoryView.getTitle().contains(MailboxInventory.getName(MailboxInventory.Type.OUT, null, null)) && inventoryView.getTitle().split("@").length == 2)) if(event.getRawSlot() > 26 && event.getRawSlot() < 36 && event.getCurrentItem() != null) {
			final ItemStack selected = event.getCurrentItem();
			event.setResult(Result.DENY);
			event.setCancelled(true);
			final Inventory inventory = event.getClickedInventory();
			final ItemStack[] contents = inventory.getContents();
			if(selected.getType().equals(Material.EMERALD)) {
				final boolean isEmpty = BukkitUtil.isMailboxEmpty(contents);
				double newcost = 0.0;
				if(!isEmpty) {
					final int count = BukkitUtil.getItemsCount(BukkitUtil.getMailboxContents(contents));
					if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_TYPE_COST)) {
						newcost = (Double) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_COST) * count;
					} else newcost = (Double) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_COST);
				}
				final ItemStack emerald = event.getCurrentItem();
				final ItemMeta emeraldMeta = emerald.getItemMeta();
				final List<String> emeraldLore = emeraldMeta.getLore();
				if(EconomyModule.getInstance() != null) {
					emeraldLore.set(0, ChatColor.RESET + "" + ChatColor.GREEN + "$ " + newcost);
				} else emeraldLore.set(0, ChatColor.RESET + "" + ChatColor.DARK_RED + LanguageModule.get(LanguageModule.Type.MAILBOX_COSTERROR));
				emeraldMeta.setLore(emeraldLore);
				emerald.setItemMeta(emeraldMeta);
				inventory.setItem(event.getRawSlot(), emerald);
			}
			final String[] elements = inventoryView.getTitle().split("!");
			if(elements.length == 2) {
				final Integer mailboxID = Integer.parseInt(elements[1]);
				if(selected.getType().equals(Material.BARRIER)) {
					DatabaseModule.Virtual.setStatus(mailboxID, DatabaseModule.Virtual.Status.DENIED);
					final UUID from = DatabaseModule.Virtual.getMailboxFrom(mailboxID);
					if(from != null) {
						MailboxModule.log(player.getUniqueId(), from, MailboxModule.Action.GAVE_BACK, mailboxID, null, null, null);
						final Integer backMailboxID = MailboxModule.send(Bukkit.getConsoleSender(), LTPlayer.fromUUID(from), BukkitUtil.getMailboxContents(contents), player.getName() + " " + LanguageModule.get(LanguageModule.Type.MAILBOX_RETURNED));
						DatabaseModule.Virtual.setStatus(backMailboxID, DatabaseModule.Virtual.Status.ACCEPTED);
					}
					DatabaseModule.Virtual.setMailboxDeleted(mailboxID);
					inventoryView.close();
				}
				if(selected.getType().equals(Material.ENDER_EYE)) {
					DatabaseModule.Virtual.setStatus(mailboxID, DatabaseModule.Virtual.Status.ACCEPTED);
					event.getInventory().clear();
					inventoryView.close();
					player.openInventory(MailboxInventory.getInventory(MailboxInventory.Type.IN, mailboxID, null, DatabaseModule.Virtual.getMailbox(mailboxID), DatabaseModule.Virtual.getMailboxFrom(mailboxID), DatabaseModule.Virtual.getMailboxLabel(mailboxID), false));
				}
			}
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onSendMail(final EntitySendMailEvent event) {
		final LTPlayer playerTo = event.getPlayerTo();
		if(event.getFrom() instanceof Player) {
			final LTPlayer playerFrom = LTPlayer.fromUUID(((Player) event.getFrom()).getUniqueId());
			if(playerFrom != null) {
				if(!playerFrom.isRegistered()) DatabaseModule.User.register(playerFrom);
				if(playerFrom.isBanned()) {
					event.setCancelReason(LanguageModule.get(LanguageModule.Type.PLAYER_BANNED) + " (@" + playerFrom.getName() + " => @" + playerTo.getName() + ")");
					event.setCancelled(true);
				} else if(!event.isCancelled()) {
					int sent_count = playerFrom.getMailSentCount();
					sent_count++;
					DatabaseModule.User.setSentCount(playerFrom.getUniqueId(), sent_count);
				}
			}
		}
		if(playerTo != null) if(!event.isCancelled()) {
			if(!playerTo.isRegistered()) DatabaseModule.User.register(playerTo);
			int received_count = playerTo.getMailReceivedCount();
			received_count++;
			DatabaseModule.User.setReceivedCount(playerTo.getUniqueId(), received_count);
		}
	}
}