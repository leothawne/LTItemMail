package io.github.leothawne.LTItemMail;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationLoader {
	private LTItemMailLoader plugin;
	private ConsoleLoader myLogger;
	public ConfigurationLoader(LTItemMailLoader plugin, ConsoleLoader myLogger) {
		this.plugin = plugin;
		this.myLogger = myLogger;
	}
	private File configFile;
	public void check() {
		myLogger.info("Looking for config file...");
		configFile = new File(plugin.getDataFolder(), "config.yml");
		if(configFile.exists() == false) {
			myLogger.warning("Config file not found. Creating a new one...");
			plugin.saveDefaultConfig();
			myLogger.info("New config file created.");
		} else {
			myLogger.info("Config file found.");
		}
	}
	public FileConfiguration load() {
		myLogger.info("Loading config file...");
		configFile = new File(plugin.getDataFolder(), "config.yml");
		if(configFile.exists()) {
			FileConfiguration configuration = plugin.getConfig();
			myLogger.info("Config file loaded.");
			if(configuration.getInt("config-version") != new Version(plugin, myLogger).configFileVersion) {
				myLogger.severe("The config.yml file is outdated! You must manually delete the config.yml file and reload the plugin.");
			}
			return configuration;
		}
		myLogger.severe("A config file was not found to be loaded.");
		myLogger.severe("Running without config file. You will face several errors from this point.");
		return null;
	}
}