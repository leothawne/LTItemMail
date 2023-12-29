package io.github.leothawne.LTItemMail.inventory;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.module.ConfigurationModule;

public final class MailboxInventory {
	private MailboxInventory() {}
	public static final String getMailboxName(final Type type, final Integer mailboxID, final OfflinePlayer player) {
		if(type.equals(Type.IN)) {
			if(mailboxID != null) {
				return (String) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_NAME) + "#" + String.valueOf(mailboxID);
			} else return (String) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_NAME) + "#";
		} else if(type.equals(Type.OUT)) {
			if(player != null) {
				return (String) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_NAME) + "@" + player.getName();
			} else return (String) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_NAME) + "@";
		}
		return null;
	}
	public static final Inventory getMailboxInventory(final Type type, final Integer mailboxID, OfflinePlayer player, final LinkedList<ItemStack> contents) {
		if(type.equals(Type.IN)) {
			final Inventory inventory = Bukkit.createInventory(null, 27, getMailboxName(type, mailboxID, null));
			for(int i = 0; i < (contents.size() - 1); i++) inventory.setItem(i, contents.get(i));
			return inventory;
		} else if(type.equals(Type.OUT)) return Bukkit.createInventory(null, 27, getMailboxName(type, mailboxID, player));
		return null;
	}
	public enum Type {
		IN,
		OUT
	}
}