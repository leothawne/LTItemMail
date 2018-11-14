package io.github.leothawne.LTItemMail;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageLoader {
	private LTItemMailLoader plugin;
	private ConsoleLoader myLogger;
	private FileConfiguration configuration;
	public LanguageLoader(LTItemMailLoader plugin, ConsoleLoader myLogger, FileConfiguration configuration) {
		this.plugin = plugin;
		this.myLogger = myLogger;
		this.configuration = configuration;
	}
	private File languageFile;
	public void check() {
		myLogger.info("Looking for language file...");
		languageFile = new File(plugin.getDataFolder(), configuration.getString("language") + ".yml");
		if(languageFile.exists() == false) {
			myLogger.warning("Language file not found. Extracting...");
			if(configuration.getString("language").equalsIgnoreCase("english") || configuration.getString("language").equalsIgnoreCase("portuguese")) {
				plugin.saveResource(configuration.getString("language") + ".yml", true);
				myLogger.warning(configuration.getString("language") + ".yml extracted!");
			} else {
				myLogger.severe(configuration.getString("language") + ".yml is not supported yet. I suggest you to manually create the language file and do manually the translation.");
			}
			
		} else {
			myLogger.info(configuration.getString("language") + ".yml file found.");
		}
	}
	public FileConfiguration load() {
		myLogger.info("Loading language file...");
		languageFile = new File(plugin.getDataFolder(), configuration.getString("language") + ".yml");
		if(languageFile.exists()) {
			FileConfiguration languageConfig = new YamlConfiguration();
			try {
				languageConfig.load(languageFile);
				myLogger.info(configuration.getString("language") + ".yml file loaded.");
				return languageConfig;
			} catch(IOException | InvalidConfigurationException exception) {
				exception.printStackTrace();
			}
			return null;
		}
		myLogger.severe("A config file was not found to be loaded.");
		myLogger.severe("Running without config file. You will face several errors from this point.");
		return null;
	}
}