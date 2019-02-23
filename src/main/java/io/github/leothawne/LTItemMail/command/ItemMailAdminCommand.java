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
package io.github.leothawne.LTItemMail.command;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import io.github.leothawne.LTItemMail.ConsoleLoader;
import io.github.leothawne.LTItemMail.Version;

public class ItemMailAdminCommand implements CommandExecutor {
	private static ConsoleLoader myLogger;
	private static FileConfiguration configuration;
	private static FileConfiguration language;
	public ItemMailAdminCommand(ConsoleLoader myLogger, FileConfiguration configuration, FileConfiguration language) {
		ItemMailAdminCommand.myLogger = myLogger;
		ItemMailAdminCommand.configuration = configuration;
		ItemMailAdminCommand.language = language;
	}
	@Override
	public final boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender.hasPermission("LTItemMail.use")) {
			if(sender.hasPermission("LTItemMail.admin")) {
				if(args.length == 0) {
					sender.sendMessage(ChatColor.AQUA + "=+=+=+= [LT Item Mail :: Admin] =+=+=+=");
					sender.sendMessage(ChatColor.GREEN + "/itemmailadmin " + ChatColor.AQUA + "- Administration commands for LT Item Mail.");
					sender.sendMessage(ChatColor.GREEN + "/itemmailadmin version " + ChatColor.AQUA + "- Check for new updates.");
					sender.sendMessage(ChatColor.YELLOW + "You can also use "+ ChatColor.GREEN + "/itemmailadmin "+ ChatColor.YELLOW + "as "+ ChatColor.GREEN + "/imad"+ ChatColor.YELLOW + ".");
				} else if(args[0].equalsIgnoreCase("version")) {
					if(args.length < 2) {
						try {
							URLConnection newUpdateURL = new URL("https://leothawne.github.io/LTItemMail/api/version.html").openConnection();
							newUpdateURL.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
							newUpdateURL.connect();
							BufferedReader newUpdateReader = new BufferedReader(new InputStreamReader(newUpdateURL.getInputStream(), Charset.forName("UTF-8")));
							StringBuilder sb = new StringBuilder();
							String line;
							while((line = newUpdateReader.readLine()) != null) {
								sb.append(line);
							}
							if(sb.toString() != null) {
								String[] LocalVersion = Version.getVersionNumber().split("\\.");
								int Local_VersionNumber1 = Integer.parseInt(LocalVersion[0]);
								int Local_VersionNumber2 = Integer.parseInt(LocalVersion[1]);
								int Local_VersionNumber3 = Integer.parseInt(LocalVersion[2]);
								String[] Server1 = sb.toString().split("-");
								String[] Server2 = Server1[0].split("\\.");
								int Server2_VersionNumber1 = Integer.parseInt(Server2[0]);
								int Server2_VersionNumber2 = Integer.parseInt(Server2[1]);
								int Server2_VersionNumber3 = Integer.parseInt(Server2[2]);
								sender.sendMessage(ChatColor.AQUA + "[" + configuration.getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "Running: " + ChatColor.GREEN + "" + Version.getVersionNumber() + "" + ChatColor.YELLOW + " (Released on " + ChatColor.GREEN + "" + Version.getVersionDate() + "" + ChatColor.YELLOW + ")");
								String updateMessage = ChatColor.AQUA + "[" + configuration.getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "A newer version is available: " + ChatColor.GREEN + "" + Server1[0] + "" + ChatColor.YELLOW + " (released on " + ChatColor.GREEN + "" + Server1[1] + "" + ChatColor.YELLOW + ")";
								if(Server2_VersionNumber1 > Local_VersionNumber1) {
									sender.sendMessage(updateMessage);
								} else if(Server2_VersionNumber1 == Local_VersionNumber1 && Server2_VersionNumber2 > Local_VersionNumber2) {
									sender.sendMessage(updateMessage);
								} else if(Server2_VersionNumber1 == Local_VersionNumber1 && Server2_VersionNumber2 == Local_VersionNumber2 && Server2_VersionNumber3 > Local_VersionNumber3) {
									sender.sendMessage(updateMessage);
								} else {
									sender.sendMessage(ChatColor.AQUA + "[" + configuration.getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "The plugin is up to date!");
								}
							} else {
								sender.sendMessage(ChatColor.AQUA + "[" + configuration.getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "Error while checking for new updates: Server did not respond correctly.");
							}
						} catch(Exception e) {
							myLogger.severe("Error while checking for new updates: " + e.getMessage());
							sender.sendMessage(ChatColor.AQUA + "[" + configuration.getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "Error while checking for new updates.");
						}
					} else {
						sender.sendMessage(ChatColor.AQUA + "[" + configuration.getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "Too many arguments!");
					}
				} else {
					sender.sendMessage(ChatColor.AQUA + "[" + configuration.getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "Invalid command! Type " + ChatColor.GREEN + "/itemmailadmin " + ChatColor.YELLOW + "to see all available commands.");
				}
			} else {
				sender.sendMessage(ChatColor.AQUA + "[" + configuration.getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "" + language.getString("no-permission"));
				myLogger.severe(sender.getName() + " does not have permission [LTItemMail.admin].");
			}
		} else {
			sender.sendMessage(ChatColor.AQUA + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + language.getString("no-permission"));
			myLogger.severe(sender.getName() + " does not have permission [LTItemMail.use].");
		}
		return true;
	}
}
