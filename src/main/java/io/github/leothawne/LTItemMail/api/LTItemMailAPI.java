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
import java.util.List;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.api.bStats.MetricsAPI;
import io.github.leothawne.LTItemMail.api.utility.SendMailboxAPI;

/**
 * 
 * The API class.
 * 
 * @author leothawne
 * 
 */
public class LTItemMailAPI {
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
	public LTItemMailAPI(LTItemMail plugin, FileConfiguration configuration, FileConfiguration language, HashMap<UUID, Boolean> playerBusy, MetricsAPI metrics) {
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
		return configuration.getBoolean("use-vault");
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
	public final boolean isPlayerBusy(Player player) {
		return (boolean) playerBusy.get(player.getUniqueId()).booleanValue();
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
	public final boolean isPlayerBusy(UUID playerUUID) {
		return this.isPlayerBusy(plugin.getServer().getPlayer(playerUUID));
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
	 * @deprecated Replaced by {@link #isPlayerBusy(Player)} or {@link #isPlayerBusy(UUID)}.
	 * 
	 */
	public final boolean isPlayerBusy(String playerName) {
		return this.isPlayerBusy(plugin.getServer().getPlayer(playerName));
	}
	/**
	 * 
	 * Returns a FileConfiguration type value that can be used
	 * to determine the current language used by the plugin.
	 * 
	 * @return A FileConfiguration type value.
	 * 
	 */
	public final FileConfiguration getLanguageFile(){
		return language;
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
		return metrics.isEnabled();
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
	public final void sendSpecialMailbox(Player player, List<ItemStack> items) {
		SendMailboxAPI.run(plugin, configuration, language, playerBusy, player, items);
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
	public final void sendSpecialMailbox(UUID playerUUID, List<ItemStack> items) {
		this.sendSpecialMailbox(plugin.getServer().getPlayer(playerUUID), items);
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
	 * @deprecated Replaced by {@link #sendSpecialMailbox(Player, List)} or {@link #sendSpecialMailbox(UUID, List)}.
	 * 
	 */
	@Deprecated
	public final void sendSpecialMailbox(String playerName, List<ItemStack> items) {
		this.sendSpecialMailbox(plugin.getServer().getPlayer(playerName), items);
	}
}