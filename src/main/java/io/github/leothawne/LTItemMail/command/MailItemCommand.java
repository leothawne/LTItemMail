package io.github.leothawne.LTItemMail.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.inventory.MailboxInventory;
import io.github.leothawne.LTItemMail.module.ConsoleModule;
import io.github.leothawne.LTItemMail.type.MailboxType;

public final class MailItemCommand implements CommandExecutor {
	private static LTItemMail plugin;
	private static ConsoleModule console;
	private static FileConfiguration configuration;
	private static FileConfiguration language;
	public MailItemCommand(final LTItemMail plugin, final ConsoleModule console, final FileConfiguration configuration, final FileConfiguration language) {
		MailItemCommand.plugin = plugin;
		MailItemCommand.console = console;
		MailItemCommand.configuration = configuration;
		MailItemCommand.language = language;
	}
	@Override
	public final boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender.hasPermission("LTItemMail.send")) {
			if(sender instanceof Player) {
				final Player player = (Player) sender;
				if(args.length < 1) {
					player.sendMessage(ChatColor.DARK_GREEN + "[" + MailItemCommand.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + MailItemCommand.language.getString("recipient-empty"));
				} else if(args.length < 3) {
					final Player player1 = MailItemCommand.plugin.getServer().getPlayer(args[0]);
					if(player1 != null) {
						if(player1.getUniqueId().equals(player.getUniqueId())) {
							if(args.length == 2 && args[1].equalsIgnoreCase("--bypass") && player.hasPermission("LTItemMail.admin")) {
								player.sendMessage(ChatColor.DARK_GREEN + "[" + MailItemCommand.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "Okay...");
								player.openInventory(MailboxInventory.getMailboxInventory(MailboxType.OUT, player1, null));
							} else {
								player.sendMessage(ChatColor.DARK_GREEN + "[" + MailItemCommand.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + MailItemCommand.language.getString("player-self"));
							}
						} else {
							player.sendMessage(ChatColor.DARK_GREEN + "[" + MailItemCommand.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + MailItemCommand.language.getString("mailbox-opening"));
							player.openInventory(MailboxInventory.getMailboxInventory(MailboxType.OUT, player1, null));
						}
					} else {
						player.sendMessage(ChatColor.DARK_GREEN + "[" + MailItemCommand.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + MailItemCommand.language.getString("recipient-offline"));
					}
				} else {
					player.sendMessage(ChatColor.DARK_GREEN + "[" + MailItemCommand.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + MailItemCommand.language.getString("player-tma"));
				}
			} else {
				sender.sendMessage(ChatColor.DARK_GREEN + "[" + MailItemCommand.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + MailItemCommand.language.getString("player-error"));
			}
		} else {
			sender.sendMessage(ChatColor.DARK_GREEN + "[" + MailItemCommand.configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + MailItemCommand.language.getString("no-permission"));
			MailItemCommand.console.warning(sender.getName() + " does not have permission [LTItemMail.use]!");
		}
		return true;
	}
}