package io.github.leothawne.LTItemMail;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import io.github.leothawne.LTItemMail.commands.ItemMailAdminCommands;
import io.github.leothawne.LTItemMail.commands.ItemMailCommands;
import io.github.leothawne.LTItemMail.commands.SendBoxCommand;
import io.github.leothawne.LTItemMail.commands.constructor.ItemMailAdminConstructTabCompleter;
import io.github.leothawne.LTItemMail.commands.constructor.ItemMailConstructTabCompleter;
import io.github.leothawne.LTItemMail.commands.constructor.SendBoxConstructTabCompleter;
import io.github.leothawne.LTItemMail.commands.inventories.events.OpenBoxInventoryEvent;
import io.github.leothawne.LTItemMail.commands.inventories.events.SendBoxInventoryEvent;
import io.github.leothawne.LTItemMail.player.Listeners;
import net.milkbowl.vault.economy.Economy;

public class LTItemMailLoader extends JavaPlugin {
	private final ConsoleLoader myLogger = new ConsoleLoader(this);
	private static Economy economyPlugin = null;
	public static final void registerEvents(LTItemMailLoader plugin, Listener...listeners) {
		for(Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
		}
	}
	private static FileConfiguration configuration;
	private static FileConfiguration language;
	@Override
	public final void onEnable() {
		for(Player player : this.getServer().getOnlinePlayers()) {
			player.sendMessage(ChatColor.AQUA + "[LTItemMail] " + ChatColor.LIGHT_PURPLE + "Loading...");
		}
		myLogger.Hello();
		new MetricsLoader(this, myLogger);
		MetricsLoader.init();
		myLogger.info("Loading mailboxes...");
		myLogger.info("Loading Vault...");
		new VaultLoader(this);
		if(VaultLoader.isInstalled()) {
			myLogger.info("Vault loaded!");
			myLogger.info("Looking for Economy plugin...");
			new VaultLoader(this);
			if(VaultLoader.isReady()) {
				myLogger.info("Economy plugin found!");
				economyPlugin = VaultLoader.getEconomy();
				new ConfigurationLoader(this, myLogger);
				ConfigurationLoader.check();
				new ConfigurationLoader(this, myLogger);
				configuration = ConfigurationLoader.load();
				new LanguageLoader(this, myLogger, configuration);
				LanguageLoader.check();
				new LanguageLoader(this, myLogger, configuration);
				language = LanguageLoader.load();
				if(configuration.getBoolean("enable-plugin") == true) {
					getCommand("itemmail").setExecutor(new ItemMailCommands(this, myLogger, configuration, language));
					getCommand("itemmail").setTabCompleter(new ItemMailConstructTabCompleter());
					getCommand("itemmailadmin").setExecutor(new ItemMailAdminCommands(myLogger, configuration, language));
					getCommand("itemmailadmin").setTabCompleter(new ItemMailAdminConstructTabCompleter());
					getCommand("sendbox").setExecutor(new SendBoxCommand(this, myLogger, configuration, language));
					getCommand("sendbox").setTabCompleter(new SendBoxConstructTabCompleter());
					registerEvents(this, new SendBoxInventoryEvent(this, configuration, language, economyPlugin));
					registerEvents(this, new OpenBoxInventoryEvent(configuration, language));
					registerEvents(this, new Listeners(configuration));
					new Version(this, myLogger);
					Version.check();
					myLogger.warning("A permissions plugin is required! Just make sure you are using one. Permissions nodes can be found at: https://leothawne.github.io/LTItemMail/permissions.html");
					for(Player player : this.getServer().getOnlinePlayers()) {
						player.sendMessage(ChatColor.AQUA + "[LTItemMail] " + ChatColor.LIGHT_PURPLE + "Loaded!");
					}
				} else {
					myLogger.severe("You manually choose to disable this plugin.");
					getServer().getPluginManager().disablePlugin(this);
				}
			} else {
				myLogger.severe("An economy plugin is missing! Disabling LT Item Mail...");
				getServer().getPluginManager().disablePlugin(this);
			}
		} else {
			myLogger.severe("Vault is missing! Disabling LT Item Mail...");
			getServer().getPluginManager().disablePlugin(this);
		}
	}
	@Override
	public final void onDisable() {
		for(Player player : this.getServer().getOnlinePlayers()) {
			player.sendMessage(ChatColor.AQUA + "[LTItemMail] " + ChatColor.LIGHT_PURPLE + "Unloading...");
		}
		myLogger.info("Closing mailboxes...");
		myLogger.Goodbye();
		for(Player player : this.getServer().getOnlinePlayers()) {
			player.sendMessage(ChatColor.AQUA + "[LTItemMail] " + ChatColor.LIGHT_PURPLE + "Unloaded!");
		}
	}
}