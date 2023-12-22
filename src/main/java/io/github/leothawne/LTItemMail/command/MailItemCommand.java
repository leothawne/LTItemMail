package io.github.leothawne.LTItemMail.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.inventory.MailboxInventory;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.type.MailboxType;

public final class MailItemCommand implements CommandExecutor {
	public MailItemCommand() {}
	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if(sender.hasPermission("LTItemMail.send")) {
			if(sender instanceof Player) {
				final Player player = (Player) sender;
				if(args.length < 1) {
					player.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get("recipient-empty"));
				} else if(args.length < 3) {
					final Player player1 = LTItemMail.getInstance().getServer().getPlayer(args[0]);
					if(player1 != null) {
						if(player1.getUniqueId().equals(player.getUniqueId())) {
							if(args.length == 2 && args[1].equalsIgnoreCase("--bypass") && player.hasPermission("LTItemMail.admin")) {
								player.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "Okay...");
								player.openInventory(MailboxInventory.getMailboxInventory(MailboxType.OUT, null, player1, null));
							} else player.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get("player-self"));
						} else {
							player.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get("mailbox-opening"));
							player.openInventory(MailboxInventory.getMailboxInventory(MailboxType.OUT, null, player1, null));
						}
					} else player.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get("recipient-offline"));
				} else player.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get("player-tma"));
			} else sender.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get("player-error"));
		} else {
			sender.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get("no-permission"));
			LTItemMail.getInstance().getConsole().warning(sender.getName() + " does not have permission [LTItemMail.use]!");
		}
		return true;
	}
}