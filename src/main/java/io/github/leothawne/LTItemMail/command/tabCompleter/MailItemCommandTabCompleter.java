package io.github.leothawne.LTItemMail.command.tabCompleter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import io.github.leothawne.LTItemMail.lib.Completer;
import io.github.leothawne.LTItemMail.module.PermissionModule;

public final class MailItemCommandTabCompleter implements TabCompleter {
	public MailItemCommandTabCompleter() {}
	@Override
	public final List<String> onTabComplete(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args){
		if(args.length == 1) if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_SEND)) {
			final LinkedList<String> response = new LinkedList<>();
			for(final Player player : Bukkit.getOnlinePlayers()) response.add(player.getName());
			return Completer.partial(args[0], response);
		}
		return Collections.emptyList();
	}
}