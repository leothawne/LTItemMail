package io.github.leothawne.LTItemMail.commands.inventories;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class OpenBoxInventory {
	public static final String getName() {
		return "> Mailbox < ";
	}
	public static final Inventory GUI(List<ItemStack> contents) {
		Inventory inventory = Bukkit.createInventory(null, 54, getName());
		for(ItemStack item : contents) {
			inventory.addItem(item);
		}
		return inventory;
	}
}