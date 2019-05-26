package io.github.leothawne.LTItemMail.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.inventory.MailboxInventory;
import io.github.leothawne.LTItemMail.type.MailboxType;

public final class MailboxAPI {
	public static final void sendSpecial(final LTItemMail plugin, final FileConfiguration configuration, final FileConfiguration language, final HashMap<UUID, Boolean> playerBusy, final Player receiver, final LinkedList<ItemStack> items) {
		final String mailboxFrom = language.getString("special-mailbox");
		final String[] mailboxOpening = language.getString("mailbox-opening-seconds").split("%");
		playerBusy.put(receiver.getUniqueId(), true);
		if(configuration.getBoolean("use-title") == true) {
			receiver.sendTitle(ChatColor.LIGHT_PURPLE + "" + mailboxFrom, ChatColor.AQUA + "" + mailboxOpening[0] + "" + ChatColor.GREEN + "" + configuration.getInt("mail-time") + "" + ChatColor.AQUA + "" + mailboxOpening[1] + " " + ChatColor.DARK_RED + "" + language.getString("mailbox-lose"), 20 * 1, 20 * configuration.getInt("mail-time"), 20 * 1);
		} else {
			receiver.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.AQUA + "" + mailboxFrom);
			receiver.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.AQUA + "" + mailboxOpening[0] + "" + ChatColor.GREEN + "" + configuration.getInt("mail-time") + "" + ChatColor.AQUA + "" + mailboxOpening[1] + " " + ChatColor.DARK_RED + "" + language.getString("mailbox-lose"));
		}
		new BukkitRunnable() {
			@Override
			public final void run() {
				receiver.openInventory(MailboxInventory.getMailboxInventory(MailboxType.IN, null, items));
			}
		}.runTaskLater(plugin, 20 * configuration.getInt("mail-time") + 2);
	}
}