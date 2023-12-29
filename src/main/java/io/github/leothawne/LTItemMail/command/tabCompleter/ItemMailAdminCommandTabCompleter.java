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
import io.github.leothawne.LTItemMail.module.PermissionModule;

public final class ItemMailAdminCommandTabCompleter implements TabCompleter {
	@Override
	public final List<String> onTabComplete(final CommandSender sender, Command cmd, final String commandLabel, final String[] args){
		if(args.length == 1) if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_MAIN)) return TabCompleterAPI.partial(args[0], ImmutableList.of("update", "list", "recover", "reload"));
		if(args.length == 2) if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_LIST)) if(args[0].equals("list")) {
			final LinkedList<String> response = new LinkedList<>();
			for(final Player p: Bukkit.getOnlinePlayers()) response.add(p.getName());
			return TabCompleterAPI.partial(args[1], response);
		}
		return new ArrayList<>();
	}
}