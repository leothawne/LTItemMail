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
import org.bukkit.entity.Player;

import io.github.leothawne.LTItemMail.ConsoleLoader;
import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.inventory.command.SendBoxCommandInventory;

public class SendBoxCommand implements CommandExecutor {
	private static LTItemMail plugin;
	private static ConsoleLoader myLogger;
	private static FileConfiguration configuration;
	private static FileConfiguration language;
	public SendBoxCommand(LTItemMail plugin, ConsoleLoader myLogger, FileConfiguration configuration, FileConfiguration language) {
		SendBoxCommand.plugin = plugin;
		SendBoxCommand.myLogger = myLogger;
		SendBoxCommand.configuration = configuration;
		SendBoxCommand.language = language;
	}
	@Override
	public final boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender.hasPermission("LTItemMail.send")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(args.length < 1) {
					player.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + language.getString("recipient-empty"));
				} else if(args.length < 3) {
					Player player1 = plugin.getServer().getPlayer(args[0]);
					if(player1 != null) {
						if(player1.getUniqueId().equals(player.getUniqueId())) {
							if(args.length == 2 && ((configuration.getString("language").equalsIgnoreCase("english") && args[1].equalsIgnoreCase("becauseiwant")) || (configuration.getString("language").equalsIgnoreCase("portuguese") && args[1].equalsIgnoreCase("porqueeuquero")))) {
								player.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "Okay...");
								new SendBoxCommandInventory();
								player.openInventory(SendBoxCommandInventory.GUI(player1));
							} else {
								player.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + language.getString("player-self"));
							}
						} else {
							player.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + language.getString("mailbox-opening"));
							new SendBoxCommandInventory();
							player.openInventory(SendBoxCommandInventory.GUI(player1));
						}
					} else {
						player.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + language.getString("recipient-offline"));
					}
				} else {
					player.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + language.getString("player-tma"));
				}
			} else {
				sender.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + language.getString("player-error"));
			}
		} else {
			sender.sendMessage(ChatColor.DARK_GREEN + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + language.getString("no-permission"));
			myLogger.warning(sender.getName() + " does not have permission [LTItemMail.use]!");
		}
		return true;
	}
}