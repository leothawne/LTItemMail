package io.github.leothawne.LTItemMail.command.tabCompleter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

import io.github.leothawne.LTItemMail.api.TabCompleterAPI;
import io.github.leothawne.LTItemMail.module.DatabaseModule;

public final class ItemMailCommandTabCompleter implements TabCompleter {
	@Override
	public final List<String> onTabComplete(CommandSender sender, Command cmd, String commandLabel, String[] args){
		final List<String> ReturnNothing = new ArrayList<>();
		if(sender.hasPermission("LTItemMail.use")) {
			if(args.length == 1) {
				final ImmutableList<String> completes = ImmutableList.of("version", "list", "open");
				return TabCompleterAPI.partial(args[0], completes);
			}
			if(args.length == 2) {
				if(args[0].equals("open")) if(sender instanceof Player) {
					final Player player = (Player) sender;
					final HashMap<Integer, String> mailboxes = DatabaseModule.Function.getMailboxesList(player.getUniqueId());
					final LinkedList<String> completes = new LinkedList<>();
					for(final Integer i : mailboxes.keySet()) completes.add(String.valueOf(i));
					return TabCompleterAPI.partial(args[1], completes);
				}
			}
		}
		return ReturnNothing;
	}
}