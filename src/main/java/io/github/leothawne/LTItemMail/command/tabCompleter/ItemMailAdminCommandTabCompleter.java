package io.github.leothawne.LTItemMail.command.tabCompleter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.google.common.collect.ImmutableList;

import io.github.leothawne.LTItemMail.api.utility.TabCompleterAPI;

public class ItemMailAdminCommandTabCompleter extends TabCompleterAPI implements TabCompleter {
	@Override
	public final List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args){
		List<String> ReturnNothing = new ArrayList<>();
		if(sender.hasPermission("LTItemMail.use") && sender.hasPermission("LTItemMail.admin") && cmd.getName().equalsIgnoreCase("itemmailadmin")) {
			if(args.length == 1) {
				ImmutableList<String> MailAdmin = ImmutableList.of("version");
				return partial(args[0], MailAdmin);
			}
		}
		return ReturnNothing;
	}
}