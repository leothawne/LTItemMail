package io.github.leothawne.LTItemMail.api.utility;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.inventory.command.OpenBoxCommandInventory;

public class SendMailboxAPI {
	public static final void run(LTItemMail plugin, FileConfiguration configuration, FileConfiguration language, HashMap<UUID, Boolean> playerBusy, Player receiver, List<ItemStack> items) {
		String mailboxFrom = language.getString("special-mailbox");
		String[] mailboxOpening = language.getString("mailbox-opening-seconds").split("%");
		playerBusy.put(receiver.getUniqueId(), true);
		if(configuration.getBoolean("use-title") == true) {
			receiver.sendTitle(ChatColor.LIGHT_PURPLE + "" + mailboxFrom, ChatColor.AQUA + "" + mailboxOpening[0] + "" + ChatColor.GREEN + "" + configuration.getInt("mail-time") + "" + ChatColor.AQUA + "" + mailboxOpening[1] + " " + ChatColor.DARK_RED + "" + language.getString("mailbox-lose"), 20 * 1, 20 * configuration.getInt("mail-time"), 20 * 1);
		} else {
			receiver.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.AQUA + "" + mailboxFrom);
			receiver.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.AQUA + "" + mailboxOpening[0] + "" + ChatColor.GREEN + "" + configuration.getInt("mail-time") + "" + ChatColor.AQUA + "" + mailboxOpening[1] + " " + ChatColor.DARK_RED + "" + language.getString("mailbox-lose"));
		}
		plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, new Runnable() {
			public final void run() {
				new OpenBoxCommandInventory();
				receiver.openInventory(OpenBoxCommandInventory.GUI(items));
			}
		}, 20 * configuration.getInt("mail-time") + 2);
	}
}