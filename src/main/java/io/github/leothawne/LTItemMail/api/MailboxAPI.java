package io.github.leothawne.LTItemMail.api;

import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;

public final class MailboxAPI {
	private MailboxAPI() {}
	public static final String sendSpecial(final OfflinePlayer receiver, final LinkedList<ItemStack> items) {
		if(receiver.hasPlayedBefore()) {
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_TITLE)) {
				if(receiver.getPlayer() != null) receiver.getPlayer().sendTitle(ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_SPECIAL), "", 20 * 1, 20 * 5, 20 * 1);
			} else if(receiver.getPlayer() != null) receiver.getPlayer().sendMessage(ChatColor.DARK_GREEN + "[" + (String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + "] " + ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_SPECIAL));
			DatabaseModule.Virtual.saveMailbox(null, receiver.getUniqueId(), items);
			return "success";
		}
		return LanguageModule.Type.PLAYER_NEVERPLAYEDERROR.toString();
	}
}