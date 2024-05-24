package io.github.leothawne.LTItemMail;

import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.item.MailboxItem;
import io.github.leothawne.LTItemMail.lib.Fetch;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.MailboxModule;

/**
 * 
 * {@link Bukkit#getOfflinePlayer(String)} requires a case sensitive name. This class avoids that method using the username cache of the server.
 * 
 * @author leothawne
 * 
 */
public final class LTPlayer {
	private OfflinePlayer player;
	private String name;
	private UUID uuid;
	private LTPlayer(final OfflinePlayer player, final String name, final UUID uuid) {
		this.player = player;
		this.name = name;
		this.uuid = uuid;
	}
	/**
	 * 
	 * Gets a LTPlayer with the player name (case NOT sensitive).
	 * 
	 * @param name The player name.
	 * 
	 */
	public static final LTPlayer fromName(final String name) {
		final UUID uuid = Fetch.Player.fromName(name);
		if(uuid != null) return new LTPlayer(Bukkit.getOfflinePlayer(uuid), Fetch.Player.fromUUID(uuid), uuid);
		return null;
	}
	/**
	 * 
	 * Gets a LTPlayer with the player unique id.
	 * 
	 * @param uuid The player UUID.
	 * 
	 */
	public static final LTPlayer fromUUID(final UUID uuid) {
		final String name = Fetch.Player.fromUUID(uuid);
		if(name != null) return new LTPlayer(Bukkit.getOfflinePlayer(Fetch.Player.fromName(name)), name, Fetch.Player.fromName(name));
		return null;
	}
	/**
	 * 
	 * Converts Bukkit player into LTPlayer.
	 * 
	 * @deprecated Not recommended. Bukkit requires case sensitive to get offline players with {@link Bukkit#getOfflinePlayer(String)}. Use {@link LTPlayer#fromName(String)} instead.
	 * 
	 */
	@Deprecated
	public static final LTPlayer fromBukkitPlayer(final OfflinePlayer player) {
		return new LTPlayer(player, player.getName(), player.getUniqueId());
	}
	/**
	 * 
	 * Converts LTPlayer into Bukkit player.
	 * 
	 */
	public final OfflinePlayer getBukkitPlayer() {
		return player;
	}
	/**
	 * 
	 * Forces the LTPlayer to send a mailbox (no charge). It must be online for this to work.
	 * 
	 * @param playerTo The player who will receive (See {@link LTPlayer}).
	 * @param items A list of items that the player will receive.
	 * @param label The label you want to put on the mailbox.
	 * 
	 * @return "success" if it was successfully delivered. Otherwise it will return an error message.
	 * 
	 */
	public final String forceSend(final LTPlayer playerTo, final LinkedList<ItemStack> items, String label) {
		final Player player = this.player.getPlayer();
		if(player == null) return "offline";
		if(playerTo != null) {
			MailboxModule.send(player, playerTo, items, label);
			return "success";
		}
		return LanguageModule.Type.PLAYER_NEVERPLAYEDERROR.toString();
	}
	/**
	 * 
	 * Gives the LTPlayer a mailbox block.
	 * 
	 * @return true if the player is online and received the mailbox block. Otherwise, it will return false.
	 * 
	 */
	public final boolean giveMailboxBlock() {
		final Player player = this.player.getPlayer();
		if(player != null) player.getWorld().dropItemNaturally(player.getLocation(), new MailboxItem().getItem(null));
		return false;
	}
	/**
	 * 
	 * Gets the LTPlayer user name.
	 * 
	 */
	public final String getName() {
		return name;
	}
	/**
	 * 
	 * Gets the LTPlayer unique id.
	 * 
	 */
	public final UUID getUniqueId() {
		return uuid;
	}
	/**
	 * 
	 * Gets the ban reason if the LTPlayer is banned.
	 * 
	 */
	public final String getBanReason() {
		if(isRegistered()) return DatabaseModule.User.banreason(uuid);
		return null;
	}
	/**
	 * 
	 * Checks if the LTPlayer is banned from the post office (cannot send items, receive only).
	 * 
	 * @return true if the player is banned. Otherwise, it will return false.
	 * 
	 */
	public final boolean isBanned() {
		if(isRegistered()) return DatabaseModule.User.banned(uuid);
		return false;
	}
	/**
	 * 
	 * Gets the count of mails sent from the LTPlayer.
	 * 
	 */
	public final int getMailSentCount() {
		if(isRegistered()) return DatabaseModule.User.sent(uuid);
		return 0;
	}
	/**
	 * 
	 * Gets the count of mails sent to the LTPlayer.
	 * 
	 */
	public final int getMailReceivedCount() {
		if(isRegistered()) return DatabaseModule.User.received(uuid);
		return 0;
	}
	/**
	 * 
	 * Checks if the LTPlayer has a registration on the post office.
	 * 
	 * @return true if the player is registered. Otherwise, it will return false.
	 * 
	 */
	public final boolean isRegistered() {
		return DatabaseModule.User.registered(uuid);
	}
	/**
	 * 
	 * Gets the registry date of the LTPlayer (dd/MM/yyyy).
	 * 
	 */
	public final String getRegistryDate() {
		return DatabaseModule.User.date(uuid);
	}
}