/*
 * Copyright (C) 2019 Murilo Amaral Nappi (murilonappi@gmail.com)
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

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class Version {
	private static LTItemMailLoader plugin;
	private static ConsoleLoader myLogger;
	public Version(LTItemMailLoader plugin, ConsoleLoader myLogger) {
		Version.plugin = plugin;
		Version.myLogger = myLogger;
	}
	private static final int configFileVersion = 6;
	private static final int english_languageFileVersion = 1;
	private static final int portuguese_languageFileVersion = 1;
	private static final int vietnamese_languageFileVersion = 1;
	private static final String Plugin_Version = "0.2.3";
	private static final String Plugin_Date = "99/99/9999 (America/Sao_Paulo)";
	private static final String Minecraft_Version = "1.13.x";
	private static final String Minecraft_Build = "1.13-R0.1-SNAPSHOT";
	private static final String Java_Version = "8+";
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
	public static final void version(CommandSender sender) {
		sender.sendMessage(ChatColor.AQUA + "LT Item Mail " + ChatColor.YELLOW + "plugin " + ChatColor.GREEN + "" + Plugin_Version + "" + ChatColor.YELLOW + " (" + ChatColor.GREEN + "" + Plugin_Date + "" + ChatColor.YELLOW + "), Minecraft " + ChatColor.GREEN + "" + Minecraft_Version +  "" + ChatColor.YELLOW + " (Java " + ChatColor.GREEN + "" + Java_Version + "" + ChatColor.YELLOW + ", build " + ChatColor.GREEN + "" + Minecraft_Build + "" + ChatColor.YELLOW + ").");
	}
	public static final void check() {
		try {
			URLConnection allowedUrl = new URL("https://leothawne.github.io/LTItemMail/api/" + Plugin_Version + "/plugin.html").openConnection();
			allowedUrl.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			allowedUrl.connect();
			BufferedReader allowedReader = new BufferedReader(new InputStreamReader(allowedUrl.getInputStream(), Charset.forName("UTF-8")));
			StringBuilder sb = new StringBuilder();
			String line;
			while((line = allowedReader.readLine()) != null) {
				sb.append(line);
			}
			if(sb.toString() != null) {
				if(sb.toString().equalsIgnoreCase("disabled")) {
					myLogger.severe("Hey you, stop right there! The version " + Plugin_Version + " is not allowed anymore!");
					myLogger.severe("Apologies, but this plugin will now be disabled! Download a newer version to play: https://dev.bukkit.org/projects/lt-item-mail");
					plugin.getServer().getPluginManager().disablePlugin(plugin);
				}
			}
		} catch(Exception e) {
			myLogger.severe("Plugin: Is this version allowed?");
		}
	}
}
