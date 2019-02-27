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

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.LTItemMail.api.utility.HTTP;

public class Version {
	private static final int configFileVersion = 6;
	private static final int english_languageFileVersion = 2;
	private static final int portuguese_languageFileVersion = 2;
	private static final int vietnamese_languageFileVersion = 2;
	private static final String Plugin_Version = "0.2.3";
	private static final String Plugin_Date = "26/02/2019 (America/Sao_Paulo)";
	private static final String Minecraft_Version = "1.13.x";
	private static final String Minecraft_Build = "1.13-R0.1-SNAPSHOT";
	private static final String Java_Version = "8+";
	private static final String Update_URL = "https://leothawne.github.io/LTItemMail/api/version.html";
	private static final String Plugin_URL = "https://leothawne.github.io/LTItemMail/api/" + Plugin_Version + "/plugin.html";
	public static final int getConfigVersion() {
		return configFileVersion;
	}
	public static final int getLanguageVersion(String language) {
		if(language.equals("portuguese")) {
			return portuguese_languageFileVersion;
		} else if(language.equals("english")) {
			return english_languageFileVersion;
		} else if(language.equals("vietnamese")) {
			return vietnamese_languageFileVersion;
		}
		return 0;
	}
	public static final String getVersionNumber() {
		return Plugin_Version;
	}
	public static final String getVersionDate() {
		return Plugin_Date;
	}
	public static final String getMinecraftVersion() {
		return Minecraft_Version;
	}
	public static final String getMinecraftBuild() {
		return Minecraft_Build;
	}
	public static final String getJavaVersion() {
		return Java_Version;
	}
	public static final String getUpdateURL() {
		return Update_URL;
	}
	public static final void version(CommandSender sender) {
		sender.sendMessage(ChatColor.AQUA + "LT Item Mail " + ChatColor.YELLOW + "plugin " + ChatColor.GREEN + "" + Plugin_Version + "" + ChatColor.YELLOW + " (" + ChatColor.GREEN + "" + Plugin_Date + "" + ChatColor.YELLOW + "), Minecraft " + ChatColor.GREEN + "" + Minecraft_Version +  "" + ChatColor.YELLOW + " (Java " + ChatColor.GREEN + "" + Java_Version + "" + ChatColor.YELLOW + ", build " + ChatColor.GREEN + "" + Minecraft_Build + "" + ChatColor.YELLOW + ").");
	}
	public static final void check(LTItemMail plugin, ConsoleLoader myLogger) {
		new BukkitRunnable() {
			@Override
			public final void run() {
				String response = HTTP.getData(Plugin_URL);
				if(response != null) {
					if(response.equalsIgnoreCase("disabled")) {
						myLogger.severe("Hey you, stop right there! The version " + Plugin_Version + " is not allowed anymore!");
						myLogger.severe("Apologies, but this plugin will now be disabled! Download a newer version to play: https://dev.bukkit.org/projects/lt-item-mail");
						plugin.getServer().getPluginManager().disablePlugin(plugin);
					}
				} else {
					myLogger.warning("Unable to locate: " + Plugin_URL);
				}
			}
		}.runTask(plugin);
		
	}
}
