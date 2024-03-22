package io.github.leothawne.LTItemMail;

import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;

/**
 * 
 * The API class.
 * 
 * @author leothawne
 * 
 */
public final class LTItemMailAPI {
	private LTItemMailAPI() {}
	/**
	 * 
	 * Method used to send items anonymously
	 * to any player on the server. The mailbox
	 * will be assigned as a "Special Mailbox".
	 * 
	 * @param player The OfflinePlayer type variable.
	 * @param items The list of items that the player will receive.
	 * @param label The label you want to put on the mail box (cannot be null).
	 * 
	 * @return "success" if it was successfully delivered. Otherwise it will return an error message.
	 * 
	 */
	public static final String sendSpecialMailbox(final OfflinePlayer player, final LinkedList<ItemStack> items, String label) {
		if(player.hasPlayedBefore()) {
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_TITLE)) {
				if(player.getPlayer() != null) player.getPlayer().sendTitle(ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_SPECIAL), "", 20 * 1, 20 * 5, 20 * 1);
			} else if(player.getPlayer() != null) player.getPlayer().sendMessage(ChatColor.DARK_GREEN + "[" + (String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + "] " + ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_SPECIAL));
			if(label == null) label = "";
			DatabaseModule.Virtual.saveMailbox(null, player.getUniqueId(), items, label);
			return "success";
		}
		return LanguageModule.Type.PLAYER_NEVERPLAYEDERROR.toString();
	}
	/**
	 * 
	 * Method used to send items anonymously
	 * to any player on the server. The mailbox
	 * will be assigned as a "Special Mailbox".
	 * 
	 * @param playerUUID The player's unique id.
	 * @param items The list of items that the player will receive.
	 * @param label The label you want to put on the mail box (cannot be null).
	 * 
	 * @return "success" if it was successfully delivered. Otherwise it will return an error message.
	 * 
	 */
	public static final String sendSpecialMailbox(final UUID playerUUID, final LinkedList<ItemStack> items, final String label) {
		return sendSpecialMailbox(Bukkit.getOfflinePlayer(playerUUID), items, label);
	}
	/**
	 * 
	 * Method used to send items anonymously
	 * to any player on the server. The mailbox
	 * will be assigned as a "Special Mailbox".
	 * 
	 * @param playerName The player's name.
	 * @param items The list of items that the player will receive.
	 * @param label The label you want to put on the mail box (cannot be null).
	 * 
	 * @return "success" if it was successfully delivered. Otherwise it will return an error message.
	 * 
	 */
	@SuppressWarnings("deprecation")
	public static final String sendSpecialMailbox(final String playerName, final LinkedList<ItemStack> items, final String label) {
		return sendSpecialMailbox(Bukkit.getOfflinePlayer(playerName), items, label);
	}
}