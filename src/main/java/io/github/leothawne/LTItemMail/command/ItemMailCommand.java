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
import io.github.leothawne.LTItemMail.type.LanguageType;
import io.github.leothawne.LTItemMail.type.MailboxType;

public final class ItemMailCommand implements CommandExecutor {
	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if(sender.hasPermission("LTItemMail.use")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "=+=+=+= [LT Item Mail] =+=+=+=");
				sender.sendMessage(ChatColor.GREEN + "/itemmail " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageType.COMMAND_PLAYER_ITEMMAIL));
				sender.sendMessage(ChatColor.GREEN + "/itemmail version " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageType.COMMAND_PLAYER_VERSION));
				sender.sendMessage(ChatColor.GREEN + "/itemmail list " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageType.COMMAND_PLAYER_LIST));
				sender.sendMessage(ChatColor.GREEN + "/itemmail open <mailbox id> " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageType.COMMAND_PLAYER_OPEN));
				sender.sendMessage(ChatColor.GREEN + "/itemmail delete <mailbox id> " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageType.COMMAND_PLAYER_DELETE));
				sender.sendMessage(ChatColor.GREEN + "/mailitem <player> " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageType.COMMAND_PLAYER_MAILITEM));
			} else if(args[0].equalsIgnoreCase("version")) {
				if(args.length == 1) {
					DataModule.version(LTItemMail.getInstance().getDescription().getVersion(), sender);
				} else sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + LanguageModule.get(LanguageType.PLAYER_SYNTAXERROR));
			} else if(args[0].equalsIgnoreCase("open") && sender instanceof Player) {
				final Player player = (Player) sender;
				if(args.length == 2) {
					try {
						final Integer mailboxID = Integer.valueOf(args[1]);
						if(DatabaseModule.Function.isMaiboxOwner(player.getUniqueId(), mailboxID) && !DatabaseModule.Function.isMailboxOpened(mailboxID)) player.openInventory(MailboxInventory.getMailboxInventory(MailboxType.IN, mailboxID, null, DatabaseModule.Function.getMailbox(mailboxID)));
					} catch (final NumberFormatException e) {
						player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageType.MAILBOX_IDERROR));
					}
				} else player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + LanguageModule.get(LanguageType.PLAYER_SYNTAXERROR));
			} else if(args[0].equalsIgnoreCase("list") && sender instanceof Player) {
				final Player player = (Player) sender;
				if(args.length == 1) {
					final HashMap<Integer, String> mailboxes = DatabaseModule.Function.getMailboxesList(player.getUniqueId());
					if(mailboxes.size() > 0) {
						for(final Integer mailboxID : mailboxes.keySet()) player.sendMessage(LTItemMail.getInstance().getConfiguration().getString("mailbox-name") + " #" + mailboxID + " : " + mailboxes.get(mailboxID));
					} else player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageType.MAILBOX_NONEW));
				} else player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + LanguageModule.get(LanguageType.PLAYER_SYNTAXERROR));
			} else if(args[0].equalsIgnoreCase("delete") && sender instanceof Player) {
				final Player player = (Player) sender;
				if(args.length == 2) {
					try {
						final Integer mailboxID = Integer.valueOf(args[1]);
						if(DatabaseModule.Function.isMaiboxOwner(player.getUniqueId(), mailboxID) && !DatabaseModule.Function.isMailboxOpened(mailboxID)) {
							DatabaseModule.Function.setMailboxOpened(mailboxID);
							MailboxLogModule.log(player.getUniqueId(), null, MailboxLogModule.Action.OPENED, mailboxID);
							player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageType.MAILBOX_DELETED) + " " + LTItemMail.getInstance().getConfiguration().getString("mailbox-name") + " #" + mailboxID);
						}
					} catch (final NumberFormatException e) {
						player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageType.MAILBOX_IDERROR));
					}
				} else player.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + LanguageModule.get(LanguageType.PLAYER_SYNTAXERROR));
			} else {
				final String[] invalidCmd = LanguageModule.get(LanguageType.COMMAND_INVALID).split("%");
				sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + invalidCmd[0] + ChatColor.GREEN + "/itemmail " + ChatColor.YELLOW + invalidCmd[1]);
			}
		} else sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageType.PLAYER_PERMISSIONERROR));
		return true;
	}
}
