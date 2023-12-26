package io.github.leothawne.LTItemMail.module;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import io.github.leothawne.LTItemMail.LTItemMail;

public final class ConfigurationModule {
	private ConfigurationModule() {}
	public static final void check() {
		final File configFile = new File(LTItemMail.getInstance().getDataFolder(), "config.yml");
		if(!configFile.exists()) {
			ConsoleModule.warning("Extracting config.yml file...");
			LTItemMail.getInstance().saveDefaultConfig();
			ConsoleModule.info("Done.");
		} else ConsoleModule.info("Found config.yml file.");
	}
	public static final FileConfiguration load() {
		final File configFile = new File(LTItemMail.getInstance().getDataFolder(), "config.yml");
		if(configFile.exists()) {
			final FileConfiguration configuration = LTItemMail.getInstance().getConfig();
			ConsoleModule.info("Loaded config.yml file.");
			if(configuration.getInt("config-version") != Integer.parseInt(DataModule.getVersion(DataModule.VersionType.CONFIG_YML))) {
				ConsoleModule.severe("config.yml file outdated. Delete the current file and restart the server.");
				return null;
			}
			return configuration;
		}
		ConsoleModule.severe("Missing config.yml file.");
		return null;
	}
}