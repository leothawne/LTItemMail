package io.github.leothawne.LTItemMail.commands.inventories.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.LTItemMailLoader;
import io.github.leothawne.LTItemMail.commands.inventories.OpenBoxInventory;
import io.github.leothawne.LTItemMail.commands.inventories.SendBoxInventory;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public class SendBoxInventoryEvent implements Listener {
	private static LTItemMailLoader plugin;
	private static FileConfiguration configuration;
	private static FileConfiguration language;
	private static Economy economyPlugin;
	public SendBoxInventoryEvent(LTItemMailLoader plugin, FileConfiguration configuration, FileConfiguration language, Economy economyPlugin){
		SendBoxInventoryEvent.plugin = plugin;
		SendBoxInventoryEvent.configuration = configuration;
		SendBoxInventoryEvent.language = language;
		SendBoxInventoryEvent.economyPlugin = economyPlugin;
	}
	private static final String inventoryName = new SendBoxInventory().getName();
	@EventHandler
	public static final void onInventoryClose(InventoryCloseEvent event) {
		Inventory inventory = event.getInventory();
		if(inventory.getName().equals(inventoryName)) {
			Player sender = (Player) event.getPlayer();
			Player recipient = (Player) inventory.getHolder();
			ItemStack[] contents = inventory.getContents();
			ItemStack[] recipientContents = recipient.getInventory().getContents();
			List<ItemStack> contentsarray = new ArrayList<ItemStack>();
			int countslot = 0;
			for(ItemStack content : contents) {
				if(content == null) {
					contentsarray.add(new ItemStack(Material.AIR));
					countslot++;
				} else {
					contentsarray.add(content);
				}
			}
			int slots = 54 - countslot;
			int countrecipientslot = 0;
			for(ItemStack content : recipientContents) {
				if(content != null) {
					countrecipientslot++;
				}
			}
			int recipientslots = 36 - countrecipientslot;
			boolean isEmpty = true;
			int count = 0;
			for(ItemStack content : contentsarray) {
				if(content.getType() != Material.AIR) {
					isEmpty = false;
					count = count + content.getAmount();
				}
			}
			inventory.clear();
			if(isEmpty == false) {
				if(recipientslots >= slots) {
					if(recipient.isInvulnerable() == false) {
						double newcost = 0;
						if(configuration.getBoolean("cost-per-item") == true) {
							newcost = configuration.getDouble("mail-cost") * count;
						} else {
							newcost = configuration.getDouble("mail-cost");
						}
						if(economyPlugin.has(sender, newcost)) {
							EconomyResponse er = economyPlugin.withdrawPlayer(sender, newcost);
							if(er.transactionSuccess()) {
								String[] mailboxPaid = language.getString("mailbox-paid").split("%");
								String[] mailboxSent = language.getString("mailbox-sent").split("%");
								String[] mailboxFrom = language.getString("mailbox-from").split("%");
								String[] mailboxOpening = language.getString("mailbox-opening-seconds").split("%");
								sender.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + mailboxPaid[0] + "" + ChatColor.GREEN + "$" + newcost + "" + ChatColor.YELLOW + "" + mailboxPaid[1]);
								sender.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + mailboxSent[0] + "" + ChatColor.AQUA + "" + recipient.getName() + "" + ChatColor.YELLOW + "" + mailboxSent[1]);
								recipient.setInvulnerable(true);
								if(configuration.getBoolean("use-title") == true) {
									recipient.sendTitle(ChatColor.AQUA + "" + mailboxFrom[0] +  "" + ChatColor.GREEN + "" + sender.getName(), ChatColor.AQUA + "" + mailboxOpening[0] + "" + ChatColor.GREEN + "" + configuration.getInt("mail-time") + "" + ChatColor.AQUA + "" + mailboxOpening[1] + " " + ChatColor.DARK_RED + "" + language.getString("mailbox-lose"), 20 * 1, 20 * configuration.getInt("mail-time"), 20 * 1);
								} else {
									recipient.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.AQUA + "" + mailboxFrom[0] + "" + ChatColor.GREEN + "" + sender.getName() + "" + ChatColor.AQUA + "" + mailboxFrom[1]);
									recipient.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.AQUA + "" + mailboxOpening[0] + "" + ChatColor.GREEN + "" + configuration.getInt("mail-time") + "" + ChatColor.AQUA + "" + mailboxOpening[1] + " " + ChatColor.DARK_RED + "" + language.getString("mailbox-lose"));
								}
								plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
									public void run() {
										recipient.openInventory(new OpenBoxInventory().GUI(contentsarray));
										sender.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + language.getString("mailbox-delivered"));
									}
								}, 20 * configuration.getInt("mail-time") + 2);
							} else {
								sender.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + language.getString("transaction-error"));
								for(ItemStack item : contentsarray) {
									sender.getInventory().addItem(item);
								}
							}
						} else {
							String[] transactionNoMoney = language.getString("transaction-no-money").split("%");
							sender.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + transactionNoMoney[0] + "" + ChatColor.GREEN + "$" + newcost + "" + ChatColor.YELLOW + "" + transactionNoMoney[1]);
							for(ItemStack item : contentsarray) {
								sender.getInventory().addItem(item);
							}
						}
					} else {
						String[] recipientBusy = language.getString("recipient-busy").split("%");
						sender.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.AQUA + "" + recipient.getName() + "" + ChatColor.YELLOW + "" + recipientBusy[1]);
						for(ItemStack item : contentsarray) {
							sender.getInventory().addItem(item);
						}
					}
				} else {
					String[] recipientFull = language.getString("recipient-full").split("%");
					sender.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.AQUA + "" + recipient.getName() + "" + ChatColor.YELLOW + "" + recipientFull[1] + "" + slots + "" + recipientFull[2]);
					for(ItemStack item : contentsarray) {
						sender.getInventory().addItem(item);
					}
				}
			} else {
				sender.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + language.getString("mailbox-aborted"));
			}
		}
	}
}