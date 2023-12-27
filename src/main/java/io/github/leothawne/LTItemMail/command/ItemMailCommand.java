package io.github.leothawne.LTItemMail.command;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.inventory.MailboxInventory;
import io.github.leothawne.LTItemMail.module.DataModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.MailboxLogModule;
import io.github.leothawne.LTItemMail.module.PermissionModule;

public final class ItemMailCommand implements CommandExecutor {
	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		Boolean hasPermission = false;
		if(args.length == 0) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_MAIN)) {
				sender.sendMessage(ChatColor.AQUA + "=+=+=+= [LT Item Mail] =+=+=+=");
				sender.sendMessage(ChatColor.GREEN + "/itemmail " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_ITEMMAIL));
				sender.sendMessage(ChatColor.GREEN + "/itemmail version " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_VERSION));
				sender.sendMessage(ChatColor.GREEN + "/itemmail list " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_LIST));
				sender.sendMessage(ChatColor.GREEN + "/itemmail open <mailbox id> " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_OPEN));
				sender.sendMessage(ChatColor.GREEN + "/itemmail delete <mailbox id> " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_DELETE));
				sender.sendMessage(ChatColor.GREEN + "/mailitem <player> " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_MAILITEM));
			}
		} else if(args[0].equalsIgnoreCase("version")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_VERSION)) {
				if(args.length == 1) {
					DataModule.version(LTItemMail.getInstance().getDescription().getVersion(), sender);
				} else sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
			}
		} else if(args[0].equalsIgnoreCase("open")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_OPEN)) {
				if(sender instanceof Player) {
					final Player player = (Player) sender;
					if(args.length == 2) {
						try {
							final Integer mailboxID = Integer.valueOf(args[1]);
							if(DatabaseModule.Virtual.isMaiboxOwner(player.getUniqueId(), mailboxID) && !DatabaseModule.Virtual.isMailboxOpened(mailboxID)) player.openInventory(MailboxInventory.getMailboxInventory(MailboxInventory.Type.IN, mailboxID, null, DatabaseModule.Virtual.getMailbox(mailboxID)));
						} catch (final NumberFormatException e) {
							player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_IDERROR));
						}
					} else player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
				} else sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_ERROR));
			}
		} else if(args[0].equalsIgnoreCase("list")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_LIST)) {
				if(sender instanceof Player) {
					final Player player = (Player) sender;
					if(args.length == 1) {
						final HashMap<Integer, String> mailboxes = DatabaseModule.Virtual.getMailboxesList(player.getUniqueId());
						if(mailboxes.size() > 0) {
							for(final Integer mailboxID : mailboxes.keySet()) player.sendMessage(LTItemMail.getInstance().getConfiguration().getString("mailbox-name") + " #" + mailboxID + " : " + mailboxes.get(mailboxID));
						} else player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_NONEW));
					} else player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
				} else sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_ERROR));
			}
		} else if(args[0].equalsIgnoreCase("delete")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_DELETE)) {
				if(sender instanceof Player) {
					final Player player = (Player) sender;
					if(args.length == 2) {
						try {
							final Integer mailboxID = Integer.valueOf(args[1]);
							if(DatabaseModule.Virtual.isMaiboxOwner(player.getUniqueId(), mailboxID) && !DatabaseModule.Virtual.isMailboxOpened(mailboxID)) {
								DatabaseModule.Virtual.setMailboxOpened(mailboxID);
								MailboxLogModule.log(player.getUniqueId(), null, MailboxLogModule.Action.OPENED, mailboxID, null);
								player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_DELETED) + " " + LTItemMail.getInstance().getConfiguration().getString("mailbox-name") + " #" + mailboxID);
							}
						} catch (final NumberFormatException e) {
							player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_IDERROR));
						}
					} else player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
				} else sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_ERROR));
			}
		} else {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_MAIN)) {
				final String[] invalidCmd = LanguageModule.get(LanguageModule.Type.COMMAND_INVALID).split("%");
				sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + invalidCmd[0] + ChatColor.GREEN + "/itemmail " + ChatColor.YELLOW + invalidCmd[1]);
			}
		}
		if(!hasPermission) sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_PERMISSIONERROR));
		return true;
	}
}
