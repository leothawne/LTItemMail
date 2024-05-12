package io.github.leothawne.LTItemMail.command.tabCompleter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

import io.github.leothawne.LTItemMail.lib.Completer;
import io.github.leothawne.LTItemMail.module.BungeeModule;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.PermissionModule;

public final class ItemMailAdminCommandTabCompleter implements TabCompleter {
	@Override
	public final List<String> onTabComplete(final CommandSender sender, Command cmd, final String commandLabel, final String[] args){
		if(args.length == 1) if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_MAIN)) return Completer.partial(args[0], ImmutableList.of("update", "list", "recover", "reload", "ban", "unban", "banlist"));
		if(args.length == 2) if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_LIST)) if(args[0].equals("list") || args[0].equals("ban") || args[0].equals("unban")) {
			final LinkedList<String> response = new LinkedList<>();
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.BUNGEE_MODE)) {
				for(final String bungeePlayer : BungeeModule.getOnlinePlayers()) response.add(bungeePlayer);
			} else for(final Player onlinePlayer : Bukkit.getOnlinePlayers()) response.add(onlinePlayer.getName());
			for(final Player p: Bukkit.getOnlinePlayers()) response.add(p.getName());
			return Completer.partial(args[1], response);
		}
		return Collections.emptyList();
	}
}