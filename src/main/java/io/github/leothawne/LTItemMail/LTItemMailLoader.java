package io.github.leothawne.LTItemMail;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

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

public class LTItemMailLoader extends JavaPlugin {
	private final ConsoleLoader myLogger = new ConsoleLoader(this);
	private static Economy economyPlugin = null;
	public final void registerEvents(Listener...listeners) {
		for(Listener listener : listeners) {
			Bukkit.getServer().getPluginManager().registerEvents(listener, this);
		}
	}
	private static FileConfiguration configuration;
	private static FileConfiguration language;
	@Override
	public final void onEnable() {
		myLogger.Hello();
		myLogger.info("Loading...");
		new ConfigurationLoader(this, myLogger);
		ConfigurationLoader.check();
		new ConfigurationLoader(this, myLogger);
		configuration = ConfigurationLoader.load();
		if(configuration.getBoolean("enable-plugin") == true) {
			new MetricsLoader(this, myLogger);
			MetricsLoader.init();
			if(configuration.getBoolean("use-vault") == true) {
				myLogger.info("Loading Vault...");
				new VaultLoader(this);
				if(VaultLoader.isInstalled()) {
					myLogger.info("Vault loaded.");
					myLogger.info("Looking for an Economy plugin...");
					new VaultLoader(this);
					VaultLoader.ready();
					economyPlugin = VaultLoader.getEconomy();
					if(economyPlugin != null) {
						myLogger.info("Economy plugin found.");
					} else {
						myLogger.info("Economy plugin is missing. Skipping...");
					}
				} else {
					myLogger.info("Vault is not installed. Skipping...");
				}
			}
			new LanguageLoader(this, myLogger, configuration);
			LanguageLoader.check();
			new LanguageLoader(this, myLogger, configuration);
			language = LanguageLoader.load();
			getCommand("itemmail").setExecutor(new ItemMailCommand(this, myLogger, configuration, language));
			getCommand("itemmail").setTabCompleter(new ItemMailCommandTabCompleter());
			getCommand("itemmailadmin").setExecutor(new ItemMailAdminCommand(myLogger, configuration, language));
			getCommand("itemmailadmin").setTabCompleter(new ItemMailAdminCommandTabCompleter());
			getCommand("sendbox").setExecutor(new SendBoxCommand(this, myLogger, configuration, language));
			getCommand("sendbox").setTabCompleter(new SendBoxCommandTabCompleter());
			registerEvents(new SendBoxCommandInventoryEvent(this, configuration, language, economyPlugin), new OpenBoxCommandInventoryEvent(configuration, language), new PlayerEvent(configuration));
			new Version(this, myLogger);
			Version.check();
			new WarnIntegrationsAPI(this, Arrays.asList("Vault"));
		} else {
			myLogger.severe("You choose to disable this plugin.");
			getServer().getPluginManager().disablePlugin(this);
		}
	}
	@Override
	public final void onDisable() {
		myLogger.info("Unloading...");
	}
}