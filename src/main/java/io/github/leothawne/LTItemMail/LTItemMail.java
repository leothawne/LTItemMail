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
package io.github.leothawne.LTItemMail;

import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.leothawne.LTItemMail.api.LTItemMailAPI;
import io.github.leothawne.LTItemMail.api.bStats.MetricsAPI;
import io.github.leothawne.LTItemMail.api.utility.WarnIntegrationsAPI;
import io.github.leothawne.LTItemMail.command.ItemMailAdminCommand;
import io.github.leothawne.LTItemMail.command.ItemMailCommand;
import io.github.leothawne.LTItemMail.command.SendBoxCommand;
import io.github.leothawne.LTItemMail.command.tabCompleter.ItemMailAdminCommandTabCompleter;
import io.github.leothawne.LTItemMail.command.tabCompleter.ItemMailCommandTabCompleter;
import io.github.leothawne.LTItemMail.command.tabCompleter.SendBoxCommandTabCompleter;
import io.github.leothawne.LTItemMail.event.PlayerEvent;
import io.github.leothawne.LTItemMail.event.inventory.command.OpenBoxCommandInventoryEvent;
import io.github.leothawne.LTItemMail.event.inventory.command.SendBoxCommandInventoryEvent;
import net.milkbowl.vault.economy.Economy;

/**
 * Main class.
 * 
 * @author leothawne
 *
 */
public class LTItemMail extends JavaPlugin {
	private final ConsoleLoader myLogger = new ConsoleLoader(this);
	private final void registerEvents(Listener...listeners) {
		for(Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, this);
		}
	}
	private static FileConfiguration configuration;
	private static FileConfiguration language;
	private static HashMap<UUID, Boolean> playerBusy = new HashMap<UUID, Boolean>();
	private static MetricsAPI metrics;
	/**
	 * 
	 * @deprecated Not for public use.
	 * 
	 */
	@Override
	public final void onEnable() {
		myLogger.Hello();
		myLogger.info("Loading...");
		for(Player player : getServer().getOnlinePlayers()) {
			playerBusy.put(player.getUniqueId(), false);
		}
		ConfigurationLoader.check(this, myLogger);
		configuration = ConfigurationLoader.load(this, myLogger);
		if(configuration.getBoolean("enable-plugin") == true) {
			MetricsLoader.init(this, myLogger, metrics);
			Economy economyPlugin = null;
			if(configuration.getBoolean("use-vault") == true) {
				myLogger.info("Loading Vault...");
				if(VaultLoader.isVaultInstalled(this)) {
					myLogger.info("Vault loaded.");
					myLogger.info("Looking for an Economy plugin...");
					economyPlugin = VaultLoader.getEconomy(this);
					if(economyPlugin != null) {
						myLogger.info("Economy plugin found.");
						new WarnIntegrationsAPI(this, Arrays.asList("Vault"));
					} else {
						myLogger.info("Economy plugin is missing. Skipping...");
					}
				} else {
					myLogger.info("Vault is not installed. Skipping...");
				}
			}
			LanguageLoader.check(this, myLogger, configuration);
			language = LanguageLoader.load(this, myLogger, configuration);
			getCommand("itemmail").setExecutor(new ItemMailCommand(myLogger, configuration, language));
			getCommand("itemmail").setTabCompleter(new ItemMailCommandTabCompleter());
			getCommand("itemmailadmin").setExecutor(new ItemMailAdminCommand(this, myLogger, configuration, language));
			getCommand("itemmailadmin").setTabCompleter(new ItemMailAdminCommandTabCompleter());
			getCommand("sendbox").setExecutor(new SendBoxCommand(this, myLogger, configuration, language));
			getCommand("sendbox").setTabCompleter(new SendBoxCommandTabCompleter());
			registerEvents(new SendBoxCommandInventoryEvent(this, configuration, language, playerBusy, economyPlugin), new OpenBoxCommandInventoryEvent(configuration, language, playerBusy), new PlayerEvent(configuration, playerBusy));
			Version.check(this, myLogger);
		} else {
			myLogger.severe("You choose to disable this plugin.");
			getServer().getPluginManager().disablePlugin(this);
		}
	}
	/**
	 * 
	 * @deprecated Not for public use.
	 * 
	 */
	@Override
	public final void onDisable() {
		myLogger.info("Unloading...");
	}
	/**
	 * 
	 * Method used to cast the API class.
	 * 
	 * @return The API class.
	 * 
	 */
	@SuppressWarnings("deprecation")
	public final LTItemMailAPI getAPI() {
		return new LTItemMailAPI(this, configuration, language, playerBusy, metrics);
	}
}