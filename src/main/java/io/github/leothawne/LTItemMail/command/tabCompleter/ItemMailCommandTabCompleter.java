package io.github.leothawne.LTItemMail.command.tabCompleter;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import com.google.common.collect.ImmutableList;

import io.github.leothawne.LTItemMail.api.utility.TabCompleterAPI;

public class ItemMailCommandTabCompleter extends TabCompleterAPI implements TabCompleter {
	@Override
	public final List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args){
		List<String> ReturnNothing = new ArrayList<>();
		if(sender.hasPermission("LTItemMail.use") && cmd.getName().equalsIgnoreCase("itemmail")) {
			if(args.length == 1) {
				ImmutableList<String> Mail = ImmutableList.of("version");
				return partial(args[0], Mail);
			}
		}
		return ReturnNothing;
	}
}