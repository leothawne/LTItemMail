package io.github.leothawne.LTItemMail.api;

import java.util.LinkedList;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.MailboxLogModule;

public final class MailboxAPI {
	public static final void sendSpecial(final Player receiver, final LinkedList<ItemStack> items) {
		if(LTItemMail.getInstance().getConfiguration().getBoolean("use-title")) {
			receiver.sendTitle(ChatColor.AQUA + "" + LanguageModule.get("special-mailbox"), "", 20 * 1, 20 * 5, 20 * 1);
		} else receiver.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.AQUA + "" + LanguageModule.get("special-mailbox"));
		final Integer mailboxID = DatabaseModule.Function.saveMailbox(null, receiver.getUniqueId(), items);
		MailboxLogModule.log(null, null, MailboxLogModule.ActionType.RECEIVED, mailboxID);
	}
}