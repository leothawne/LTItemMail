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

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

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
import io.github.leothawne.LTItemMail.module.MetricsModule;
import io.github.leothawne.LTItemMail.module.RecipeModule;
import io.github.leothawne.LTItemMail.module.integration.IntegrationModule;
import io.github.leothawne.LTItemMail.task.MailboxItemTask;
import io.github.leothawne.LTItemMail.task.VersionTask;

public final class LTItemMail extends JavaPlugin {
	private static LTItemMail instance;
	private final void registerEvents(final Listener...listeners) {
		for(final Listener listener : listeners) Bukkit.getServer().getPluginManager().registerEvents(listener, this);
	}
	private FileConfiguration configuration;
	private FileConfiguration language;
	private Connection connection;
	@Override
	public final void onEnable() {
		instance = this;
		ConsoleModule.Hello();
		ConsoleModule.info("Enabling...");
		loadConfig();
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_ENABLE)) {
			VersionTask.run();
			MetricsModule.register();
			loadLang();
			loadIntegrations(false);
			DatabaseModule.check();
			connection = DatabaseModule.load();
			final Integer dbVer = DatabaseModule.checkDbVer();
			if(dbVer < Integer.valueOf(DataModule.getVersion(DataModule.VersionType.DATABASE))) {
				for(Integer i = dbVer; i < Integer.valueOf(DataModule.getVersion(DataModule.VersionType.DATABASE)); i++) {
					ConsoleModule.warning("Updating database... (" + i + " -> " + (i + 1) + ")");
					if(DatabaseModule.updateDb(i)) {
						ConsoleModule.warning("Database updated! (" + i + " -> " + (i + 1) + ")");
					} else ConsoleModule.severe("Database update failed! (" + i + " -> " + (i + 1) + ")");
				}
			} else ConsoleModule.info("Database is up to date! (" + dbVer + ")");
			RecipeModule.register();
			MailboxItemTask.run();
			registerEvents(new VirtualMailboxListener(),
					new PlayerListener(),
					new ItemMailboxListener());
			getCommand("itemmail").setExecutor(new ItemMailCommand());
			getCommand("itemmail").setTabCompleter(new ItemMailCommandTabCompleter());
			getCommand("itemmailadmin").setExecutor(new ItemMailAdminCommand());
			getCommand("itemmailadmin").setTabCompleter(new ItemMailAdminCommandTabCompleter());
			getCommand("mailitem").setExecutor(new MailItemCommand());
			getCommand("mailitem").setTabCompleter(new MailItemCommandTabCompleter());
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_UPDATE_CHECK)) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "itemmailadmin update");
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
	public static final LTItemMail getInstance() {
		return instance;
	}
	public final FileConfiguration getConfiguration() {
		return configuration;
	}
	public final FileConfiguration getLanguage() {
		return language;
	}
	public final Connection getConnection() {
		return connection;
	}
	public final void reload() {
		loadConfig();
		loadLang();
		loadIntegrations(true);
	}
	private final void loadConfig() {
		ConfigurationModule.check();
		configuration = ConfigurationModule.load();
	}
	private final void loadLang() {
		LanguageModule.check();
		language = LanguageModule.load();
	}
	private final void loadIntegrations(boolean restart) {
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_VAULT)) {
			ConsoleModule.info("Loading Vault...");
			if(IntegrationModule.getInstance(restart).isInstalled(IntegrationModule.TPlugin.VAULT)) {
				ConsoleModule.info("Vault loaded.");
				new BukkitRunnable() {
					@Override
					public final void run() {
						Boolean waiting = false;
						if(IntegrationModule.getInstance(false).register(IntegrationModule.FPlugin.VAULT_ECONOMY)) {
							ConsoleModule.info("Economy plugin registered.");
							IntegrationModule.getInstance(false).warn(IntegrationModule.TPlugin.VAULT);
							this.cancel();
						} else if(!waiting) {
							ConsoleModule.warning("Economy plugin not found. Waiting for Vault registration.");
							waiting = true;
						}
					}
				}.runTaskTimer(this, 0, 20 * 5);
			} else ConsoleModule.warning("Vault is not installed. Skipping.");
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_GRIEFPREVENTION)) {
			ConsoleModule.info("Loading GriefPrevention...");
			if(IntegrationModule.getInstance(false).isInstalled(IntegrationModule.TPlugin.GRIEF_PREVENTION)) {
				ConsoleModule.info("GriefPrevention loaded.");
				IntegrationModule.getInstance(false).register(IntegrationModule.FPlugin.GRIEF_PREVENTION_API);
				IntegrationModule.getInstance(false).warn(IntegrationModule.TPlugin.GRIEF_PREVENTION);
			} else ConsoleModule.warning("GriefPrevention is not installed. Skipping.");
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_REDPROTECT)) {
			ConsoleModule.info("Loading RedProtect...");
			if(IntegrationModule.getInstance(false).isInstalled(IntegrationModule.TPlugin.RED_PROTECT)) {
				ConsoleModule.info("RedProtect loaded.");
				IntegrationModule.getInstance(false).register(IntegrationModule.FPlugin.RED_PROTECT_API);
				IntegrationModule.getInstance(false).warn(IntegrationModule.TPlugin.RED_PROTECT);
			} else ConsoleModule.warning("RedProtect is not installed. Skipping.");
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_TOWNYADVANCED)) {
			ConsoleModule.info("Loading Towny...");
			if(IntegrationModule.getInstance(false).isInstalled(IntegrationModule.TPlugin.TOWNY_ADVANCED)) {
				ConsoleModule.info("Towny loaded.");
				IntegrationModule.getInstance(false).register(IntegrationModule.FPlugin.TOWNY_ADVANCED_API);
				IntegrationModule.getInstance(false).warn(IntegrationModule.TPlugin.TOWNY_ADVANCED);
			} else ConsoleModule.warning("Towny is not installed. Skipping.");
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_WORLDGUARD)) {
			ConsoleModule.info("Loading WorldGuard...");
			if(IntegrationModule.getInstance(false).isInstalled(IntegrationModule.TPlugin.WORLD_GUARD)) {
				ConsoleModule.info("WorldGuard loaded.");
				IntegrationModule.getInstance(false).register(IntegrationModule.FPlugin.WORLD_GUARD_API);
				IntegrationModule.getInstance(false).warn(IntegrationModule.TPlugin.WORLD_GUARD);
			} else ConsoleModule.warning("WorldGuard is not installed. Skipping.");
		}
	}
}