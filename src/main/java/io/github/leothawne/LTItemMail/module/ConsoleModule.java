package io.github.leothawne.LTItemMail.module;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.type.VersionType;

public final class ConsoleModule {
	public ConsoleModule() {}
	private final ConsoleCommandSender getConsoleSender() {
		return Bukkit.getConsoleSender();
	}
	public final void Hello() {
		getConsoleSender().sendMessage(ChatColor.AQUA + " _   _______ _____ __  __ ");
		getConsoleSender().sendMessage(ChatColor.AQUA + "| | |__   __|_   _|  \\/  |");
		getConsoleSender().sendMessage(ChatColor.AQUA + "| |    | |    | | | \\  / |" + ChatColor.WHITE + "  V: " + LTItemMail.getInstance().getDescription().getVersion() + " (Minecraft: " + DataModule.getVersion(VersionType.MINECRAFT) + ")");
		getConsoleSender().sendMessage(ChatColor.AQUA + "| |    | |    | | | |\\/| |" + ChatColor.WHITE + "  Requires Java: " + DataModule.getVersion(VersionType.JAVA));
		getConsoleSender().sendMessage(ChatColor.AQUA + "| |____| |   _| |_| |  | |" + ChatColor.WHITE + "  Released on: " + DataModule.getVersionDate());
		getConsoleSender().sendMessage(ChatColor.AQUA + "|______|_|  |_____|_|  |_|" + ChatColor.WHITE + "  My Twitter: @nc2ps");
	}
	public final void info(final String message) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + "LTIM " + ChatColor.GREEN + "I" + ChatColor.WHITE + "] " + message);
	}
	public final void warning(final String message) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + "LTIM " + ChatColor.YELLOW + "W" + ChatColor.WHITE + "] " + message);
	}
	public final void severe(final String message) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + "LTIM " + ChatColor.RED + "E" + ChatColor.WHITE + "] " + message);
	}
}