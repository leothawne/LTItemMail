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
import org.bukkit.command.ConsoleCommandSender;

public class ConsoleLoader {
	private LTItemMail plugin;
	public ConsoleLoader(LTItemMail plugin) {
		this.plugin = plugin;
	}
	private final ConsoleCommandSender getConsoleSender() {
		return plugin.getServer().getConsoleSender();
	}
	public final void Hello() {
		getConsoleSender().sendMessage(ChatColor.AQUA + " _   _______ _____ __  __ ");
		getConsoleSender().sendMessage(ChatColor.AQUA + "| | |__   __|_   _|  \\/  |");
		getConsoleSender().sendMessage(ChatColor.AQUA + "| |    | |    | | | \\  / |" + ChatColor.WHITE + "  V: " + Version.getVersionNumber() + " (Minecraft: " + Version.getMinecraftVersion() + ")");
		getConsoleSender().sendMessage(ChatColor.AQUA + "| |    | |    | | | |\\/| |" + ChatColor.WHITE + "  Requires Java: " + Version.getJavaVersion());
		getConsoleSender().sendMessage(ChatColor.AQUA + "| |____| |   _| |_| |  | |" + ChatColor.WHITE + "  Released on: " + Version.getVersionDate());
		getConsoleSender().sendMessage(ChatColor.AQUA + "|______|_|  |_____|_|  |_|" + ChatColor.WHITE + "  My Twitter: @leonappi_");
	}
	public final void info(String message) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + "LTIM " + ChatColor.GREEN + "I" + ChatColor.WHITE + "] " + message);
	}
	public final void warning(String message) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + "LTIM " + ChatColor.YELLOW + "W" + ChatColor.WHITE + "] " + message);
	}
	public final void severe(String message) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + "LTIM " + ChatColor.RED + "E" + ChatColor.WHITE + "] " + message);
	}
}