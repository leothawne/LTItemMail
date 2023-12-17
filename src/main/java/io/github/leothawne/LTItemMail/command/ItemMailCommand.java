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
import io.github.leothawne.LTItemMail.type.MailboxType;

public final class ItemMailCommand implements CommandExecutor {
	public ItemMailCommand() {}
	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if(sender.hasPermission("LTItemMail.use")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "=+=+=+= [LT Item Mail] =+=+=+=");
				sender.sendMessage(ChatColor.GREEN + "/itemmail " + ChatColor.AQUA + "- Commands of the plugin.");
				sender.sendMessage(ChatColor.GREEN + "/itemmail version " + ChatColor.AQUA + "- Show the current version.");
				sender.sendMessage(ChatColor.GREEN + "/itemmail list " + ChatColor.AQUA + "- List pending mailboxes.");
				sender.sendMessage(ChatColor.GREEN + "/itemmail open <mailbox id> " + ChatColor.AQUA + "- Open a pending mailbox.");
				sender.sendMessage(ChatColor.GREEN + "/mailitem <player> " + ChatColor.AQUA + "- Open a new mailbox to put items inside and send to another player.");
				//sender.sendMessage(ChatColor.GREEN + "/itemmailadmin " + ChatColor.AQUA + "- Commands for administrators.");
				sender.sendMessage(ChatColor.YELLOW + "You can also use "+ ChatColor.GREEN + "/itemmail "+ ChatColor.YELLOW + "as "+ ChatColor.GREEN + "/ima"+ ChatColor.YELLOW + ".");
				sender.sendMessage(ChatColor.YELLOW + "You can also use "+ ChatColor.GREEN + "/mailitem "+ ChatColor.YELLOW + "as "+ ChatColor.GREEN + "/mit"+ ChatColor.YELLOW + " or "+ ChatColor.GREEN + "/enviaritem"+ ChatColor.YELLOW + " or "+ ChatColor.GREEN + "/vit"+ ChatColor.YELLOW + ".");
			} else if(args[0].equalsIgnoreCase("version")) {
				if(args.length == 1) {
					DataModule.version(LTItemMail.getInstance().getDescription().getVersion(), sender);
				} else sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "Too many arguments!");
			} else if(args[0].equalsIgnoreCase("open") && sender instanceof Player) {
				final Player player = (Player) sender;
				if(args.length == 2) try {
					final Integer mailboxID = Integer.valueOf(args[1]);
					if(DatabaseModule.Function.isMaiboxOwner(player.getUniqueId(), mailboxID) && !DatabaseModule.Function.isMailboxOpened(mailboxID)) {
						player.sendMessage(ChatColor.DARK_RED + "" + LTItemMail.getInstance().getLanguage().getString("mailbox-lose"));
						LTItemMail.getInstance().getPlayerBusy().put(player.getUniqueId(), true);
						player.openInventory(MailboxInventory.getMailboxInventory(MailboxType.IN, mailboxID, null, DatabaseModule.Function.getMailbox(mailboxID)));
						DatabaseModule.Function.setMailboxOpened(mailboxID);
					}
				} catch (final NumberFormatException e) {
					player.sendMessage("Mailbox ID must be a number!");
				}
			} else if(args[0].equalsIgnoreCase("list") && sender instanceof Player) {
				final Player player = (Player) sender;
				final HashMap<Integer, String> mailboxes = DatabaseModule.Function.getMailboxesList(player.getUniqueId());
				if(mailboxes.size() > 0) {
					for(final Integer mailboxID : mailboxes.keySet()) player.sendMessage("Mailbox #" + mailboxID + " : " + mailboxes.get(mailboxID));
				} else player.sendMessage("No new mailboxes.");
			} else sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "Invalid command! Type " + ChatColor.GREEN + "/itemmail " + ChatColor.YELLOW + "to see all available commands.");
		} else {
			sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LTItemMail.getInstance().getLanguage().getString("no-permission"));
			LTItemMail.getInstance().getConsole().severe(sender.getName() + " does not have permission [LTItemMail.use].");
		}
		return true;
	}
}
