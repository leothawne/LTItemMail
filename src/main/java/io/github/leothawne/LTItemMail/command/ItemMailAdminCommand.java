package io.github.leothawne.LTItemMail.command;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.api.HTTP;
import io.github.leothawne.LTItemMail.module.DataModule;

public final class ItemMailAdminCommand implements CommandExecutor {
	public ItemMailAdminCommand() {}
	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if(sender.hasPermission("LTItemMail.admin")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "=+=+=+= [LT Item Mail :: Admin] =+=+=+=");
				sender.sendMessage(ChatColor.GREEN + "/itemmailadmin " + ChatColor.AQUA + "- Commands for administrators.");
				sender.sendMessage(ChatColor.GREEN + "/itemmailadmin update " + ChatColor.AQUA + "- Check for new updates.");
				sender.sendMessage(ChatColor.YELLOW + "You can also use "+ ChatColor.GREEN + "/itemmailadmin "+ ChatColor.YELLOW + "as "+ ChatColor.GREEN + "/imad"+ ChatColor.YELLOW + ".");
			} else if(args[0].equalsIgnoreCase("update")) {
				if(args.length == 1) {
					final CommandSender finalSender = sender;
					new BukkitRunnable() {
						@Override
						public final void run() {
							String[] LocalVersion = LTItemMail.getInstance().getDescription().getVersion().split("\\.");
							int Local_VersionNumber1 = Integer.parseInt(LocalVersion[0]);
							int Local_VersionNumber2 = Integer.parseInt(LocalVersion[1]);
							int Local_VersionNumber3 = Integer.parseInt(LocalVersion[2]);
							String[] Server1 = HTTP.getData(DataModule.getUpdateURL()).split("-");
							String[] Server2 = Server1[0].split("\\.");
							int Server2_VersionNumber1 = Integer.parseInt(Server2[0]);
							int Server2_VersionNumber2 = Integer.parseInt(Server2[1]);
							int Server2_VersionNumber3 = Integer.parseInt(Server2[2]);
							String updateMessage = ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "A newer version is available: " + ChatColor.GREEN + Server1[0] + ChatColor.YELLOW + " (released on " + ChatColor.GREEN + Server1[1] + ChatColor.YELLOW + ").";
							if(Server2_VersionNumber1 > Local_VersionNumber1) {
								finalSender.sendMessage(updateMessage);
							} else if(Server2_VersionNumber1 == Local_VersionNumber1 && Server2_VersionNumber2 > Local_VersionNumber2) {
								finalSender.sendMessage(updateMessage);
							} else if(Server2_VersionNumber1 == Local_VersionNumber1 && Server2_VersionNumber2 == Local_VersionNumber2 && Server2_VersionNumber3 > Local_VersionNumber3) {
								finalSender.sendMessage(updateMessage);
							} else finalSender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "The plugin is up to date!");
						}
					}.runTaskAsynchronously(LTItemMail.getInstance());
				} else sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "Too many arguments!");
			} else sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "Invalid command! Type " + ChatColor.GREEN + "/itemmailadmin " + ChatColor.YELLOW + "to see all available commands.");
		} else {
			sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "" + LTItemMail.getInstance().getLanguage().getString("no-permission"));
			LTItemMail.getInstance().getConsole().severe(sender.getName() + " does not have permission [LTItemMail.admin].");
		}
		return true;
	}
}
