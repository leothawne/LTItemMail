package br.net.gmj.nobookie.LTItemMail.command.tabCompleter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import br.net.gmj.nobookie.LTItemMail.module.BungeeModule;
import br.net.gmj.nobookie.LTItemMail.module.ConfigurationModule;
import br.net.gmj.nobookie.LTItemMail.module.PermissionModule;
import br.net.gmj.nobookie.LTItemMail.util.TabUtil;

public final class ItemMailAdminCommandTabCompleter implements TabCompleter {
	@Override
	public final List<String> onTabComplete(final CommandSender sender, Command cmd, final String commandLabel, final String[] args){
		if(args.length == 1) {
			final List<String> commands = new ArrayList<>();
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_MAIN)) commands.add("help");
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_UPDATE)) commands.add("update");
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_LIST)) commands.add("list");
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_RECOVER)) commands.add("recover");
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_RELOAD)) commands.add("reload");
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_INFO)) commands.add("info");
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_BAN)) commands.add("ban");
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_UNBAN)) commands.add("unban");
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_BANLIST)) commands.add("banlist");
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_BLOCKS)) commands.add("blocks");
			return TabUtil.partial(args[0], commands);
		}
		if(args.length == 2) if((PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_LIST) && args[0].equals("list")) || (PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_BAN) && args[0].equals("ban")) || (PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_UNBAN) && args[0].equals("unban")) || (PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_INFO) && args[0].equals("info")) || (PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_BLOCKS) && args[0].equals("blocks"))) {
			final LinkedList<String> response = new LinkedList<>();
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.BUNGEE_MODE)) {
				for(final String bungeePlayer : BungeeModule.getOnlinePlayers()) response.add(bungeePlayer);
				for(final Player p: Bukkit.getOnlinePlayers()) if(!response.contains(p.getName())) response.add(p.getName());
			} else for(final Player onlinePlayer : Bukkit.getOnlinePlayers()) response.add(onlinePlayer.getName());
			return TabUtil.partial(args[1], response);
		}
		return Collections.emptyList();
	}
}