/*
 * Copyright (C) 2019 Murilo Amaral Nappi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.leothawne.LTItemMail.listener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
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
import io.github.leothawne.LTItemMail.type.MailboxType;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public final class MailboxListener implements Listener {
	private static LTItemMail plugin;
	private static FileConfiguration configuration;
	private static FileConfiguration language;
	private static HashMap<UUID, Boolean> playerBusy;
	private static Economy economyPlugin;
	public MailboxListener(final LTItemMail plugin, final FileConfiguration configuration, final FileConfiguration language, final HashMap<UUID, Boolean> playerBusy, final Economy economyPlugin){
		MailboxListener.plugin = plugin;
		MailboxListener.configuration = configuration;
		MailboxListener.language = language;
		MailboxListener.playerBusy = playerBusy;
		MailboxListener.economyPlugin = economyPlugin;
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public final void onInventoryClose(final InventoryCloseEvent event) {
		final Player player = (Player) event.getPlayer();
		final Inventory inventory = event.getInventory();
		final InventoryView inventoryView = player.getOpenInventory();
		if(inventoryView.getTitle().equals(MailboxInventory.getMailboxName(MailboxType.IN))) {
			MailboxListener.playerBusy.put(player.getUniqueId(), false);
			final ItemStack[] contents = inventory.getContents();
			boolean isEmpty = true;
			for(final ItemStack content : contents) {
				if(content != null) {
					isEmpty = false;
				}
			}
			if(isEmpty == false) {
				String itemslost = "";
				int count = 0;
				for(final ItemStack content: contents) {
					if(content != null) {
						if(itemslost.isBlank() && itemslost.trim().isBlank()) {
							itemslost = content.getAmount() + "x " + content.getType().name();
						} else itemslost = itemslost + ", " + content.getAmount() + "x " + content.getType().name();
						count = count + content.getAmount();
					}
				}
				final String[] mailboxClosedItems = MailboxListener.language.getString("mailbox-closed-items").split("%");
				player.sendMessage(ChatColor.DARK_GREEN + "[" + MailboxListener.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + language.getString("mailbox-closed") + " " + mailboxClosedItems[0] + "" + ChatColor.GREEN + "" + count + "" + ChatColor.YELLOW + "" + mailboxClosedItems[1] + " " + ChatColor.GREEN + "" + itemslost);
			} else {
				player.sendMessage(ChatColor.DARK_GREEN + "[" + MailboxListener.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + language.getString("mailbox-closed"));
			}
			inventory.clear();
		}
		if(inventoryView.getTitle().equals(MailboxInventory.getMailboxName(MailboxType.OUT))) {
			final Player sender = (Player) event.getPlayer();
			final Player recipient = (Player) inventory.getHolder();
			final ItemStack[] contents = inventory.getContents();
			final LinkedList<ItemStack> contentsarray = new LinkedList<ItemStack>();
			for(final ItemStack content : contents) {
				if(content == null) {
					contentsarray.add(new ItemStack(Material.AIR));
				} else {
					contentsarray.add(content);
				}
			}
			boolean isEmpty = true;
			int count = 0;
			for(final ItemStack content : contentsarray) {
				if(content.getType() != Material.AIR) {
					isEmpty = false;
					count = count + content.getAmount();
				}
			}
			inventory.clear();
			if(isEmpty == false) {
				if(MailboxListener.playerBusy.get(recipient.getUniqueId()).booleanValue() == false) {
					double newcost = 0;
					if(configuration.getBoolean("cost-per-item") == true) {
						newcost = MailboxListener.configuration.getDouble("mail-cost") * count;
					} else {
						newcost = MailboxListener.configuration.getDouble("mail-cost");
					}
					if(MailboxListener.economyPlugin != null) {
						if(MailboxListener.economyPlugin.has(sender, newcost)) {
							final EconomyResponse er = MailboxListener.economyPlugin.withdrawPlayer(sender, newcost);
							if(er.transactionSuccess()) {
								final String[] mailboxPaid = MailboxListener.language.getString("mailbox-paid").split("%");
								sender.sendMessage(ChatColor.DARK_GREEN + "[" + MailboxListener.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + mailboxPaid[0] + "" + ChatColor.GREEN + newcost + "" + ChatColor.YELLOW + "" + mailboxPaid[1]);
								sendBox(sender, recipient, contentsarray);
							} else {
								sender.sendMessage(ChatColor.DARK_GREEN + "[" + MailboxListener.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + MailboxListener.language.getString("transaction-error"));
								for(final ItemStack item : contentsarray) {
									sender.getInventory().addItem(item);
								}
							}
						} else {
							final String[] transactionNoMoney = MailboxListener.language.getString("transaction-no-money").split("%");
							sender.sendMessage(ChatColor.DARK_GREEN + "[" + MailboxListener.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + transactionNoMoney[0] + "" + ChatColor.GREEN + newcost + "" + ChatColor.YELLOW + "" + transactionNoMoney[1]);
							for(final ItemStack item : contentsarray) {
								sender.getInventory().addItem(item);
							}
						}
					} else {
						sendBox(sender, recipient, contentsarray);
					}
				} else {
					final String[] recipientBusy = MailboxListener.language.getString("recipient-busy").split("%");
					sender.sendMessage(ChatColor.DARK_GREEN + "[" + MailboxListener.configuration.getString("plugin-tag") + "] " + ChatColor.AQUA + "" + recipient.getName() + "" + ChatColor.YELLOW + "" + recipientBusy[1]);
					for(final ItemStack item : contentsarray) {
						sender.getInventory().addItem(item);
					}
				}
			} else {
				sender.sendMessage(ChatColor.DARK_GREEN + "[" + MailboxListener.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + MailboxListener.language.getString("mailbox-aborted"));
			}
		}
	}
	private static final void sendBox(final CommandSender sender, final Player recipient, final LinkedList<ItemStack> contentsarray) {
		final String[] mailboxSent = MailboxListener.language.getString("mailbox-sent").split("%");
		final String[] mailboxFrom = MailboxListener.language.getString("mailbox-from").split("%");
		final String[] mailboxOpening = MailboxListener.language.getString("mailbox-opening-seconds").split("%");
		sender.sendMessage(ChatColor.DARK_GREEN + "[" + MailboxListener.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + mailboxSent[0] + "" + ChatColor.AQUA + "" + recipient.getName() + "" + ChatColor.YELLOW + "" + mailboxSent[1]);
		MailboxListener.playerBusy.put(recipient.getUniqueId(), true);
		if(MailboxListener.configuration.getBoolean("use-title") == true) {
			recipient.sendTitle(ChatColor.AQUA + "" + mailboxFrom[0] +  "" + ChatColor.GREEN + "" + sender.getName(), ChatColor.AQUA + "" + mailboxOpening[0] + "" + ChatColor.GREEN + "" + MailboxListener.configuration.getInt("mail-time") + "" + ChatColor.AQUA + "" + mailboxOpening[1] + " " + ChatColor.DARK_RED + "" + MailboxListener.language.getString("mailbox-lose"), 20 * 1, 20 * MailboxListener.configuration.getInt("mail-time"), 20 * 1);
		} else {
			recipient.sendMessage(ChatColor.DARK_GREEN + "[" + MailboxListener.configuration.getString("plugin-tag") + "] " + ChatColor.AQUA + "" + mailboxFrom[0] + "" + ChatColor.GREEN + "" + sender.getName() + "" + ChatColor.AQUA + "" + mailboxFrom[1]);
			recipient.sendMessage(ChatColor.DARK_GREEN + "[" + MailboxListener.configuration.getString("plugin-tag") + "] " + ChatColor.AQUA + "" + mailboxOpening[0] + "" + ChatColor.GREEN + "" + MailboxListener.configuration.getInt("mail-time") + "" + ChatColor.AQUA + "" + mailboxOpening[1] + " " + ChatColor.DARK_RED + "" + MailboxListener.language.getString("mailbox-lose"));
		}
		new BukkitRunnable() {
			@Override
			public final void run() {
				recipient.openInventory(MailboxInventory.getMailboxInventory(MailboxType.IN, null, contentsarray));
				sender.sendMessage(ChatColor.DARK_GREEN + "[" + MailboxListener.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + MailboxListener.language.getString("mailbox-delivered"));
			}
		}.runTaskLater(MailboxListener.plugin, 20 * MailboxListener.configuration.getInt("mail-time") + 2);
	}
}