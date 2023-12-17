package io.github.leothawne.LTItemMail.api;

import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.inventory.MailboxInventory;
import io.github.leothawne.LTItemMail.type.MailboxType;

public final class MailboxAPI {
	public static final void sendSpecial(final Player receiver, final LinkedList<ItemStack> items) {
		final String mailboxFrom = LTItemMail.getInstance().getLanguage().getString("special-mailbox");
		final String[] mailboxOpening = LTItemMail.getInstance().getLanguage().getString("mailbox-opening-seconds").split("%");
		LTItemMail.getInstance().getPlayerBusy().put(receiver.getUniqueId(), true);
		if(LTItemMail.getInstance().getConfiguration().getBoolean("use-title") == true) {
			receiver.sendTitle(ChatColor.LIGHT_PURPLE + "" + mailboxFrom, ChatColor.AQUA + "" + mailboxOpening[0] + "" + ChatColor.GREEN + "" + LTItemMail.getInstance().getConfiguration().getInt("mail-time") + "" + ChatColor.AQUA + "" + mailboxOpening[1] + " " + ChatColor.DARK_RED + "" + LTItemMail.getInstance().getLanguage().getString("mailbox-lose"), 20 * 1, 20 * LTItemMail.getInstance().getConfiguration().getInt("mail-time"), 20 * 1);
		} else {
			receiver.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.AQUA + "" + mailboxFrom);
			receiver.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.AQUA + "" + mailboxOpening[0] + "" + ChatColor.GREEN + "" + LTItemMail.getInstance().getConfiguration().getInt("mail-time") + "" + ChatColor.AQUA + "" + mailboxOpening[1] + " " + ChatColor.DARK_RED + "" + LTItemMail.getInstance().getLanguage().getString("mailbox-lose"));
		}
		new BukkitRunnable() {
			@Override
			public final void run() {
				receiver.openInventory(MailboxInventory.getMailboxInventory(MailboxType.IN, null, items));
			}
		}.runTaskLater(LTItemMail.getInstance(), 20 * LTItemMail.getInstance().getConfiguration().getInt("mail-time") + 2);
	}
}