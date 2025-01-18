package io.github.leothawne.LTItemMail;

import java.sql.Connection;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.leothawne.LTItemMail.command.ItemMailAdminCommand;
import io.github.leothawne.LTItemMail.command.ItemMailCommand;
import io.github.leothawne.LTItemMail.command.MailItemCommand;
import io.github.leothawne.LTItemMail.command.tabCompleter.ItemMailAdminCommandTabCompleter;
import io.github.leothawne.LTItemMail.command.tabCompleter.ItemMailCommandTabCompleter;
import io.github.leothawne.LTItemMail.command.tabCompleter.MailItemCommandTabCompleter;
import io.github.leothawne.LTItemMail.listener.MailboxBlockListener;
import io.github.leothawne.LTItemMail.listener.MailboxListener;
import io.github.leothawne.LTItemMail.listener.PlayerListener;
import io.github.leothawne.LTItemMail.module.BungeeModule;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.ConsoleModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.ExtensionModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.ResourcePackModule;
import io.github.leothawne.LTItemMail.task.MailboxBlockTask;
import io.github.leothawne.LTItemMail.task.MailboxTask;
import io.github.leothawne.LTItemMail.task.RecipeTask;
import io.github.leothawne.LTItemMail.task.VersionControlTask;
import io.github.leothawne.LTItemMail.util.BStats;

public final class LTItemMail extends JavaPlugin {
	private static LTItemMail instance;
	private FileConfiguration configuration;
	private FileConfiguration language;
	public Connection connection = null;
	@Override
	public final void onEnable() {
		instance = this;
		new BStats(this, 3647);
		ConsoleModule.hello();
		loadConfig();
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_ENABLE)) {
			loadLang();
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.BUNGEE_MODE)) {
				getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
				getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeModule());
			}
			connection = DatabaseModule.connect();
			DatabaseModule.checkForUpdates();
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.DATABASE_CONVERT)) DatabaseModule.convert();
			ExtensionModule.getInstance().load();
			new PlayerListener();
			new MailboxListener();
			new MailboxBlockListener();
			getCommand("itemmail").setExecutor(new ItemMailCommand());
			getCommand("itemmail").setTabCompleter(new ItemMailCommandTabCompleter());
			getCommand("itemmailadmin").setExecutor(new ItemMailAdminCommand());
			getCommand("itemmailadmin").setTabCompleter(new ItemMailAdminCommandTabCompleter());
			getCommand("mailitem").setExecutor(new MailItemCommand());
			getCommand("mailitem").setTabCompleter(new MailItemCommandTabCompleter());
			Bukkit.getScheduler().runTaskTimer(this, new RecipeTask(), 1, 20 * 60);
			Bukkit.getScheduler().runTaskTimer(this, new MailboxTask(), 1, 1);
			Bukkit.getScheduler().runTaskTimer(this, new MailboxBlockTask(), 1, 20);
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_UPDATE_CHECK)) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ltitemmail:itemmailadmin update");
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_UPDATE_PERIODIC_NOTIFICATION)) Bukkit.getScheduler().runTaskTimerAsynchronously(this, new Runnable() {
					@Override
					public final void run() {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ltitemmail:itemmailadmin update");
					}
				}, 20 * 60 * 60 * 3, 20 * 60 * 60 * 3);
			}
			VersionControlTask.run();
			ResourcePackModule.begin();
		} else {
			Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
				@Override
				public final void run() {
					ConsoleModule.severe("Plugin disabled in config.yml.");
					Bukkit.getPluginManager().disablePlugin(instance);
				}
			}, 1, 1);
		}
	}
	@Override
	public final void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
		ExtensionModule.getInstance().unload();
		DatabaseModule.disconnect();
		getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord");
	}
	public final FileConfiguration getConfiguration() {
		return configuration;
	}
	public final FileConfiguration getLanguage() {
		return language;
	}
	public final void reload() {
		loadConfig();
		loadLang();
		ExtensionModule.reload().load();
	}
	private final void loadConfig() {
		ConfigurationModule.check();
		configuration = ConfigurationModule.load();
		ConfigurationModule.addMissing();
	}
	private final void loadLang() {
		LanguageModule.check();
		language = LanguageModule.load();
		LanguageModule.addMissing();
	}
	public static final LTItemMail getInstance() {
		return instance;
	}
}