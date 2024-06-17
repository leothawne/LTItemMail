package io.github.leothawne.LTItemMail.command.tabCompleter;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.ImmutableList;

import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.PermissionModule;
import io.github.leothawne.LTItemMail.util.TabUtil;

public final class ItemMailCommandTabCompleter implements TabCompleter {
	@Override
	public final List<String> onTabComplete(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args){
		if(args.length == 1) if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_MAIN)) return TabUtil.partial(args[0], ImmutableList.of("help", "version", "list", "open", "delete", "info", "price", "color"));
		if(args.length == 2) {
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_OPEN) || PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_DELETE)) if(args[0].equals("open") || args[0].equals("delete")) if(sender instanceof Player) {
				final Player player = (Player) sender;
				final HashMap<Integer, String> mailboxes = DatabaseModule.Virtual.getMailboxesList(player.getUniqueId());
				final LinkedList<String> response = new LinkedList<>();
				for(final Integer i : mailboxes.keySet()) response.add(String.valueOf(i));
				response.sort(Comparator.naturalOrder());
				return TabUtil.partial(args[1], response);
			}
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_COLOR)) if(args[0].equals("color")) {
				final LinkedList<String> colors = new LinkedList<>();
				for(Material color : Material.values()) if(color.toString().endsWith("_SHULKER_BOX")) colors.add(color.toString().replace("_SHULKER_BOX", "").toLowerCase());
				colors.sort(Comparator.naturalOrder());
				return TabUtil.partial(args[1], colors);
			}
		}
		return Collections.emptyList();
	}
}