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

import io.github.leothawne.LTItemMail.commands.inventories.OpenBoxInventory;

public class OpenBoxInventoryEvent implements Listener {
	private String inventoryName = new OpenBoxInventory().getName();
	@EventHandler
	public void onInventoryClose(InventoryCloseEvent event) {
		Inventory inventory = event.getInventory();
		if(inventory.getName().equals(inventoryName)) {
			Player player = (Player) event.getPlayer();
			player.setInvulnerable(false);
			ItemStack[] contents = inventory.getContents();
			List<ItemStack> contentsarray = new ArrayList<ItemStack>();
			for(ItemStack content : contents) {
				if(content == null) {
					contentsarray.add(new ItemStack(Material.AIR));
				} else {
					contentsarray.add(content);
				}
			}
			boolean isEmpty = true;
			for(ItemStack content : contentsarray) {
				if(content.getType() != Material.AIR) {
					isEmpty = false;
				}
			}
			if(isEmpty == false) {
				List<ItemStack> itemslost = new ArrayList<ItemStack>();
				int count = 0;
				for(ItemStack content: contentsarray) {
					if(content.getType() != Material.AIR) {
						itemslost.add(content);
						count = count + content.getAmount();
					}
				}
				player.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.YELLOW + "Mailbox closed! You lost " + ChatColor.GREEN + "" + count + "" + ChatColor.YELLOW + " items: " + ChatColor.GREEN + "" + itemslost);
			} else {
				player.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.YELLOW + "Mailbox closed!");
			}
			inventory.clear();
		}
	}
}