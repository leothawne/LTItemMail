package io.github.leothawne.LTItemMail.command;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.leothawne.LTItemMail.inventory.MailboxInventory;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.PermissionModule;
import net.md_5.bungee.api.ChatColor;

public final class MailItemCommand implements CommandExecutor {
	@SuppressWarnings("deprecation")
	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_SEND)) {
			if(sender instanceof Player) {
				final Player player = (Player) sender;
				if(args.length == 0) {
					player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_MISSINGERROR));
				} else if(args.length >= 1) {
					final OfflinePlayer playerTo = Bukkit.getOfflinePlayer(args[0]);
					if(playerTo.hasPlayedBefore()) {
						if(playerTo.getUniqueId().equals(player.getUniqueId())) {
							if(args.length == 2 && args[1].equalsIgnoreCase("--bypass") && PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_BYPASS)) {
								player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "Ok...");
								player.openInventory(MailboxInventory.getMailboxInventory(MailboxInventory.Type.OUT, null, playerTo, null, ""));
							} else player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_SELFERROR));
						} else {
							String label = "";
							if(args.length > 1) for(int i = 1; i < args.length; i++) label = label + args[i] + " ";
							player.openInventory(MailboxInventory.getMailboxInventory(MailboxInventory.Type.OUT, null, playerTo, null, label));
						}
					} else player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_NEVERPLAYEDERROR));
				} else player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
			} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_ERROR));
		} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_PERMISSIONERROR));
		return true;
	}
}