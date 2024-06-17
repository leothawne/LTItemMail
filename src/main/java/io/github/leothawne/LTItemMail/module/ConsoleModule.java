package io.github.leothawne.LTItemMail.module;

import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import io.github.leothawne.LTItemMail.LTItemMail;

public final class ConsoleModule {
	private ConsoleModule() {}
	private static final ConsoleCommandSender sender() {
		return Bukkit.getConsoleSender();
	}
	public static final void hello() {
		sender().sendMessage(ChatColor.DARK_AQUA + " _   _______ _____ __  __ ");
		sender().sendMessage(ChatColor.DARK_AQUA + "| | |__   __|_   _|  \\/  |");
		sender().sendMessage(ChatColor.DARK_AQUA + "| |    | |    | | | \\  / |" + ChatColor.WHITE + "  Build number: " + LTItemMail.getInstance().getDescription().getVersion());
		sender().sendMessage(ChatColor.DARK_AQUA + "| |    | |    | | | |\\/| |" + ChatColor.WHITE + "  Build date/time: " + DataModule.getDate());
		sender().sendMessage(ChatColor.DARK_AQUA + "| |____| |   _| |_| |  | |" + ChatColor.WHITE + "  Requires: Java " + DataModule.getJava() + " (or higher)");
		sender().sendMessage(ChatColor.DARK_AQUA + "|______|_|  |_____|_|  |_|" + ChatColor.WHITE + "  Need support or have questions? https://discord.gg/Nvnrv3P");
	}
	public static final void info(final String message) {
		sender().sendMessage(ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "LTIM " + ChatColor.DARK_GREEN + "INFO" + ChatColor.WHITE + "] " + message);
	}
	public static final void warning(final String message) {
		sender().sendMessage(ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "LTIM " + ChatColor.GOLD + "WARNING" + ChatColor.WHITE + "] " + ChatColor.YELLOW + message);
	}
	public static final void severe(final String message) {
		sender().sendMessage(ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "LTIM " + ChatColor.DARK_RED + "ERROR" + ChatColor.WHITE + "] " + ChatColor.RED + message);
	}
	public static final void debug(final String message) {
		sender().sendMessage(ChatColor.DARK_GRAY + "[" + LTItemMail.class.getName() + "] " + message);
	}
	public static final void mailbox(final String message) {
		sender().sendMessage(ChatColor.WHITE + "[Mailbox-Log-Worker] " + message);
	}
	public static final void server(final List<String> warnings) {
		sender().sendMessage(ChatColor.WHITE + "========== " + ChatColor.DARK_AQUA + "LT Item Mail " + ChatColor.RED + "Version Control" + ChatColor.WHITE + " ==========");
		for(final String warning: warnings) sender().sendMessage(ChatColor.WHITE + warning);
		sender().sendMessage(ChatColor.WHITE + "==================================================");
	}
	public static final void server(final Map<Integer, Map<String, Map<String, List<String>>>> messages) {
		sender().sendMessage(ChatColor.WHITE + "========== " + ChatColor.DARK_AQUA + "LT Item Mail " + ChatColor.LIGHT_PURPLE + "Messages Board" + ChatColor.WHITE + " ==========");
		sender().sendMessage("");
		for(final Integer id : messages.keySet()) {
			final Map<String, Map<String, List<String>>> contents1 = messages.get(id);
			for(final String title : contents1.keySet()) {
				final Map<String, List<String>> contents2 = contents1.get(title);
				for(final String datetime : contents2.keySet()) {
					sender().sendMessage(ChatColor.WHITE + "[" + datetime + " | " + title + "]");
					for(final String message : contents2.get(datetime)) sender().sendMessage(ChatColor.WHITE + "  " + message);
				}
				sender().sendMessage("");
			}
		}
		sender().sendMessage(ChatColor.WHITE + "=================================================");
		for(final Player player : Bukkit.getOnlinePlayers()) if(PermissionModule.hasPermission(player, PermissionModule.Type.CMD_ADMIN_NOTIFY)) {
			player.sendMessage(ChatColor.WHITE + "========== " + ChatColor.DARK_AQUA + "LT Item Mail " + ChatColor.LIGHT_PURPLE + "Messages Board" + ChatColor.WHITE + " ==========");
			player.sendMessage("");
			for(final Integer id : messages.keySet()) {
				final Map<String, Map<String, List<String>>> contents1 = messages.get(id);
				for(final String title : contents1.keySet()) {
					final Map<String, List<String>> contents2 = contents1.get(title);
					for(final String datetime : contents2.keySet()) {
						player.sendMessage(ChatColor.WHITE + "[" + datetime + " | " + title + "]");
						for(final String message : contents2.get(datetime)) player.sendMessage(ChatColor.WHITE + "  " + message);
					}
					player.sendMessage("");
				}
			}
			player.sendMessage(ChatColor.WHITE + "=================================================");
		}
	}
}