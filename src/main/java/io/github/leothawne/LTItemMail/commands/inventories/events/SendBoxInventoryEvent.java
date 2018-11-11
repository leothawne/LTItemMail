package io.github.leothawne.LTItemMail.commands.inventories.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.Material;
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
	private LTItemMailLoader plugin;
	private Economy economyPlugin;
	public SendBoxInventoryEvent(LTItemMailLoader plugin, Economy economyPlugin){
		this.plugin = plugin;
		this.economyPlugin = economyPlugin;
	}
	private String inventoryName = new SendBoxInventory().getName();
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
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
			for(ItemStack content : contentsarray) {
				if(content.getType() != Material.AIR) {
					isEmpty = false;
				}
			}
			inventory.clear();
			if(isEmpty == false) {
				if(recipientslots >= slots) {
					if(recipient.isInvulnerable() == false) {
						if(economyPlugin.has(sender, 10.0)) {
							EconomyResponse er = economyPlugin.withdrawPlayer(sender, 10.0);
							if(er.transactionSuccess()) {
								sender.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.YELLOW + "You paid " + ChatColor.GREEN + "$10.0 " + ChatColor.YELLOW + "to the bank! " + recipientslots);
								sender.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.YELLOW + "Items sent to " + ChatColor.AQUA + "" + recipient.getName() + "" + ChatColor.YELLOW + ". " + slots);
								recipient.setInvulnerable(true);
								recipient.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.AQUA + "Mailbox from: " + ChatColor.GREEN + "" + sender.getName() + "" + ChatColor.AQUA + ".");
								recipient.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.AQUA + "Opening in " + ChatColor.GREEN + "5 seconds" + ChatColor.AQUA + "... " + ChatColor.DARK_RED + "Items left inside will be lost!");
								recipient.sendTitle(ChatColor.AQUA + "Mailbox from: " + ChatColor.GREEN + "" + sender.getName(), ChatColor.AQUA + "Opening in " + ChatColor.GREEN + "5 seconds" + ChatColor.AQUA + "... " + ChatColor.DARK_RED + "Items left inside will be lost!", 20 * 1, 20 * 5, 20 * 1);
								plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
									public void run() {
										recipient.openInventory(new OpenBoxInventory().GUI(contentsarray));
										sender.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.YELLOW + "Items delivered successfully!");
									}
								}, 20 * 7);
							} else {
								sender.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.YELLOW + "Transaction not succeeded!");
								for(ItemStack item : contentsarray) {
									sender.getInventory().addItem(item);
								}
							}
						} else {
							sender.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.YELLOW + "You don't have " + ChatColor.GREEN + "$10.0 " + ChatColor.YELLOW + "to pay.");
							for(ItemStack item : contentsarray) {
								sender.getInventory().addItem(item);
							}
						}
					} else {
						sender.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.AQUA + "" + recipient.getName() + "" + ChatColor.YELLOW + " is busy right now! Try again later.");
						for(ItemStack item : contentsarray) {
							sender.getInventory().addItem(item);
						}
					}
				} else {
					sender.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.AQUA + "" + recipient.getName() + "" + ChatColor.YELLOW + " doesn't have " + slots + " slots available.");
					for(ItemStack item : contentsarray) {
						sender.getInventory().addItem(item);
					}
				}
			} else {
				sender.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.YELLOW + "Box empty! Aborted.");
			}
		}
	}
}