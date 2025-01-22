package br.net.gmj.nobookie.LTItemMail.entity;

import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import br.net.gmj.nobookie.LTItemMail.item.MailboxItem;
import br.net.gmj.nobookie.LTItemMail.module.DatabaseModule;
import br.net.gmj.nobookie.LTItemMail.module.ExtensionModule;
import br.net.gmj.nobookie.LTItemMail.module.LanguageModule;
import br.net.gmj.nobookie.LTItemMail.module.MailboxModule;
import br.net.gmj.nobookie.LTItemMail.module.ext.LTUltimateAdvancementAPI;
import br.net.gmj.nobookie.LTItemMail.util.FetchUtil;

/**
 * 
 * {@link Bukkit#getOfflinePlayer(String)} requires a case sensitive name. This class avoids that method using the LT Item Mail database first or the username cache of the server.
 * 
 * @author Nobookie
 * 
 */
public final class LTPlayer {
	private final OfflinePlayer player;
	private final String name;
	private final UUID uuid;
	private LTPlayer(final OfflinePlayer player, final String name, final UUID uuid) {
		this.player = player;
		this.name = name;
		this.uuid = uuid;
	}
	private final LTUltimateAdvancementAPI ultimateAdvancementAPI = (LTUltimateAdvancementAPI) ExtensionModule.getInstance().get(ExtensionModule.Function.ULTIMATEADVANCEMENTAPI);
	/**
	 * 
	 * Gets a LTPlayer with the player name (case NOT sensitive).
	 * 
	 * @param name The player name.
	 * 
	 */
	public static final LTPlayer fromName(final String name) {
		final UUID uuid = FetchUtil.Player.fromName(name);
		if(uuid != null) return new LTPlayer(Bukkit.getOfflinePlayer(uuid), FetchUtil.Player.fromUUID(uuid), uuid);
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
		final String name = FetchUtil.Player.fromUUID(uuid);
		if(name != null) return new LTPlayer(Bukkit.getOfflinePlayer(FetchUtil.Player.fromName(name)), name, FetchUtil.Player.fromName(name));
		return null;
	}
	/**
	 * 
	 * Converts Bukkit player into LTPlayer.
	 * 
	 * @deprecated Not recommended. Bukkit requires case sensitive to get offline players with {@link Bukkit#getOfflinePlayer(String)}. Use {@link LTPlayer#fromName(String)} or {@link LTPlayer#fromUUID(UUID)} instead.
	 * 
	 */
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
	 * Gives to the LTPlayer a mailbox block.
	 * 
	 * @return true if the player is online and received the mailbox block. Otherwise, it will return false.
	 * 
	 */
	public final boolean giveMailboxBlock() {
		final Player player = this.player.getPlayer();
		if(player != null && player.getInventory().firstEmpty() != -1) {
			player.getInventory().addItem(new MailboxItem().getItem(null));
			player.playSound(player.getLocation(), Sound.ENTITY_ITEM_PICKUP, SoundCategory.AMBIENT, 1, 1);
			return true;
		}
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
		if(isRegistered()) return DatabaseModule.User.getBanReason(uuid);
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
		if(isRegistered()) return DatabaseModule.User.isBanned(uuid);
		return false;
	}
	/**
	 * 
	 * Gets the count of mails sent from the LTPlayer.
	 * 
	 */
	public final int getMailSentCount() {
		if(isRegistered()) return DatabaseModule.User.getSentCount(uuid);
		return 0;
	}
	/**
	 * 
	 * Gets the count of mails sent to the LTPlayer.
	 * 
	 */
	public final int getMailReceivedCount() {
		if(isRegistered()) return DatabaseModule.User.getReceivedCount(uuid);
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
		return DatabaseModule.User.isRegistered(uuid);
	}
	/**
	 * 
	 * Gets the registry date of the LTPlayer (dd/MM/yyyy).
	 * 
	 */
	public final String getRegistryDate() {
		return DatabaseModule.User.getRegistryDate(uuid);
	}
	/**
	 * 
	 * Sends a toast message to the LTPlayer.
	 * 
	 * @param message The message that will be shown.
	 * 
	 */
	public final void sendToastMessage(final String message) {
		if(ultimateAdvancementAPI != null) ultimateAdvancementAPI.show(this, message);
	}
}