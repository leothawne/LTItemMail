package io.github.leothawne.LTItemMail.api;

import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.inventory.ItemStack;

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
	 * Method used to instantiate LT Item Mail API class.
	 * 
	 * @return The API class.
	 * 
	 */
	public static final LTItemMailAPI getInstance() {
		return new LTItemMailAPI();
	}
	/**
	 * 
	 * Method used to send items anonymously
	 * to any player on the server. The mailbox
	 * will be assigned as a "Special Mailbox".
	 * 
	 * @param player The OfflinePlayer type variable.
	 * @param items The list of items that the player will receive.
	 * 
	 * @return "success" if it was successfully delivered. Otherwise it will return an error message.
	 * 
	 */
	public final String sendSpecialMailbox(final OfflinePlayer player, final LinkedList<ItemStack> items) {
		return MailboxAPI.sendSpecial(player, items);
	}
	/**
	 * 
	 * Method used to send items anonymously
	 * to any player on the server. The mailbox
	 * will be assigned as a "Special Mailbox".
	 * 
	 * @param playerUUID The player's unique id.
	 * @param items The list of items that the player will receive.
	 * 
	 * @return "success" if it was successfully delivered. Otherwise it will return an error message.
	 * 
	 */
	public final String sendSpecialMailbox(final UUID playerUUID, final LinkedList<ItemStack> items) {
		return sendSpecialMailbox(Bukkit.getOfflinePlayer(playerUUID), items);
	}
	/**
	 * 
	 * Method used to send items anonymously
	 * to any player on the server. The mailbox
	 * will be assigned as a "Special Mailbox".
	 * 
	 * @param playerName The player's name.
	 * @param items The list of items that the player will receive.
	 * 
	 * @return "success" if it was successfully delivered. Otherwise it will return an error message.
	 * 
	 */
	@SuppressWarnings("deprecation")
	public final String sendSpecialMailbox(final String playerName, final LinkedList<ItemStack> items) {
		return sendSpecialMailbox(Bukkit.getOfflinePlayer(playerName), items);
	}
}