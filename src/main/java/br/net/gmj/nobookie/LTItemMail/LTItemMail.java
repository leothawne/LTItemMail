package br.net.gmj.nobookie.LTItemMail;

import java.awt.Desktop;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JOptionPane;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import br.net.gmj.nobookie.LTItemMail.command.ItemMailAdminCommand;
import br.net.gmj.nobookie.LTItemMail.command.ItemMailCommand;
import br.net.gmj.nobookie.LTItemMail.command.MailItemCommand;
import br.net.gmj.nobookie.LTItemMail.command.tabCompleter.ItemMailAdminCommandTabCompleter;
import br.net.gmj.nobookie.LTItemMail.command.tabCompleter.ItemMailCommandTabCompleter;
import br.net.gmj.nobookie.LTItemMail.command.tabCompleter.MailItemCommandTabCompleter;
import br.net.gmj.nobookie.LTItemMail.listener.MailboxBlockListener;
import br.net.gmj.nobookie.LTItemMail.listener.MailboxListener;
import br.net.gmj.nobookie.LTItemMail.listener.PlayerListener;
import br.net.gmj.nobookie.LTItemMail.module.BungeeModule;
import br.net.gmj.nobookie.LTItemMail.module.ConfigurationModule;
import br.net.gmj.nobookie.LTItemMail.module.ConsoleModule;
import br.net.gmj.nobookie.LTItemMail.module.DatabaseModule;
import br.net.gmj.nobookie.LTItemMail.module.ExtensionModule;
import br.net.gmj.nobookie.LTItemMail.module.LanguageModule;
import br.net.gmj.nobookie.LTItemMail.module.ModelsModule;
import br.net.gmj.nobookie.LTItemMail.module.ResourcePackModule;
import br.net.gmj.nobookie.LTItemMail.task.MailboxTask;
import br.net.gmj.nobookie.LTItemMail.task.RecipeTask;
import br.net.gmj.nobookie.LTItemMail.task.VersionControlTask;
import br.net.gmj.nobookie.LTItemMail.util.BStats;

/**
 * 
 * Main class of the plugin.
 * 
 * @author Nobookie
 * 
 */
public final class LTItemMail extends JavaPlugin {
	private static LTItemMail instance;
	public FileConfiguration configuration;
	public FileConfiguration language;
	public FileConfiguration models;
	public Connection connection = null;
	public List<Integer> boardsForPlayers = new ArrayList<>();
	public Map<String, List<Integer>> boardsPlayers = new HashMap<>();
	/**
	 * 
	 * Used internally by Bukkit.
	 * 
	 */
	@Override
	public final void onEnable() {
		instance = this;
		final BStats metrics = new BStats(this, 3647);
		loadConfig();
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_ENABLE)) {
			ConsoleModule.hello();
			metrics.addCustomChart(new BStats.SimplePie("builds", () -> {
		        return String.valueOf((Integer) ConfigurationModule.get(ConfigurationModule.Type.BUILD_NUMBER));
		    }));
			loadLang();
			loadModels();
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
	/**
	 * 
	 * Used internally by Bukkit.
	 * 
	 */
	@Override
	public final void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
		ExtensionModule.getInstance().unload();
		DatabaseModule.disconnect();
		getServer().getMessenger().unregisterOutgoingPluginChannel(this, "BungeeCord");
		getServer().getMessenger().unregisterIncomingPluginChannel(this, "BungeeCord");
	}
	/**
	 * 
	 * Reloads the plugin (configuration, language and item models).
	 * 
	 */
	public final void reload() {
		loadConfig();
		loadLang();
		loadModels();
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
	private final void loadModels() {
		ModelsModule.check();
		models = ModelsModule.load();
		ModelsModule.addMissing();
	}
	/**
	 * 
	 * Gets the {@link LTItemMail} instance.
	 * 
	 */
	public static final LTItemMail getInstance() {
		return instance;
	}
	public static final void main(final String[] args) {
		final String error = "Incorrect usage! You must put the jar file into your server /plugins/ folder.";
		System.out.println(error);		if(Desktop.isDesktopSupported()) JOptionPane.showInternalMessageDialog(null, error, LTItemMail.class.getName(), JOptionPane.ERROR_MESSAGE);
		System.exit(0);
	}
}