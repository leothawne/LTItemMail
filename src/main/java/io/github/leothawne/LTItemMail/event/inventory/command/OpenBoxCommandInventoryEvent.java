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
package io.github.leothawne.LTItemMail.event.inventory.command;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.inventory.command.OpenBoxCommandInventory;

public class OpenBoxCommandInventoryEvent implements Listener {
	private static FileConfiguration configuration;
	private static FileConfiguration language;
	private static HashMap<UUID, Boolean> playerBusy;
	public OpenBoxCommandInventoryEvent(FileConfiguration configuration, FileConfiguration language, HashMap<UUID, Boolean> playerBusy) {
		OpenBoxCommandInventoryEvent.configuration = configuration;
		OpenBoxCommandInventoryEvent.language = language;
		OpenBoxCommandInventoryEvent.playerBusy = playerBusy;
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public static final void onInventoryClose(InventoryCloseEvent event) {
		Inventory inventory = event.getInventory();
		if(inventory.getName().equals(OpenBoxCommandInventory.getName())) {
			Player player = (Player) event.getPlayer();
			playerBusy.put(player.getUniqueId(), false);
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