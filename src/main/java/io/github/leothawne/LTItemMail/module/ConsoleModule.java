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
	private static final ConsoleCommandSender getConsoleSender() {
		return Bukkit.getConsoleSender();
	}
	public static final void hello() {
		getConsoleSender().sendMessage(ChatColor.DARK_AQUA + " _   _______ _____ __  __ ");
		getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "| | |__   __|_   _|  \\/  |");
		getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "| |    | |    | | | \\  / |" + ChatColor.WHITE + "  Build number: " + LTItemMail.getInstance().getDescription().getVersion());
		getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "| |    | |    | | | |\\/| |" + ChatColor.WHITE + "  Build date & time: " + DataModule.getVersionDate());
		getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "| |____| |   _| |_| |  | |" + ChatColor.WHITE + "  Requires: Java " + DataModule.getVersion(DataModule.VersionType.JAVA) + " (or higher)");
		getConsoleSender().sendMessage(ChatColor.DARK_AQUA + "|______|_|  |_____|_|  |_|" + ChatColor.WHITE + "  Need support or have questions? https://discord.gg/Nvnrv3P");
	}
	public static final void info(final String message) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "LTIM " + ChatColor.DARK_GREEN + "INFO" + ChatColor.WHITE + "] " + message);
	}
	public static final void warning(final String message) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "LTIM " + ChatColor.GOLD + "WARNING" + ChatColor.WHITE + "] " + ChatColor.YELLOW + message);
	}
	public static final void severe(final String message) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "LTIM " + ChatColor.DARK_RED + "ERROR" + ChatColor.WHITE + "] " + ChatColor.RED + message);
	}
	public static final void debug(final String message) {
		getConsoleSender().sendMessage(ChatColor.DARK_GRAY + "[" + LTItemMail.class.getName() + "] " + message);
	}
	public static final void mailbox(final String message) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "[Mailbox-Log-Worker] " + message);
	}
	public static final void server(final List<String> warnings) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "========== " + ChatColor.DARK_AQUA + "LT Item Mail " + ChatColor.RED + "Version Control" + ChatColor.WHITE + " ==========");
		for(final String warning: warnings) getConsoleSender().sendMessage(ChatColor.WHITE + warning);
		getConsoleSender().sendMessage(ChatColor.WHITE + "==================================================");
	}
	public static final void server(final Map<Integer, Map<String, Map<String, List<String>>>> messages) {
		getConsoleSender().sendMessage(ChatColor.WHITE + "========== " + ChatColor.DARK_AQUA + "LT Item Mail " + ChatColor.LIGHT_PURPLE + "Messages Board" + ChatColor.WHITE + " ==========");
		getConsoleSender().sendMessage("");
		for(final Integer id : messages.keySet()) {
			final Map<String, Map<String, List<String>>> contents1 = messages.get(id);
			for(final String title : contents1.keySet()) {
				final Map<String, List<String>> contents2 = contents1.get(title);
				for(final String datetime : contents2.keySet()) {
					getConsoleSender().sendMessage(ChatColor.WHITE + "[" + datetime + " | " + title + "]");
					for(final String message : contents2.get(datetime)) getConsoleSender().sendMessage(ChatColor.WHITE + "  " + message);
				}
				getConsoleSender().sendMessage("");
			}
		}
		getConsoleSender().sendMessage(ChatColor.WHITE + "=================================================");
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