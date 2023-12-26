package io.github.leothawne.LTItemMail.module;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import io.github.leothawne.LTItemMail.LTItemMail;

public final class ConsoleModule {
	private ConsoleModule() {}
	private static final ConsoleCommandSender getConsoleSender() {
		return Bukkit.getConsoleSender();
	}
	public static final void Hello() {
		getConsoleSender().sendMessage(ChatColor.AQUA + " _   _______ _____ __  __ ");
		getConsoleSender().sendMessage(ChatColor.AQUA + "| | |__   __|_   _|  \\/  |");
		getConsoleSender().sendMessage(ChatColor.AQUA + "| |    | |    | | | \\  / |" + ChatColor.WHITE + "  V: " + LTItemMail.getInstance().getDescription().getVersion() + " (Minecraft: " + DataModule.getVersion(DataModule.VersionType.MINECRAFT) + ")");
		getConsoleSender().sendMessage(ChatColor.AQUA + "| |    | |    | | | |\\/| |" + ChatColor.WHITE + "  Requires Java: " + DataModule.getVersion(DataModule.VersionType.JAVA) + " (or higher)");
		getConsoleSender().sendMessage(ChatColor.AQUA + "| |____| |   _| |_| |  | |" + ChatColor.WHITE + "  Released on: " + DataModule.getVersionDate());
		getConsoleSender().sendMessage(ChatColor.AQUA + "|______|_|  |_____|_|  |_|" + ChatColor.WHITE + "  Creator's X (Twitter): @nc2ps");
	}
	public static final void info(final String message) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + "LTIM " + ChatColor.GREEN + "I" + ChatColor.WHITE + "] " + message);
	}
	public static final void warning(final String message) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + "LTIM " + ChatColor.YELLOW + "W" + ChatColor.WHITE + "] " + ChatColor.YELLOW + message);
	}
	public static final void severe(final String message) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + "LTIM " + ChatColor.RED + "E" + ChatColor.WHITE + "] " + ChatColor.RED + message);
	}
}