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
package io.github.leothawne.LTItemMail.task;

import io.github.leothawne.LTItemMail.ConsoleLoader;
import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.api.utility.HTTP;

public class VersionTask implements Runnable {
	private static LTItemMail plugin;
	private static ConsoleLoader myLogger;
	private static String pluginVersion;
	private static String pluginURL;
	public VersionTask(LTItemMail plugin, ConsoleLoader myLogger, String pluginVersion, String pluginURL) {
		VersionTask.plugin = plugin;
		VersionTask.myLogger = myLogger;
		VersionTask.pluginVersion = pluginVersion;
		VersionTask.pluginURL = pluginURL;
	}
	@Override
	public final void run() {
		String response = HTTP.getData(pluginURL);
		if(response != null) {
			if(response.equalsIgnoreCase("disabled")) {
				myLogger.severe("Hey you, stop right there! The version " + pluginVersion + " is not allowed anymore!");
				myLogger.severe("Apologies, but this plugin will now be disabled! Download a newer version to play: https://dev.bukkit.org/projects/lt-item-mail");
				plugin.getServer().getPluginManager().disablePlugin(plugin);
			}
		} else {
			myLogger.warning("Unable to locate: " + pluginURL);
		}
	}
}