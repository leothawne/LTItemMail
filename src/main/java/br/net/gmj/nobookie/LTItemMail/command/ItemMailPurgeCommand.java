package br.net.gmj.nobookie.LTItemMail.command;

import java.util.Collections;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import br.net.gmj.nobookie.LTItemMail.module.ConfigurationModule;
import br.net.gmj.nobookie.LTItemMail.module.DatabaseModule;
import br.net.gmj.nobookie.LTItemMail.module.LanguageModule;
import br.net.gmj.nobookie.LTItemMail.module.PermissionModule;

@LTCommandInfo(
	name = "itemmailpurge",
	description = "Delete everything from the database.",
	aliases = "ltitemmail:itemmailpurge",
	permission = "ltitemmail.admin.purge"
)
public final class ItemMailPurgeCommand extends LTCommandExecutor {
	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_PURGE)) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "WARNING: " + ChatColor.RED + "Everything from the database will be wiped and cannot be recovered. Are you sure you want to continue? To agree and continue you must type " + ChatColor.YELLOW + "/itemmailpurge --confirm" + ChatColor.RED + ".");
			} else if(args[0].equals("--confirm")) {
				sender.sendMessage(ChatColor.DARK_GREEN + "Purging database now... " + ChatColor.RED + "Please do not turn off the server!");
				if(DatabaseModule.purge()) {
					sender.sendMessage(ChatColor.DARK_GREEN + "Database purged! " + ChatColor.WHITE + "A server restart is recommended but not mandatory.");
				} else sender.sendMessage(ChatColor.DARK_RED + "" + ChatColor.BOLD + "Unable to purge database.");
			} else Bukkit.dispatchCommand(sender, "ltitemmail:itemmailpurge");
		} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_PERMISSIONERROR));
		return true;
	}
	@Override
	public final List<String> onTabComplete(final CommandSender sender, Command cmd, final String commandLabel, final String[] args){
		return Collections.emptyList();
	}
}
