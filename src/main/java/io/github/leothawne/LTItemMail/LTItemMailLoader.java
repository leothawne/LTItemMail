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
	private ConsoleLoader myLogger = new ConsoleLoader(this);
	private Economy economyPlugin = null;
	public static void registerEvents(LTItemMailLoader plugin, Listener...listeners) {
		for(Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, plugin);
		}
	}
	private FileConfiguration configuration;
	private FileConfiguration language;
	@Override
	public void onEnable() {
		for(Player player : this.getServer().getOnlinePlayers()) {
			player.sendMessage(ChatColor.AQUA + "[LTItemMail] " + ChatColor.LIGHT_PURPLE + "Loading...");
		}
		myLogger.Hello();
		myLogger.info("Loading mailboxes...");
		myLogger.info("Loading Vault...");
		VaultLoader vault = new VaultLoader(this);
		if(vault.isInstalled()) {
			if(vault.isReady()) {
				economyPlugin = vault.getEconomy();
				new ConfigurationLoader(this, myLogger).check();
				configuration = new ConfigurationLoader(this, myLogger).load();
				new LanguageLoader(this, myLogger, configuration).check();
				language = new LanguageLoader(this, myLogger, configuration).load();
				if(configuration.getBoolean("enable-plugin") == true) {
					getCommand("itemmail").setExecutor(new ItemMailCommands(this, myLogger));
					getCommand("itemmail").setTabCompleter(new ItemMailConstructTabCompleter());
					getCommand("itemmailadmin").setExecutor(new ItemMailAdminCommands(this, myLogger));
					getCommand("itemmailadmin").setTabCompleter(new ItemMailAdminConstructTabCompleter());
					getCommand("sendbox").setExecutor(new SendBoxCommand(this, myLogger));
					getCommand("sendbox").setTabCompleter(new SendBoxConstructTabCompleter());
					registerEvents(this, new SendBoxInventoryEvent(this, configuration, economyPlugin));
					registerEvents(this, new OpenBoxInventoryEvent(language));
					registerEvents(this, new Listeners(configuration));
					new Version(this, myLogger).check();
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
	public void onDisable() {
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