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
package io.github.leothawne.LTItemMail.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.LTItemMail.ConsoleLoader;
import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.Version;
import io.github.leothawne.LTItemMail.api.utility.HTTP;

public class ItemMailAdminCommand implements CommandExecutor {
	private static LTItemMail plugin;
	private static ConsoleLoader myLogger;
	private static FileConfiguration configuration;
	private static FileConfiguration language;
	public ItemMailAdminCommand(LTItemMail plugin, ConsoleLoader myLogger, FileConfiguration configuration, FileConfiguration language) {
		ItemMailAdminCommand.plugin = plugin;
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
						new BukkitRunnable() {
							@Override
							public final void run() {
								String[] LocalVersion = Version.getVersionNumber().split("\\.");
								int Local_VersionNumber1 = Integer.parseInt(LocalVersion[0]);
								int Local_VersionNumber2 = Integer.parseInt(LocalVersion[1]);
								int Local_VersionNumber3 = Integer.parseInt(LocalVersion[2]);
								String upToDate = ChatColor.AQUA + "[" + configuration.getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "The plugin is up to date!";
								String[] Server1 = HTTP.getData(Version.getUpdateURL()).split("-");
								if(Server1[2].equals(Version.getMinecraftVersion())) {
									String[] Server2 = Server1[0].split("\\.");
									int Server2_VersionNumber1 = Integer.parseInt(Server2[0]);
									int Server2_VersionNumber2 = Integer.parseInt(Server2[1]);
									int Server2_VersionNumber3 = Integer.parseInt(Server2[2]);
									String updateMessage = ChatColor.AQUA + "[" + configuration.getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "A newer version is available: " + ChatColor.GREEN + "" + Server1[0] + "" + ChatColor.YELLOW + " (released on " + ChatColor.GREEN + "" + Server1[1] + "" + ChatColor.YELLOW + ").";
									if(Server2_VersionNumber1 > Local_VersionNumber1) {
										sender.sendMessage(updateMessage);
									} else if(Server2_VersionNumber1 == Local_VersionNumber1 && Server2_VersionNumber2 > Local_VersionNumber2) {
										sender.sendMessage(updateMessage);
									} else if(Server2_VersionNumber1 == Local_VersionNumber1 && Server2_VersionNumber2 == Local_VersionNumber2 && Server2_VersionNumber3 > Local_VersionNumber3) {
										sender.sendMessage(updateMessage);
									} else {
										sender.sendMessage(upToDate);
									}
								} else {
									sender.sendMessage(upToDate);
								}
							}
						}.runTask(plugin);
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
