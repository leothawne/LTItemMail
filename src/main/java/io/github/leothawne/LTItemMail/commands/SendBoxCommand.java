package io.github.leothawne.LTItemMail.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import io.github.leothawne.LTItemMail.ConsoleLoader;
import io.github.leothawne.LTItemMail.LTItemMailLoader;
import io.github.leothawne.LTItemMail.commands.inventories.SendBoxInventory;

public class SendBoxCommand implements CommandExecutor {
	private LTItemMailLoader plugin;
	private ConsoleLoader myLogger;
	private FileConfiguration language;
	public SendBoxCommand(LTItemMailLoader plugin, ConsoleLoader myLogger, FileConfiguration language) {
		this.plugin = plugin;
		this.myLogger = myLogger;
		this.language = language;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender.hasPermission("LTItemMail.send")) {
			if(sender instanceof Player) {
				Player player = (Player) sender;
				if(args.length < 1) {
					player.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.YELLOW + "" + language.getString("recipient-empty"));
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
								player.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.YELLOW + "Okay...");
								player.openInventory(new SendBoxInventory().GUI(player1));
							} else {
								player.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.YELLOW + "" + language.getString("player-self"));
							}
						} else {
							player.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.YELLOW + "" + language.getString("mailbox-opening"));
							player.openInventory(new SendBoxInventory().GUI(player1));
						}
					} else {
						player.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.YELLOW + "" + language.getString("recipient-offline"));
					}
				} else {
					player.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.YELLOW + "" + language.getString("player-tma"));
				}
			} else {
				sender.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.YELLOW + "" + language.getString("player-error"));
			}
		} else {
			sender.sendMessage(ChatColor.DARK_GREEN + "[LTIM] " + ChatColor.YELLOW + "" + language.getString("no-permission"));
			myLogger.warning(sender.getName() + " does not have permission [LTItemMail.use]!");
		}
		return true;
	}
}