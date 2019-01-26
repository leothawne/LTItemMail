package io.github.leothawne.LTItemMail.commands.constructor;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

public class ItemMailConstructTabCompleter implements TabCompleter {
	@Override
	public final List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args){
		List<String> ReturnNothing = new ArrayList<>();
		if(sender.hasPermission("LTItemMail.use") && cmd.getName().equalsIgnoreCase("itemmail")) {
			if(args.length == 1) {
				List<String> Mail = new ArrayList<>();
				Mail.add("version");
				return Mail;
			}
		}
		return ReturnNothing;
	}
}