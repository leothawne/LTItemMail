package io.github.leothawne.LTItemMail.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.inventory.MailboxInventory;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.PermissionModule;

public final class MailItemCommand implements CommandExecutor {
	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_SEND)) {
			if(sender instanceof Player) {
				final Player player = (Player) sender;
				if(args.length == 0) {
					player.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_MISSINGERROR));
				} else if(args.length == 1) {
					final Player player1 = LTItemMail.getInstance().getServer().getPlayer(args[0]);
					if(player1 != null) {
						if(player1.getUniqueId().equals(player.getUniqueId())) {
							if(args.length == 2 && args[1].equalsIgnoreCase("--bypass") && PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_BYPASS)) {
								player.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "Ok...");
								player.openInventory(MailboxInventory.getMailboxInventory(MailboxInventory.Type.OUT, null, player1, null));
							} else player.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_SELFERROR));
						} else player.openInventory(MailboxInventory.getMailboxInventory(MailboxInventory.Type.OUT, null, player1, null));
					} else player.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_OFFLINEERROR));
				} else player.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
			} else sender.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_ERROR));
		} else sender.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_PERMISSIONERROR));
		return true;
	}
}