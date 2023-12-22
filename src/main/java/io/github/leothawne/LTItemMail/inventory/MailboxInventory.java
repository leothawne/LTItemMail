package io.github.leothawne.LTItemMail.inventory;

import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.type.MailboxType;

public final class MailboxInventory {
	public static final String getMailboxName(final MailboxType type, final Integer mailboxID, final Player playerFrom) {
		if(type.equals(MailboxType.IN)) {
			if(mailboxID != null) {
				return LTItemMail.getInstance().getConfiguration().getString("mailbox-name") + "#" + String.valueOf(mailboxID);
			} else return LTItemMail.getInstance().getConfiguration().getString("mailbox-name") + "#";
		} else if(type.equals(MailboxType.OUT)) {
			if(playerFrom != null) {
				return LTItemMail.getInstance().getConfiguration().getString("mailbox-name") + "@" + playerFrom.getName();
			} else return LTItemMail.getInstance().getConfiguration().getString("mailbox-name") + "@";
		}
		return null;
	}
	public static final Inventory getMailboxInventory(final MailboxType type, final Integer mailboxID, Player player, final LinkedList<ItemStack> contents) {
		if(type.equals(MailboxType.IN)) {
			final Inventory inventory = Bukkit.createInventory(null, 27, getMailboxName(type, mailboxID, player));
			for(int i = 0; i < (contents.size() - 1); i++) inventory.setItem(i, contents.get(i));
			return inventory;
		} else if(type.equals(MailboxType.OUT)) return Bukkit.createInventory(player, 27, getMailboxName(type, mailboxID, player));
		return null;
	}
}