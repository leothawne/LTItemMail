package io.github.leothawne.LTItemMail.commands.constructor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class SendBoxConstructTabCompleter implements TabCompleter {
	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args){
		List<String> ReturnNothing = new ArrayList<>();
		if(sender.hasPermission("LTItemMail.send") && cmd.getName().equalsIgnoreCase("sendbox")) {
			if(args.length == 1) {
				return null;
			}
		}
		return ReturnNothing;
	}
}