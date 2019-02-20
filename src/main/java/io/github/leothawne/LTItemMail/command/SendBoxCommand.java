package io.github.leothawne.LTItemMail.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import io.github.leothawne.LTItemMail.ConsoleLoader;
import io.github.leothawne.LTItemMail.LTItemMailLoader;
import io.github.leothawne.LTItemMail.inventory.command.SendBoxCommandInventory;

public class SendBoxCommand implements CommandExecutor {
	private static LTItemMailLoader plugin;
	private static ConsoleLoader myLogger;
	private static FileConfiguration configuration;
	private static FileConfiguration language;
	public SendBoxCommand(LTItemMailLoader plugin, ConsoleLoader myLogger, FileConfiguration configuration, FileConfiguration language) {
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
					Player player1 = null;
					for(Player player2 : plugin.getServer().getOnlinePlayers()) {
						if(player2.getName().equalsIgnoreCase(args[0])) {
							player1 = plugin.getServer().getPlayer(player2.getUniqueId());
						}
					}
					if(player1 != null) {
						if(player1.getUniqueId().equals(player.getUniqueId())) {
							if(args.length == 2 && args[1].equalsIgnoreCase("becauseiwant")) {
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