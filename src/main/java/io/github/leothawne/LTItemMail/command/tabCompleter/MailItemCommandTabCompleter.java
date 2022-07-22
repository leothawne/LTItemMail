package io.github.leothawne.LTItemMail.command.tabCompleter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.api.TabCompleterAPI;

public final class MailItemCommandTabCompleter implements TabCompleter {
	private static LTItemMail plugin;
	public MailItemCommandTabCompleter(final LTItemMail plugin) {
		MailItemCommandTabCompleter.plugin = plugin;
	}
	@Override
	public final List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args){
		final List<String> ReturnNothing = new ArrayList<>();
		if(sender.hasPermission("LTItemMail.send")) {
			if(args.length == 1) {
				final LinkedList<String> players = new LinkedList<String>();
				for(final Player player : MailItemCommandTabCompleter.plugin.getServer().getOnlinePlayers()) {
					players.add(player.getName());
				}
				return TabCompleterAPI.partial(args[0], players);
			}
		}
		return ReturnNothing;
	}
}