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
package io.github.leothawne.LTItemMail.inventory;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.type.MailboxType;

public final class MailboxInventory {
	public static final String getMailboxName(final MailboxType type) {
		if(type.equals(MailboxType.IN)) return "> Mailbox <";
		if(type.equals(MailboxType.OUT)) return "< Mailbox >";
		return null;
	}
	public static final Inventory getMailboxInventory(final MailboxType type, Player player, final List<ItemStack> contents) {
		if(type.equals(MailboxType.IN)) {
			final Inventory inventory = Bukkit.createInventory(null, 54, getMailboxName(type));
			for(int i = 0; i < (contents.size() - 1); i++) {
				inventory.setItem(i, contents.get(i));
			}
			return inventory;
		}
		if(type.equals(MailboxType.OUT)) {
			final Inventory inventory = Bukkit.createInventory(player, 54, getMailboxName(type));
			return inventory;
		}
		return null;
	}
}