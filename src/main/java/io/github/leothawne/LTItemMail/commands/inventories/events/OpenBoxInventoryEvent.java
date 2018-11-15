package io.github.leothawne.LTItemMail.commands.inventories.events;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.commands.inventories.OpenBoxInventory;

public class OpenBoxInventoryEvent implements Listener {
	private FileConfiguration configuration;
	private FileConfiguration language;
	public OpenBoxInventoryEvent(FileConfiguration configuration, FileConfiguration language) {
		this.configuration = configuration;
		this.language = language;
	}
	private String inventoryName = new OpenBoxInventory().getName();
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Inventory inventory = event.getInventory();
		if(inventory.getName().equals(inventoryName)) {
			Player player = (Player) event.getPlayer();
			player.setInvulnerable(false);
			ItemStack[] contents = inventory.getContents();
			boolean isEmpty = true;
			for(ItemStack content : contents) {
				if(content != null) {
					isEmpty = false;
				}
			}
			if(isEmpty == false) {
				List<ItemStack> itemslost = new ArrayList<ItemStack>();
				int count = 0;
				for(ItemStack content: contents) {
					if(content != null) {
						itemslost.add(content);
						count = count + content.getAmount();
					}
				}
				String[] mailboxClosedItems = language.getString("mailbox-closed-items").split("%");
				player.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + language.getString("mailbox-closed") + " " + mailboxClosedItems[0] + "" + ChatColor.GREEN + "" + count + "" + ChatColor.YELLOW + "" + mailboxClosedItems[1] + " " + ChatColor.GREEN + "" + itemslost);
			} else {
				player.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + language.getString("mailbox-closed"));
			}
			inventory.clear();
		}
	}
}