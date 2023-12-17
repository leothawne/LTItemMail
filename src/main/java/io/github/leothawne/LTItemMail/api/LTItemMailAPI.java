package io.github.leothawne.LTItemMail.api;

import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.LTItemMail;

/**
 * 
 * The API class.
 * 
 * @author leothawne
 * 
 */
public final class LTItemMailAPI {
	/**
	 * 
	 * @deprecated There is no need to manually create
	 * an object with this constructor when
	 * you can easily use {@link LTItemMail#getAPI()}.
	 * 
	 */
	public LTItemMailAPI() {}
	/**
	 * 
	 * Returns a boolean type value that can be used to determine
	 * if the plugin is currently using currently using Vault.
	 * 
	 * @return A boolean type value.
	 * 
	 */
	public final boolean isUsingVault() {
		return LTItemMail.getInstance().getConfiguration().getBoolean("use-vault");
	}
	/**
	 * 
	 * Returns a boolean type value that can be used to determine
	 * if a player is marked as busy. While opening a mailbox,
	 * the receiver is marked as busy to not receive other
	 * mailboxes until it closes the currently opened one.
	 * 
	 * @param player The Player type variable.
	 * 
	 * @return A boolean type value.
	 * 
	 */
	public final boolean isPlayerBusy(final Player player) {
		return (boolean) LTItemMail.getInstance().getPlayerBusy().get(player.getUniqueId()).booleanValue();
	}
	/**
	 * 
	 * Returns a boolean type value that can be used to determine
	 * if a player is marked as busy. While opening a mailbox,
	 * the receiver is marked as busy to not receive other
	 * mailboxes until it closes the currently opened one.
	 * 
	 * @param playerUUID The player's unique id.
	 * 
	 * @return A boolean type value.
	 * 
	 */
	public final boolean isPlayerBusy(final UUID playerUUID) {
		return isPlayerBusy(Bukkit.getPlayer(playerUUID));
	}
	/**
	 * 
	 * Returns a boolean type value that can be used to determine
	 * if a player is marked as busy. While opening a mailbox,
	 * the receiver is marked as busy to not receive other
	 * mailboxes until it closes the currently opened one.
	 * 
	 * @param playerName The player's name.
	 * 
	 * @return A boolean type value.
	 * 
	 */
	public final boolean isPlayerBusy(final String playerName) {
		return isPlayerBusy(Bukkit.getPlayer(playerName));
	}
	/**
	 * 
	 * Returns a FileConfiguration type value that can be used
	 * to determine the current language used by the plugin.
	 * 
	 * @return A FileConfiguration type value.
	 * 
	 */
	public final FileConfiguration getLanguageMap(){
		return LTItemMail.getInstance().getLanguage();
	}
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