/*
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

import java.sql.Connection;
import java.util.Arrays;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitScheduler;

import io.github.leothawne.LTItemMail.api.LTItemMailAPI;
import io.github.leothawne.LTItemMail.api.MetricsAPI;
import io.github.leothawne.LTItemMail.api.WarnIntegrationsAPI;
import io.github.leothawne.LTItemMail.command.ItemMailAdminCommand;
import io.github.leothawne.LTItemMail.command.ItemMailCommand;
import io.github.leothawne.LTItemMail.command.MailItemCommand;
import io.github.leothawne.LTItemMail.command.tabCompleter.ItemMailAdminCommandTabCompleter;
import io.github.leothawne.LTItemMail.command.tabCompleter.ItemMailCommandTabCompleter;
import io.github.leothawne.LTItemMail.command.tabCompleter.MailItemCommandTabCompleter;
import io.github.leothawne.LTItemMail.listener.MailboxListener;
import io.github.leothawne.LTItemMail.listener.PlayerListener;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.ConsoleModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.MailboxLogModule;
import io.github.leothawne.LTItemMail.module.MetricsModule;
import io.github.leothawne.LTItemMail.module.VaultModule;
import io.github.leothawne.LTItemMail.task.VersionTask;
import net.milkbowl.vault.economy.Economy;

/**
 * Main class.
 * 
 * @author leothawne
 *
 */
public final class LTItemMail extends JavaPlugin {
	private static LTItemMail instance;
	private final void registerEvents(final Listener...listeners) {
		for(final Listener listener : listeners) Bukkit.getServer().getPluginManager().registerEvents(listener, this);
	}
	private final ConsoleModule console = new ConsoleModule();
	private FileConfiguration configuration;
	private FileConfiguration language;
	private Connection con;
	private MetricsAPI metrics;
	private BukkitScheduler scheduler;
	private Economy economyPlugin;
	/**
	 * 
	 * @deprecated Not for public use.
	 * 
	 */
	@Override
	public final void onEnable() {
		instance = this;
		this.console.Hello();
		this.console.info("Loading...");
		ConfigurationModule.check();
		configuration = ConfigurationModule.load();
		LanguageModule.check();
		language = LanguageModule.load();
		if(configuration == null || language == null) {
			Bukkit.getServer().shutdown();
			return;
		}
		if(configuration.getBoolean("enable-plugin")) {
			metrics = MetricsModule.init();
			economyPlugin = null;
			if(configuration.getBoolean("use-vault")) {
				console.info("Loading Vault...");
				if(VaultModule.isVaultInstalled()) {
					console.info("Vault loaded.");
					console.info("Looking for an Economy plugin...");
					economyPlugin = VaultModule.getEconomy();
					if(economyPlugin != null) {
						console.info("Economy plugin found.");
					} else console.info("Economy plugin is missing. Skipping...");
				} else console.info("Vault is not installed. Skipping...");
			}
			DatabaseModule.check();
			con = DatabaseModule.load();
			MailboxLogModule.init();
			getCommand("itemmail").setExecutor(new ItemMailCommand());
			getCommand("itemmail").setTabCompleter(new ItemMailCommandTabCompleter());
			getCommand("itemmailadmin").setExecutor(new ItemMailAdminCommand());
			getCommand("itemmailadmin").setTabCompleter(new ItemMailAdminCommandTabCompleter());
			getCommand("mailitem").setExecutor(new MailItemCommand());
			getCommand("mailitem").setTabCompleter(new MailItemCommandTabCompleter());
			scheduler = Bukkit.getScheduler();
			scheduler.scheduleSyncRepeatingTask(this, new VersionTask(), 0, 20 * 60 * 60);
			registerEvents(new MailboxListener(), new PlayerListener());
			new WarnIntegrationsAPI(new LinkedList<String>(Arrays.asList("Vault", "Essentials")));
		} else {
			this.console.severe("You've choosen to disable me.");
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}
	/**
	 * 
	 * @deprecated Not for public use.
	 * 
	 */
	@Override
	public final void onDisable() {
		console.info("Unloading...");
		scheduler.cancelTasks(this);
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
		return new LTItemMailAPI();
	}
	public static final LTItemMail getInstance() {
		return instance;
	}
	public final FileConfiguration getConfiguration() {
		return configuration;
	}
	public final FileConfiguration getLanguage() {
		return language;
	}
	public final ConsoleModule getConsole() {
		return console;
	}
	public final MetricsAPI getMetrics() {
		return metrics;
	}
	public final Economy getEconomy() {
		return economyPlugin;
	}
	public final Connection getConnection() {
		return con;
	}
}