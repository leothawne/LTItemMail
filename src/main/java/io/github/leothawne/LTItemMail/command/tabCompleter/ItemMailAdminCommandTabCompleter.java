package io.github.leothawne.LTItemMail.command.tabCompleter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

import io.github.leothawne.LTItemMail.api.TabCompleterAPI;

public final class ItemMailAdminCommandTabCompleter implements TabCompleter {
	@Override
	public final List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args){
		final List<String> ReturnNothing = new ArrayList<>();
		if(sender.hasPermission("LTItemMail.admin")) {
			if(args.length == 1) {
				final ImmutableList<String> MailAdmin = ImmutableList.of("update", "list", "recover");
				return TabCompleterAPI.partial(args[0], MailAdmin);
			}
			if(args.length == 2) {
				if(args[0].equals("list")) {
					final LinkedList<String> completes = new LinkedList<>();
					for(final Player p: Bukkit.getOnlinePlayers()) completes.add(p.getName());
					return TabCompleterAPI.partial(args[1], completes);
				}
			}
		}
		return ReturnNothing;
	}
}