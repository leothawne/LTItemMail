package io.github.leothawne.LTItemMail.commands.inventories;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class SendBoxInventory {
	public String getName() {
		return "< Mailbox >";
	}
	public Inventory GUI(Player player) {
		Inventory inventory = Bukkit.createInventory(player, 54, getName());
		return inventory;
	}
}