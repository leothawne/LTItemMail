package br.net.gmj.nobookie.LTItemMail.command;

import java.util.List;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public abstract class LTCommandExecutor implements CommandExecutor {
	public abstract List<String> onTabComplete(final CommandSender sender, final Command cmd, final String alias, final String[] args);
}