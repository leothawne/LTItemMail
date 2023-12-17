package io.github.leothawne.LTItemMail.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.module.DataModule;

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
			} else if(args[0].equalsIgnoreCase("open")) {
				
			} else if(args[0].equalsIgnoreCase("list")) {
				
			} else sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "Invalid command! Type " + ChatColor.GREEN + "/itemmail " + ChatColor.YELLOW + "to see all available commands.");
		} else {
			sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LTItemMail.getInstance().getLanguage().getString("no-permission"));
			LTItemMail.getInstance().getConsole().severe(sender.getName() + " does not have permission [LTItemMail.use].");
		}
		return true;
	}
}
