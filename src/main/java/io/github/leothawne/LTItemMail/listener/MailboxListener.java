package io.github.leothawne.LTItemMail.listener;

import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.inventory.MailboxInventory;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.type.MailboxType;
import net.milkbowl.vault.economy.EconomyResponse;

public final class MailboxListener implements Listener {
	public MailboxListener(){}
	@EventHandler(priority = EventPriority.MONITOR)
	public final void onInventoryClose(final InventoryCloseEvent event) {
		final Player player = (Player) event.getPlayer();
		final Inventory inventory = event.getInventory();
		final InventoryView inventoryView = player.getOpenInventory();
		if(inventoryView.getTitle().contains(MailboxInventory.getMailboxName(MailboxType.IN, 0)) && inventoryView.getTitle().split("#").length == 2) {
			LTItemMail.getInstance().getPlayerBusy().put(player.getUniqueId(), false);
			final Integer mailboxID = Integer.valueOf(inventoryView.getTitle().split("#")[1]);
			final ItemStack[] contents = inventory.getContents();
			Boolean isEmpty = true;
			for(final ItemStack content : contents) if(content != null) isEmpty = false;
			if(!isEmpty) {
				final LinkedList<ItemStack> items = new LinkedList<>();
				String itemslost = "";
				int count = 0;
				for(final ItemStack content: contents) if(content != null) {
					items.add(content);
					if(itemslost.isBlank() && itemslost.trim().isBlank()) {
						itemslost = content.getAmount() + "x " + content.getType().name();
					} else itemslost = itemslost + ", " + content.getAmount() + "x " + content.getType().name();
					count = count + content.getAmount();
				}
				DatabaseModule.Function.saveLostMailbox(mailboxID, items);
				final String[] mailboxClosedItems = LTItemMail.getInstance().getLanguage().getString("mailbox-closed-items").split("%");
				player.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LTItemMail.getInstance().getLanguage().getString("mailbox-closed") + " " + mailboxClosedItems[0] + "" + ChatColor.GREEN + "" + count + "" + ChatColor.YELLOW + "" + mailboxClosedItems[1] + " " + ChatColor.GREEN + "" + itemslost);
			} else {
				DatabaseModule.Function.saveLostMailbox(mailboxID, new LinkedList<ItemStack>());
				player.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LTItemMail.getInstance().getLanguage().getString("mailbox-closed"));
			}
			inventory.clear();
		} else if(inventoryView.getTitle().equals(MailboxInventory.getMailboxName(MailboxType.IN, null))) {
			LTItemMail.getInstance().getPlayerBusy().put(player.getUniqueId(), false);
			final ItemStack[] contents = inventory.getContents();
			Boolean isEmpty = true;
			for(final ItemStack content : contents) if(content != null) isEmpty = false;
			if(!isEmpty) {
				final LinkedList<ItemStack> items = new LinkedList<>();
				for(final ItemStack content: contents) {
					if(content != null) {
						items.add(content);
					} else items.add(new ItemStack(Material.AIR));
				}
				DatabaseModule.Function.saveMailbox(player.getUniqueId(), items);
			} else player.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LTItemMail.getInstance().getLanguage().getString("mailbox-closed"));
			inventory.clear();
		} else if(inventoryView.getTitle().equals(MailboxInventory.getMailboxName(MailboxType.OUT, null))) {
			final Player sender = (Player) event.getPlayer();
			final Player recipient = (Player) inventory.getHolder();
			final ItemStack[] contents = inventory.getContents();
			final LinkedList<ItemStack> contentsarray = new LinkedList<ItemStack>();
			for(final ItemStack content : contents) if(content == null) {
				contentsarray.add(new ItemStack(Material.AIR));
			} else contentsarray.add(content);
			Boolean isEmpty = true;
			int count = 0;
			for(final ItemStack content : contentsarray) if(content.getType() != Material.AIR) {
				isEmpty = false;
				count = count + content.getAmount();
			}
			inventory.clear();
			if(!isEmpty) {
				if(!LTItemMail.getInstance().getPlayerBusy().get(recipient.getUniqueId()).booleanValue()) {
					double newcost = 0;
					if(LTItemMail.getInstance().getConfiguration().getBoolean("cost-per-item")) {
						newcost = LTItemMail.getInstance().getConfiguration().getDouble("mail-cost") * count;
					} else newcost = LTItemMail.getInstance().getConfiguration().getDouble("mail-cost");
					if(LTItemMail.getInstance().getEconomy() != null) {
						if(LTItemMail.getInstance().getEconomy().has(sender, newcost)) {
							final EconomyResponse er = LTItemMail.getInstance().getEconomy().withdrawPlayer(sender, newcost);
							if(er.transactionSuccess()) {
								final String[] mailboxPaid = LTItemMail.getInstance().getLanguage().getString("mailbox-paid").split("%");
								sender.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + mailboxPaid[0] + "" + ChatColor.GREEN + newcost + "" + ChatColor.YELLOW + "" + mailboxPaid[1]);
								sendBox(sender, recipient, contentsarray);
							} else {
								sender.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LTItemMail.getInstance().getLanguage().getString("transaction-error"));
								for(final ItemStack item : contentsarray) sender.getInventory().addItem(item);
							}
						} else {
							final String[] transactionNoMoney = LTItemMail.getInstance().getLanguage().getString("transaction-no-money").split("%");
							sender.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + transactionNoMoney[0] + "" + ChatColor.GREEN + newcost + "" + ChatColor.YELLOW + "" + transactionNoMoney[1]);
							for(final ItemStack item : contentsarray) sender.getInventory().addItem(item);
						}
					} else sendBox(sender, recipient, contentsarray);
				} else {
					final String[] recipientBusy = LTItemMail.getInstance().getLanguage().getString("recipient-busy").split("%");
					sender.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.AQUA + "" + recipient.getName() + "" + ChatColor.YELLOW + "" + recipientBusy[1]);
					for(final ItemStack item : contentsarray) sender.getInventory().addItem(item);
				}
			} else sender.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LTItemMail.getInstance().getLanguage().getString("mailbox-aborted"));
		}
	}
	private static final void sendBox(final CommandSender sender, final Player recipient, final LinkedList<ItemStack> contentsarray) {
		final String[] mailboxSent = LTItemMail.getInstance().getLanguage().getString("mailbox-sent").split("%");
		final String[] mailboxFrom = LTItemMail.getInstance().getLanguage().getString("mailbox-from").split("%");
		final String[] mailboxOpening = LTItemMail.getInstance().getLanguage().getString("mailbox-opening-seconds").split("%");
		sender.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + mailboxSent[0] + "" + ChatColor.AQUA + "" + recipient.getName() + "" + ChatColor.YELLOW + "" + mailboxSent[1]);
		LTItemMail.getInstance().getPlayerBusy().put(recipient.getUniqueId(), true);
		if(LTItemMail.getInstance().getConfiguration().getBoolean("use-title") == true) {
			recipient.sendTitle(ChatColor.AQUA + "" + mailboxFrom[0] +  "" + ChatColor.GREEN + "" + sender.getName(), ChatColor.AQUA + "" + mailboxOpening[0] + "" + ChatColor.GREEN + "" + LTItemMail.getInstance().getConfiguration().getInt("mail-time") + "" + ChatColor.AQUA + "" + mailboxOpening[1], 20 * 1, 20 * LTItemMail.getInstance().getConfiguration().getInt("mail-time"), 20 * 1);
		} else {
			recipient.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.AQUA + "" + mailboxFrom[0] + "" + ChatColor.GREEN + "" + sender.getName() + "" + ChatColor.AQUA + "" + mailboxFrom[1]);
			recipient.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.AQUA + "" + mailboxOpening[0] + "" + ChatColor.GREEN + "" + LTItemMail.getInstance().getConfiguration().getInt("mail-time") + "" + ChatColor.AQUA + "" + mailboxOpening[1]);
		}
		new BukkitRunnable() {
			@Override
			public final void run() {
				recipient.openInventory(MailboxInventory.getMailboxInventory(MailboxType.IN, null, null, contentsarray));
				sender.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LTItemMail.getInstance().getLanguage().getString("mailbox-delivered"));
			}
		}.runTaskLater(LTItemMail.getInstance(), 20 * LTItemMail.getInstance().getConfiguration().getInt("mail-time") + 2);
	}
}