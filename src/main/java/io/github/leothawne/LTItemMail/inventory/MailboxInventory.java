package io.github.leothawne.LTItemMail.inventory;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.type.MailboxType;

public final class MailboxInventory {
	public static final String getMailboxName(final MailboxType type, final Integer mailboxID) {
		if(mailboxID != null) {
			if(type.equals(MailboxType.IN)) if(mailboxID == 0) {
				return "Mailbox#";
			} else return "Mailbox#" + String.valueOf(mailboxID);
		} else {
			if(type.equals(MailboxType.IN)) return "> Mailbox <";
			if(type.equals(MailboxType.OUT)) return "< Mailbox >";
		}
		return null;
	}
	public static final Inventory getMailboxInventory(final MailboxType type, final Integer mailboxID, Player player, final LinkedList<ItemStack> contents) {
		if(type.equals(MailboxType.IN)) {
			final Inventory inventory = Bukkit.createInventory(null, 27, getMailboxName(type, mailboxID));
			for(int i = 0; i < (contents.size() - 1); i++) inventory.setItem(i, contents.get(i));
			return inventory;
		} else if(type.equals(MailboxType.OUT)) {
			final Inventory inventory = Bukkit.createInventory(player, 27, getMailboxName(type, mailboxID));
			return inventory;
		}
		return null;
	}
}