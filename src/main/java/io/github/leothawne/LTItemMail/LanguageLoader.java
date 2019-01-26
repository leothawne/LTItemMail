package io.github.leothawne.LTItemMail;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

public class LanguageLoader {
	private static LTItemMailLoader plugin;
	private static ConsoleLoader myLogger;
	private static FileConfiguration configuration;
	public LanguageLoader(LTItemMailLoader plugin, ConsoleLoader myLogger, FileConfiguration configuration) {
		LanguageLoader.plugin = plugin;
		LanguageLoader.myLogger = myLogger;
		LanguageLoader.configuration = configuration;
	}
	private static File languageFile;
	public static final void check() {
		myLogger.info("Looking for language file...");
		languageFile = new File(plugin.getDataFolder(), configuration.getString("language") + ".yml");
		if(languageFile.exists() == false) {
			myLogger.warning("Language file not found. Extracting...");
			if(configuration.getString("language").equalsIgnoreCase("english") || configuration.getString("language").equalsIgnoreCase("portuguese") || configuration.getString("language").equalsIgnoreCase("vietnamese")) {
				plugin.saveResource(configuration.getString("language") + ".yml", true);
				myLogger.warning(configuration.getString("language") + ".yml extracted!");
			} else {
				myLogger.severe(configuration.getString("language") + ".yml is not supported yet. I suggest you to manually create the language file and do manually the translation.");
			}
			
		} else {
			myLogger.info(configuration.getString("language") + ".yml file found.");
		}
	}
	public static final FileConfiguration load() {
		myLogger.info("Loading language file...");
		languageFile = new File(plugin.getDataFolder(), configuration.getString("language") + ".yml");
		if(languageFile.exists()) {
			FileConfiguration languageConfig = new YamlConfiguration();
			try {
				languageConfig.load(languageFile);
				myLogger.info(configuration.getString("language") + ".yml file loaded.");
				int languageVersion = 0;
				if(configuration.getString("language").equalsIgnoreCase("english") || configuration.getString("language").equalsIgnoreCase("portuguese") || configuration.getString("language").equalsIgnoreCase("vietnamese")) {
					new Version(plugin, myLogger);
					languageVersion = Version.getLanguageVersion(configuration.getString("language"));
				}
				if(languageVersion != 0) {
					if(languageConfig.getInt("language-version") != languageVersion) {
						myLogger.severe("The " + configuration.getString("language") + ".yml file is outdated! You must manually delete the " + configuration.getString("language") + ".yml file and reload the plugin.");
					}
				}
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