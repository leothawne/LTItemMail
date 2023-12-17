package io.github.leothawne.LTItemMail.module;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.type.VersionType;

public final class LanguageModule {
	public static final void check() {
		final File languageFile = new File(LTItemMail.getInstance().getDataFolder(), LTItemMail.getInstance().getConfiguration().getString("language") + ".yml");
		if(!languageFile.exists()) {
			LTItemMail.getInstance().getConsole().warning("Extracting " + LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file...");
			if(LTItemMail.getInstance().getConfiguration().getString("language").equalsIgnoreCase("vietnamese") || LTItemMail.getInstance().getConfiguration().getString("language").equalsIgnoreCase("english") || LTItemMail.getInstance().getConfiguration().getString("language").equalsIgnoreCase("portuguese")) {
				LTItemMail.getInstance().saveResource(LTItemMail.getInstance().getConfiguration().getString("language") + ".yml", false);
				LTItemMail.getInstance().getConsole().info("Done.");
			} else LTItemMail.getInstance().getConsole().warning(LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file is not supported yet. I suggest you to manually create the language file and do manually the translation.");
		} else LTItemMail.getInstance().getConsole().info("Found " + LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file.");
	}
	public static final FileConfiguration load() {
		final File languageFile = new File(LTItemMail.getInstance().getDataFolder(), LTItemMail.getInstance().getConfiguration().getString("language") + ".yml");
		if(languageFile.exists()) {
			final FileConfiguration languageConfig = new YamlConfiguration();
			try {
				languageConfig.load(languageFile);
				LTItemMail.getInstance().getConsole().info("Loaded " + LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file.");
				int languageVersion = 0;
				if(LTItemMail.getInstance().getConfiguration().getString("language").equalsIgnoreCase("english")) languageVersion = Integer.parseInt(DataModule.getVersion(VersionType.ENGLISH_YML));
				if(LTItemMail.getInstance().getConfiguration().getString("language").equalsIgnoreCase("portuguese")) languageVersion = Integer.parseInt(DataModule.getVersion(VersionType.PORTUGUESE_YML));
				if(LTItemMail.getInstance().getConfiguration().getString("language").equalsIgnoreCase("vietnamese")) languageVersion = Integer.parseInt(DataModule.getVersion(VersionType.VIETNAMESE_YML));
				if(languageVersion != 0) if(languageConfig.getInt("language-version") != languageVersion) LTItemMail.getInstance().getConsole().severe(LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file outdated. Delete that file and restart the server.");
				return languageConfig;
			} catch(final IOException | InvalidConfigurationException exception) {
				exception.printStackTrace();
			}
			return null;
		}
		LTItemMail.getInstance().getConsole().severe("Missing " + LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file.");
		return null;
	}
}