package io.github.leothawne.LTItemMail.inventory;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

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
	public static final Inventory getMailboxInventory(final Type type, final Integer mailboxID, OfflinePlayer player, final LinkedList<ItemStack> contents, final String label) {
		if(type.equals(Type.IN)) {
			final Inventory inventory = Bukkit.createInventory(null, 36, getMailboxName(type, mailboxID, null));
			for(int i = 0; i < 27; i++) inventory.setItem(i, contents.get(i));
			addLabel(inventory, label);
			return inventory;
		} else if(type.equals(Type.OUT)) {
			final Inventory inventory = Bukkit.createInventory(null, 36, getMailboxName(type, mailboxID, player));
			addLabel(inventory, label);
			return inventory;
		}
		return null;
	}
	private static final void addLabel(final Inventory inventory, final String label) {
		inventory.setItem(27, new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1));
		inventory.setItem(28, new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1));
		inventory.setItem(29, new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1));
		inventory.setItem(30, new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1));
		final ItemStack book = new ItemStack(Material.BOOK, 1);
		final ItemMeta bookMeta = book.getItemMeta();
		if(label.isEmpty()) {
			bookMeta.setDisplayName(" ");
		} else bookMeta.setDisplayName(label);
		book.setItemMeta(bookMeta);
		inventory.setItem(31, book);
		inventory.setItem(32, new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1));
		inventory.setItem(33, new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1));
		inventory.setItem(34, new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1));
		inventory.setItem(35, new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1));
	}
	public enum Type {
		IN,
		OUT
	}
}