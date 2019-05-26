/*
 * Copyright (C) 2019 Murilo Amaral Nappi
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.leothawne.LTItemMail.module;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.type.VersionType;

public final class LanguageModule {
	public static final void check(final LTItemMail plugin, final ConsoleModule console, final FileConfiguration configuration) {
		final File languageFile = new File(plugin.getDataFolder(), configuration.getString("language") + ".yml");
		if(!languageFile.exists()) {
			console.warning("Extracting " + configuration.getString("language") + ".yml file...");
			if(configuration.getString("language").equalsIgnoreCase("vietnamese") || configuration.getString("language").equalsIgnoreCase("english") || configuration.getString("language").equalsIgnoreCase("portuguese")) {
				plugin.saveResource(configuration.getString("language") + ".yml", false);
				console.info("Done.");
			} else {
				console.warning(configuration.getString("language") + ".yml file is not supported yet. I suggest you to manually create the language file and do manually the translation.");
			}
		} else {
			console.info("Found " + configuration.getString("language") + ".yml file.");
		}
	}
	public static final FileConfiguration load(final LTItemMail plugin, final ConsoleModule console, final FileConfiguration configuration) {
		final File languageFile = new File(plugin.getDataFolder(), configuration.getString("language") + ".yml");
		if(languageFile.exists()) {
			final FileConfiguration languageConfig = new YamlConfiguration();
			try {
				languageConfig.load(languageFile);
				console.info("Loaded " + configuration.getString("language") + ".yml file.");
				int languageVersion = 0;
				if(configuration.getString("language").equalsIgnoreCase("english")) {
					languageVersion = Integer.parseInt(DataModule.getVersion(VersionType.ENGLISH_YML));
				}
				if(configuration.getString("language").equalsIgnoreCase("portuguese")) {
					languageVersion = Integer.parseInt(DataModule.getVersion(VersionType.PORTUGUESE_YML));
				}
				if(configuration.getString("language").equalsIgnoreCase("vietnamese")) {
					languageVersion = Integer.parseInt(DataModule.getVersion(VersionType.VIETNAMESE_YML));
				}
				if(languageVersion != 0) {
					if(languageConfig.getInt("language-version") != languageVersion) {
						console.severe(configuration.getString("language") + ".yml file outdated. Delete that file and restart the server.");
					}
				}
				return languageConfig;
			} catch(final IOException | InvalidConfigurationException exception) {
				exception.printStackTrace();
			}
			return null;
		}
		console.severe("Missing " + configuration.getString("language") + ".yml file.");
		return null;
	}
}