package io.github.leothawne.LTItemMail.module;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.type.VersionType;

public final class ConfigurationModule {
	public static final void check(final LTItemMail plugin, final ConsoleModule console) {
		final File configFile = new File(plugin.getDataFolder(), "config.yml");
		if(configFile.exists() == false) {
			console.warning("Extracting config.yml file...");
			plugin.saveDefaultConfig();
			console.info("Done.");
		} else {
			console.info("Found config.yml file.");
		}
	}
	public static final FileConfiguration load(final LTItemMail plugin, final ConsoleModule console) {
		final File configFile = new File(plugin.getDataFolder(), "config.yml");
		if(configFile.exists()) {
			final FileConfiguration configuration = plugin.getConfig();
			console.info("Loaded config.yml file.");
			if(configuration.getInt("config-version") != Integer.parseInt(DataModule.getVersion(VersionType.CONFIG_YML))) {
				console.severe("config.yml file outdated. Delete that file and restart the server.");
			}
			return configuration;
		}
		console.severe("Missing config.yml file.");
		return null;
	}
}