/*
 * Copyright (C) 2019 Murilo Amaral Nappi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.leothawne.LTItemMail.api;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.api.bStats.MetricsAPI;

/**
 * 
 * The API class.
 * 
 * @author leothawne
 * 
 */
public final class LTItemMailAPI {
	private static LTItemMail plugin;
	private static FileConfiguration configuration;
	private static FileConfiguration language;
	private static HashMap<UUID, Boolean> playerBusy;
	private static MetricsAPI metrics;
	/**
	 * 
	 * @deprecated There is no need to manually create
	 * an object with this constructor when
	 * you can easily use {@link LTItemMail#getAPI()}.
	 * 
	 */
	public LTItemMailAPI(final LTItemMail plugin, final FileConfiguration configuration, final FileConfiguration language, final HashMap<UUID, Boolean> playerBusy, final MetricsAPI metrics) {
		LTItemMailAPI.plugin = plugin;
		LTItemMailAPI.configuration = configuration;
		LTItemMailAPI.language = language;
		LTItemMailAPI.playerBusy = playerBusy;
		LTItemMailAPI.metrics = metrics;
	}
	/**
	 * 
	 * Returns a boolean type value that can be used to determine
	 * if the plugin is currently using currently using Vault.
	 * 
	 * @return A boolean type value.
	 * 
	 */
	public final boolean isUsingVault() {
		return LTItemMailAPI.configuration.getBoolean("use-vault");
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
		return (boolean) LTItemMailAPI.playerBusy.get(player.getUniqueId()).booleanValue();
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
		return this.isPlayerBusy(LTItemMailAPI.plugin.getServer().getPlayer(playerUUID));
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
		return this.isPlayerBusy(LTItemMailAPI.plugin.getServer().getPlayer(playerName));
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
		return LTItemMailAPI.language;
	}
	/**
	 * 
	 * Returns a boolean type value that can be used to determine
	 * if the plugin is currently using bStats.
	 * 
	 * @return A boolean type value.
	 * 
	 */
	public final boolean isMetricsEnabled() {
		return LTItemMailAPI.metrics.isEnabled();
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
		MailboxAPI.sendSpecial(LTItemMailAPI.plugin, LTItemMailAPI.configuration, LTItemMailAPI.language, LTItemMailAPI.playerBusy, player, items);
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
		this.sendSpecialMailbox(LTItemMailAPI.plugin.getServer().getPlayer(playerUUID), items);
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
		this.sendSpecialMailbox(LTItemMailAPI.plugin.getServer().getPlayer(playerName), items);
	}
}