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
package io.github.leothawne.LTItemMail;

import java.io.File;

import org.bukkit.configuration.file.FileConfiguration;

public class ConfigurationLoader {
	public static final void check(LTItemMail plugin, ConsoleLoader myLogger) {
		myLogger.info("Looking for config file...");
		File configFile = new File(plugin.getDataFolder(), "config.yml");
		if(configFile.exists() == false) {
			myLogger.warning("Config file not found. Creating a new one...");
			plugin.saveDefaultConfig();
			myLogger.info("New config file created.");
		} else {
			myLogger.info("Config file found.");
		}
	}
	public static final FileConfiguration load(LTItemMail plugin, ConsoleLoader myLogger) {
		myLogger.info("Loading config file...");
		File configFile = new File(plugin.getDataFolder(), "config.yml");
		if(configFile.exists()) {
			FileConfiguration configuration = plugin.getConfig();
			myLogger.info("Config file loaded.");
			if(configuration.getInt("config-version") != Version.getConfigVersion()) {
				myLogger.severe("The config.yml file is outdated! You must manually delete the config.yml file and reload the plugin.");
			}
			return configuration;
		}
		myLogger.severe("A config file was not found to be loaded.");
		myLogger.severe("Running without config file. You will face several errors from this point.");
		return null;
	}
}