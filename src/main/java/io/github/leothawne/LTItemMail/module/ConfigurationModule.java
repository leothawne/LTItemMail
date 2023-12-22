package io.github.leothawne.LTItemMail.module;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.type.VersionType;

public final class ConfigurationModule {
	public static final void check() {
		final File configFile = new File(LTItemMail.getInstance().getDataFolder(), "config.yml");
		if(!configFile.exists()) {
			LTItemMail.getInstance().getConsole().warning("Extracting config.yml file...");
			LTItemMail.getInstance().saveDefaultConfig();
			LTItemMail.getInstance().getConsole().info("Done.");
		} else LTItemMail.getInstance().getConsole().info("Found config.yml file.");
	}
	public static final FileConfiguration load() {
		final File configFile = new File(LTItemMail.getInstance().getDataFolder(), "config.yml");
		if(configFile.exists()) {
			final FileConfiguration configuration = LTItemMail.getInstance().getConfig();
			LTItemMail.getInstance().getConsole().info("Loaded config.yml file.");
			if(configuration.getInt("config-version") != Integer.parseInt(DataModule.getVersion(VersionType.CONFIG_YML))) {
				LTItemMail.getInstance().getConsole().severe("config.yml file outdated. Please restart the server.");
				configFile.delete();
				return null;
			}
			return configuration;
		}
		LTItemMail.getInstance().getConsole().severe("Missing config.yml file.");
		return null;
	}
}