package io.github.leothawne.LTItemMail.command.tabCompleter;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import io.github.leothawne.LTItemMail.module.BungeeModule;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.PermissionModule;
import io.github.leothawne.LTItemMail.util.TabUtil;

public final class MailItemCommandTabCompleter implements TabCompleter {
	public MailItemCommandTabCompleter() {}
	@Override
	public final List<String> onTabComplete(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args){
		Player player = null;
		if(args.length == 1) if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_SEND)) {
			if(sender instanceof Player) player = (Player) sender;
			final LinkedList<String> response = new LinkedList<>();
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.BUNGEE_MODE)) {
				for(final String bungeePlayer : BungeeModule.getOnlinePlayers()) {
					if(bungeePlayer.equals(player.getName())) continue;
					response.add(bungeePlayer);
				}
			} else for(final Player onlinePlayer : Bukkit.getOnlinePlayers()) {
				if(onlinePlayer.getName().equals(player.getName())) continue;
				response.add(onlinePlayer.getName());
			}
			return TabUtil.partial(args[0], response);
		}
		return Collections.emptyList();
	}
}