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
		getConsoleSender().sendMessage(ChatColor.AQUA + "| |    | |    | | | \\  / |" + ChatColor.WHITE + "  Build number: " + LTItemMail.getInstance().getDescription().getVersion());
		getConsoleSender().sendMessage(ChatColor.AQUA + "| |    | |    | | | |\\/| |" + ChatColor.WHITE + "  Build date & time: " + DataModule.getVersionDate());
		getConsoleSender().sendMessage(ChatColor.AQUA + "| |____| |   _| |_| |  | |" + ChatColor.WHITE + "  Requires: Java " + DataModule.getVersion(DataModule.VersionType.JAVA) + " (or higher)");
		getConsoleSender().sendMessage(ChatColor.AQUA + "|______|_|  |_____|_|  |_|" + ChatColor.WHITE + "  Need support or have questions? https://discord.gg/Nvnrv3P");
	}
	public static final void info(final String message) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + "LTIM " + ChatColor.GREEN + "INFO" + ChatColor.WHITE + "] " + message);
	}
	public static final void warning(final String message) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + "LTIM " + ChatColor.YELLOW + "WARNING" + ChatColor.WHITE + "] " + ChatColor.YELLOW + message);
	}
	public static final void severe(final String message) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "[" + ChatColor.AQUA + "LTIM " + ChatColor.RED + "ERROR" + ChatColor.WHITE + "] " + ChatColor.RED + message);
	}
	public static final void debug(final String message) {
		getConsoleSender().sendMessage("[" + LTItemMail.class.getName() + "] " + message);
	}
	public static final void mailbox(final String message) {
		getConsoleSender().sendMessage("[Mailbox-Log-Worker] " + message);
	}
}