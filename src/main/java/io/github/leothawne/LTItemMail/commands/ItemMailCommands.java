package io.github.leothawne.LTItemMail.commands;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;

import io.github.leothawne.LTItemMail.ConsoleLoader;
import io.github.leothawne.LTItemMail.LTItemMailLoader;
import io.github.leothawne.LTItemMail.Version;

public class ItemMailCommands implements CommandExecutor {
	private LTItemMailLoader plugin;
	private ConsoleLoader myLogger;
	private FileConfiguration configuration;
	private FileConfiguration language;
	public ItemMailCommands(LTItemMailLoader plugin, ConsoleLoader myLogger, FileConfiguration configuration, FileConfiguration language) {
		this.plugin = plugin;
		this.myLogger = myLogger;
		this.configuration = configuration;
		this.language = language;
	}
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
		if(sender.hasPermission("LTItemMail.use")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "=+=+=+= [LT Item Mail] =+=+=+=");
				sender.sendMessage(ChatColor.GREEN + "/itemmail " + ChatColor.AQUA + "- Show all commands for LT Item Mail.");
				sender.sendMessage(ChatColor.GREEN + "/itemmail version " + ChatColor.AQUA + "- Show plugin version.");
				sender.sendMessage(ChatColor.GREEN + "/sendbox <player> " + ChatColor.AQUA + "- Send items to players.");
				sender.sendMessage(ChatColor.GREEN + "/itemmailadmin " + ChatColor.AQUA + "- Administration commands for LT Item Mail.");
				sender.sendMessage(ChatColor.YELLOW + "You can also use "+ ChatColor.GREEN + "/itemmail "+ ChatColor.YELLOW + "as "+ ChatColor.GREEN + "/ima"+ ChatColor.YELLOW + ".");
				sender.sendMessage(ChatColor.YELLOW + "You can also use "+ ChatColor.GREEN + "/sendbox "+ ChatColor.YELLOW + "as "+ ChatColor.GREEN + "/sbx"+ ChatColor.YELLOW + ".");
			} else if(args[0].equalsIgnoreCase("version")) {
				if(args.length < 2) {
					new Version(plugin, myLogger).version(sender);
				} else {
					sender.sendMessage(ChatColor.AQUA + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "Too many arguments!");
				}
			} else {
				sender.sendMessage(ChatColor.AQUA + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "Invalid command! Type " + ChatColor.GREEN + "/itemmail " + ChatColor.YELLOW + "to see all available commands.");
			}
		} else {
			sender.sendMessage(ChatColor.AQUA + "[" + configuration.getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + language.getString("no-permission"));
			myLogger.severe(sender.getName() + " does not have permission [LTItemMail.use].");
		}
		return true;
	}
}