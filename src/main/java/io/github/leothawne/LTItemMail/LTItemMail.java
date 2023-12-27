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

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.LTItemMail.api.LTItemMailAPI;
import io.github.leothawne.LTItemMail.api.WarnIntegrationsAPI;
import io.github.leothawne.LTItemMail.command.ItemMailAdminCommand;
import io.github.leothawne.LTItemMail.command.ItemMailCommand;
import io.github.leothawne.LTItemMail.command.MailItemCommand;
import io.github.leothawne.LTItemMail.command.tabCompleter.ItemMailAdminCommandTabCompleter;
import io.github.leothawne.LTItemMail.command.tabCompleter.ItemMailCommandTabCompleter;
import io.github.leothawne.LTItemMail.command.tabCompleter.MailItemCommandTabCompleter;
import io.github.leothawne.LTItemMail.listener.ItemMailboxListener;
import io.github.leothawne.LTItemMail.listener.PlayerListener;
import io.github.leothawne.LTItemMail.listener.VirtualMailboxListener;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.ConsoleModule;
import io.github.leothawne.LTItemMail.module.DataModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.MailboxLogModule;
import io.github.leothawne.LTItemMail.module.MetricsModule;
import io.github.leothawne.LTItemMail.module.RecipeModule;
import io.github.leothawne.LTItemMail.module.VaultModule;
import io.github.leothawne.LTItemMail.task.MailboxItemTask;
import io.github.leothawne.LTItemMail.task.VersionTask;
import net.milkbowl.vault.economy.Economy;

public final class LTItemMail extends JavaPlugin {
	private static LTItemMail instance;
	private final void registerEvents(final Listener...listeners) {
		for(final Listener listener : listeners) Bukkit.getServer().getPluginManager().registerEvents(listener, this);
	}
	private FileConfiguration configuration;
	private FileConfiguration language;
	private Connection con;
	private Economy economyPlugin;
	private Integer versionTaskID;
	@Override
	public final void onEnable() {
		instance = this;
		ConsoleModule.Hello();
		ConsoleModule.info("Enabling...");
		ConfigurationModule.check();
		configuration = ConfigurationModule.load();
		if(configuration == null) {
			new BukkitRunnable() {
				@Override
				public final void run() {
					Bukkit.getPluginManager().disablePlugin(instance);
				}
			}.runTaskTimer(this, 0, 0);
			return;
		}
		if(configuration.getBoolean("enable-plugin")) {
			versionTaskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new VersionTask(), 0, 20 * 10);
			MetricsModule.init();
			LanguageModule.check();
			language = LanguageModule.load();
			economyPlugin = null;
			if(configuration.getBoolean("use-vault")) {
				ConsoleModule.info("Loading Vault...");
				if(VaultModule.isVaultInstalled()) {
					ConsoleModule.info("Vault loaded.");
					new WarnIntegrationsAPI(Arrays.asList("Vault"));
					ConsoleModule.info("Looking for an Economy plugin...");
					economyPlugin = VaultModule.getEconomy();
					if(economyPlugin != null) {
						ConsoleModule.info("Economy plugin found.");
					} else {
						ConsoleModule.warning("Economy plugin not found. Waiting for Vault registration.");
						new BukkitRunnable() {
							@Override
							public final void run() {
								economyPlugin = VaultModule.getEconomy();
								if(economyPlugin != null) {
									ConsoleModule.info("Economy plugin installed.");
									this.cancel();
								}
							}
						}.runTaskTimer(this, 0, 20 * 5);
					}
				} else ConsoleModule.warning("Vault is not installed. Skipping.");
			}
			DatabaseModule.check();
			con = DatabaseModule.load();
			final Integer dbVer = DatabaseModule.checkDbVer();
			if(dbVer < Integer.valueOf(DataModule.getVersion(DataModule.VersionType.DATABASE))) {
				for(Integer i = dbVer; i < Integer.valueOf(DataModule.getVersion(DataModule.VersionType.DATABASE)); i++) {
					ConsoleModule.warning("Updating database... (" + i + " -> " + (i + 1) + ")");
					if(DatabaseModule.updateDb(i)) {
						ConsoleModule.warning("Database updated! (" + i + " -> " + (i + 1) + ")");
					} else ConsoleModule.severe("Database update failed! (" + i + " -> " + (i + 1) + ")");
				}
			} else ConsoleModule.info("Database is up to date! (" + dbVer + ")");
			MailboxLogModule.init();
			RecipeModule.register();
			Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new MailboxItemTask(), 0, 0);
			registerEvents(new VirtualMailboxListener(),
					new PlayerListener(),
					new ItemMailboxListener());
			getCommand("itemmail").setExecutor(new ItemMailCommand());
			getCommand("itemmail").setTabCompleter(new ItemMailCommandTabCompleter());
			getCommand("itemmailadmin").setExecutor(new ItemMailAdminCommand());
			getCommand("itemmailadmin").setTabCompleter(new ItemMailAdminCommandTabCompleter());
			getCommand("mailitem").setExecutor(new MailItemCommand());
			getCommand("mailitem").setTabCompleter(new MailItemCommandTabCompleter());
			Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "itemmailadmin update");
		} else {
			ConsoleModule.severe("You've choosen to disable me.");
			new BukkitRunnable() {
				@Override
				public final void run() {
					Bukkit.getPluginManager().disablePlugin(instance);
				}
			}.runTaskTimer(this, 0, 20);
		}
	}
	@Override
	public final void onDisable() {
		ConsoleModule.info("Disabling...");
		Bukkit.getScheduler().cancelTasks(this);
	}
	/**
	 * 
	 * Method used to instantiate LT Item Mail API class.
	 * 
	 * @return The API class.
	 * 
	 */
	public static final LTItemMailAPI getAPI() {
		return LTItemMailAPI.getInstance();
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
	public final Economy getEconomy() {
		return economyPlugin;
	}
	public final Connection getConnection() {
		return con;
	}
	public final Integer getVersionTask() {
		return versionTaskID;
	}
}