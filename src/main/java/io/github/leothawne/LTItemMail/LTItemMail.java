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
import io.github.leothawne.LTItemMail.lib.BStats;
import io.github.leothawne.LTItemMail.listener.MailboxBlockListener;
import io.github.leothawne.LTItemMail.listener.MailboxListener;
import io.github.leothawne.LTItemMail.listener.PlayerListener;
import io.github.leothawne.LTItemMail.module.BungeeModule;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.ConsoleModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.IntegrationModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.RecipeModule;
import io.github.leothawne.LTItemMail.task.VersionControlTask;
import io.github.leothawne.LTItemMail.task.MailboxTask;

public final class LTItemMail extends JavaPlugin {
	private static LTItemMail instance;
	private final void registerEvents(final Listener...listeners) {
		for(final Listener listener : listeners) Bukkit.getServer().getPluginManager().registerEvents(listener, this);
	}
	private FileConfiguration configuration;
	private FileConfiguration language;
	public Connection connection = null;
	@Override
	public final void onEnable() {
		instance = this;
		new BStats(this, 3647);
		saveResource("LT Item Mail Resource Pack.zip", true);
		ConsoleModule.hello();
		loadConfig();
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_ENABLE)) {
			loadLang();
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.BUNGEE_MODE)) {
				getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
				getServer().getMessenger().registerIncomingPluginChannel(this, "BungeeCord", new BungeeModule());
			}
			DatabaseModule.connect();
			DatabaseModule.checkForUpdates();
			IntegrationModule.getInstance().load();
			RecipeModule.schedule();
			MailboxTask.run();
			registerEvents(new MailboxListener(),
					new PlayerListener(),
					new MailboxBlockListener());
			getCommand("itemmail").setExecutor(new ItemMailCommand());
			getCommand("itemmail").setTabCompleter(new ItemMailCommandTabCompleter());
			getCommand("itemmailadmin").setExecutor(new ItemMailAdminCommand());
			getCommand("itemmailadmin").setTabCompleter(new ItemMailAdminCommandTabCompleter());
			getCommand("mailitem").setExecutor(new MailItemCommand());
			getCommand("mailitem").setTabCompleter(new MailItemCommandTabCompleter());
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_UPDATE_CHECK)) {
				Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ltitemmail:itemmailadmin update");
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_UPDATE_PERIODIC_NOTIFICATION)) new BukkitRunnable() {
					@Override
					public final void run() {
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ltitemmail:itemmailadmin update");
					}
				}.runTaskTimer(this, 20 * 60 * 60 * 3, 20 * 60 * 60 * 3);
			}
			VersionControlTask.run();
		} else {
			new BukkitRunnable() {
				@Override
				public final void run() {
					ConsoleModule.severe("Plugin disabled in config.yml.");
					Bukkit.getPluginManager().disablePlugin(instance);
				}
			}.runTaskTimerAsynchronously(this, 0, 0);
		}
	}
	@Override
	public final void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
		IntegrationModule.getInstance().unload();
		DatabaseModule.disconnect();
		getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord");
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
	public final void reload() {
		loadConfig();
		loadLang();
	}
	private final void loadConfig() {
		ConfigurationModule.check();
		configuration = ConfigurationModule.load();
	}
	private final void loadLang() {
		LanguageModule.check();
		language = LanguageModule.load();
	}
}