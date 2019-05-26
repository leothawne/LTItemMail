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

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.module.ConsoleModule;
import io.github.leothawne.LTItemMail.module.DataModule;

public final class ItemMailCommand implements CommandExecutor {
	private static LTItemMail plugin;
	private static ConsoleModule console;
	private static FileConfiguration configuration;
	private static FileConfiguration language;
	public ItemMailCommand(final LTItemMail plugin, final ConsoleModule console, final FileConfiguration configuration, final FileConfiguration language) {
		ItemMailCommand.plugin = plugin;
		ItemMailCommand.console = console;
		ItemMailCommand.configuration = configuration;
		ItemMailCommand.language = language;
	}
	@Override
	public final boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender.hasPermission("LTItemMail.use")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "=+=+=+= [LT Item Mail] =+=+=+=");
				sender.sendMessage(ChatColor.GREEN + "/itemmail " + ChatColor.AQUA + "- Shows commands for everyone.");
				sender.sendMessage(ChatColor.GREEN + "/itemmail version " + ChatColor.AQUA + "- Shows the current version.");
				sender.sendMessage(ChatColor.GREEN + "/mailitem <player> " + ChatColor.AQUA + "- Open a mailbox to put items inside.");
				sender.sendMessage(ChatColor.GREEN + "/itemmailadmin " + ChatColor.AQUA + "- Shows commands for administrators.");
				sender.sendMessage(ChatColor.YELLOW + "You can also use "+ ChatColor.GREEN + "/itemmail "+ ChatColor.YELLOW + "as "+ ChatColor.GREEN + "/ima"+ ChatColor.YELLOW + ".");
				sender.sendMessage(ChatColor.YELLOW + "You can also use "+ ChatColor.GREEN + "/mailitem "+ ChatColor.YELLOW + "as "+ ChatColor.GREEN + "/mit"+ ChatColor.YELLOW + " or "+ ChatColor.GREEN + "/enviaritem"+ ChatColor.YELLOW + " or "+ ChatColor.GREEN + "/vit"+ ChatColor.YELLOW + ".");
			} else if(args[0].equalsIgnoreCase("version")) {
				if(args.length < 2) {
					DataModule.version(ItemMailCommand.plugin.getDescription().getVersion(), sender);
				} else {
					sender.sendMessage(ChatColor.AQUA + "[" + ItemMailCommand.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "Too many arguments!");
				}
			} else {
				sender.sendMessage(ChatColor.AQUA + "[" + ItemMailCommand.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "Invalid command! Type " + ChatColor.GREEN + "/itemmail " + ChatColor.YELLOW + "to see all available commands.");
			}
		} else {
			sender.sendMessage(ChatColor.AQUA + "[" + ItemMailCommand.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + ItemMailCommand.language.getString("no-permission"));
			ItemMailCommand.console.severe(sender.getName() + " does not have permission [LTItemMail.use].");
		}
		return true;
	}
}
