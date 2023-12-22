package io.github.leothawne.LTItemMail.api;

import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

// TODO: Auto-generated Javadoc
/**
 * 
 * The API class.
 * 
 * @author leothawne
 * 
 */
public final class LTItemMailAPI {
	
	/**
	 * Instantiates a new LT Item Mail API.
	 */
	public LTItemMailAPI() {}
	/**
	 * 
	 * Method used to send items anonymously
	 * to any player on the server. The mailbox
	 * will be assigned as a "Special Mailbox".
	 * 
	 * @param player The Player type variable.
	 * @param items The list of items that the player will receive.
	 * 
	 */
	public final void sendSpecialMailbox(final Player player, final LinkedList<ItemStack> items) {
		MailboxAPI.sendSpecial(player, items);
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
	 */
	public final void sendSpecialMailbox(final UUID playerUUID, final LinkedList<ItemStack> items) {
		sendSpecialMailbox(Bukkit.getPlayer(playerUUID), items);
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
	 */
	public final void sendSpecialMailbox(final String playerName, final LinkedList<ItemStack> items) {
		sendSpecialMailbox(Bukkit.getPlayer(playerName), items);
	}
}