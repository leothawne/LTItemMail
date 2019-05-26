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
import org.bukkit.scheduler.BukkitScheduler;

import io.github.leothawne.LTItemMail.api.LTItemMailAPI;
import io.github.leothawne.LTItemMail.api.bStats.MetricsAPI;
import io.github.leothawne.LTItemMail.api.utility.WarnIntegrationsAPI;
import io.github.leothawne.LTItemMail.command.ItemMailAdminCommand;
import io.github.leothawne.LTItemMail.command.ItemMailCommand;
import io.github.leothawne.LTItemMail.command.MailItemCommand;
import io.github.leothawne.LTItemMail.command.tabCompleter.ItemMailAdminCommandTabCompleter;
import io.github.leothawne.LTItemMail.command.tabCompleter.ItemMailCommandTabCompleter;
import io.github.leothawne.LTItemMail.command.tabCompleter.SendBoxCommandTabCompleter;
import io.github.leothawne.LTItemMail.listener.OpenBoxCommandInventoryEvent;
import io.github.leothawne.LTItemMail.listener.PlayerListener;
import io.github.leothawne.LTItemMail.listener.SendBoxCommandInventoryEvent;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.ConsoleModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.MetricsModule;
import io.github.leothawne.LTItemMail.module.VaultModule;
import io.github.leothawne.LTItemMail.module.DataModule;
import io.github.leothawne.LTItemMail.task.VersionTask;
import net.milkbowl.vault.economy.Economy;

/**
 * Main class.
 * 
 * @author leothawne
 *
 */
public final class LTItemMail extends JavaPlugin {
	private final ConsoleModule console = new ConsoleModule(this);
	private static final void registerEvents(final Listener...listeners) {
		for(final Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, new LTItemMail());
		}
	}
	private static FileConfiguration configuration;
	private static FileConfiguration language;
	private static HashMap<UUID, Boolean> playerBusy = new HashMap<UUID, Boolean>();
	private static MetricsAPI metrics;
	private static BukkitScheduler scheduler;
	private static int versionTask = 0;
	/**
	 * 
	 * @deprecated Not for public use.
	 * 
	 */
	@Override
	public final void onEnable() {
		this.console.Hello();
		this.console.info("Loading...");
		for(final Player player : getServer().getOnlinePlayers()) {
			LTItemMail.playerBusy.put(player.getUniqueId(), false);
		}
		ConfigurationModule.check(this, this.console);
		LTItemMail.configuration = ConfigurationModule.load(this, this.console);
		if(LTItemMail.configuration.getBoolean("enable-plugin") == true) {
			MetricsModule.init(this, this.console, LTItemMail.metrics);
			Economy economyPlugin = null;
			if(LTItemMail.configuration.getBoolean("use-vault") == true) {
				this.console.info("Loading Vault...");
				if(VaultModule.isVaultInstalled(this)) {
					this.console.info("Vault loaded.");
					this.console.info("Looking for an Economy plugin...");
					economyPlugin = VaultModule.getEconomy(this);
					if(economyPlugin != null) {
						this.console.info("Economy plugin found.");
						new WarnIntegrationsAPI(this, Arrays.asList("Vault"));
					} else {
						this.console.info("Economy plugin is missing. Skipping...");
					}
				} else {
					this.console.info("Vault is not installed. Skipping...");
				}
			}
			LanguageModule.check(this, this.console, LTItemMail.configuration);
			LTItemMail.language = LanguageModule.load(this, this.console, LTItemMail.configuration);
			this.getCommand("itemmail").setExecutor(new ItemMailCommand(this.console, LTItemMail.configuration, LTItemMail.language));
			this.getCommand("itemmail").setTabCompleter(new ItemMailCommandTabCompleter());
			this.getCommand("itemmailadmin").setExecutor(new ItemMailAdminCommand(this, this.console, LTItemMail.configuration, LTItemMail.language));
			this.getCommand("itemmailadmin").setTabCompleter(new ItemMailAdminCommandTabCompleter());
			this.getCommand("sendbox").setExecutor(new MailItemCommand(this, this.console, LTItemMail.configuration, LTItemMail.language));
			this.getCommand("sendbox").setTabCompleter(new SendBoxCommandTabCompleter());
			LTItemMail.scheduler = this.getServer().getScheduler();
			LTItemMail.versionTask = scheduler.scheduleAsyncRepeatingTask(this, new VersionTask(this, this.console, DataModule.getVersionNumber(), DataModule.getPluginURL()), 0, 20 * 60 * 60);
			registerEvents(new SendBoxCommandInventoryEvent(this, LTItemMail.configuration, LTItemMail.language, LTItemMail.playerBusy, economyPlugin), new OpenBoxCommandInventoryEvent(LTItemMail.configuration, LTItemMail.language, LTItemMail.playerBusy), new PlayerListener(LTItemMail.configuration, LTItemMail.playerBusy));
		} else {
			this.console.severe("You've choosen to disable me.");
			this.getServer().getPluginManager().disablePlugin(this);
		}
	}
	/**
	 * 
	 * @deprecated Not for public use.
	 * 
	 */
	@Override
	public final void onDisable() {
		this.console.info("Unloading...");
		if(LTItemMail.scheduler.isCurrentlyRunning(LTItemMail.versionTask) || scheduler.isQueued(LTItemMail.versionTask)) {
			LTItemMail.scheduler.cancelTask(LTItemMail.versionTask);
		}
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
		return new LTItemMailAPI(this, LTItemMail.configuration, LTItemMail.language, LTItemMail.playerBusy, LTItemMail.metrics);
	}
}