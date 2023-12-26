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
import io.github.leothawne.LTItemMail.module.PermissionModule;

public final class ItemMailCommandTabCompleter implements TabCompleter {
	@Override
	public final List<String> onTabComplete(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args){
		if(args.length == 1) if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_MAIN)) return TabCompleterAPI.partial(args[0], ImmutableList.of("version", "list", "open", "delete"));
		if(args.length == 2) if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_OPEN) || PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_DELETE)) if(args[0].equals("open") || args[0].equals("delete")) if(sender instanceof Player) {
			final Player player = (Player) sender;
			final HashMap<Integer, String> mailboxes = DatabaseModule.Function.getMailboxesList(player.getUniqueId());
			final LinkedList<String> response = new LinkedList<>();
			for(final Integer i : mailboxes.keySet()) response.add(String.valueOf(i));
			return TabCompleterAPI.partial(args[1], response);
		}
		return new ArrayList<>();
	}
}