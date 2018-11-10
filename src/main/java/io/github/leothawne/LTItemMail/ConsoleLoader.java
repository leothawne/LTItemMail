package io.github.leothawne.LTItemMail;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

public class ConsoleLoader {
	private LTItemMailLoader plugin;
	public ConsoleLoader(LTItemMailLoader plugin) {
		this.plugin = plugin;
	}
	private ConsoleCommandSender newLogger() {
		return plugin.getServer().getConsoleSender();
	}
	private String LTIMVersion = new Version(plugin, this).LTIMVersion;
	private String LTIMVersion_Date = new Version(plugin, this).LTIMVersion_Date;
	private String Minecraft_Version = new Version(plugin, this).Minecraft_Version;
	private String Java_Version = new Version(plugin, this).Java_Version;
	public void Hello() {
		newLogger().sendMessage(ChatColor.AQUA + " (            (                      *                ");
		newLogger().sendMessage(ChatColor.AQUA + " )\\ ) *   )   )\\ )   )             (  `           (   ");
		newLogger().sendMessage(ChatColor.AQUA + "(()/` )  /(  (()/(( /(  (    )     )\\))(     ) (  )\\  ");
		newLogger().sendMessage(ChatColor.AQUA + " /(_)( )(_))  /(_))\\())))\\  (     ((_)()\\ ( /( )\\((_) ");
		newLogger().sendMessage(ChatColor.AQUA + "(_))(_(_())  (_))(_))//((_) )\\  ' (_()((_))(_)((_)_   " + ChatColor.LIGHT_PURPLE + "  V " + LTIMVersion + " (Minecraft " + Minecraft_Version + ")");
		newLogger().sendMessage(ChatColor.AQUA + "| | |_   _|  |_ _| |_(_)) _((_))  |  \\/  ((_)_ (_| |  " + ChatColor.LIGHT_PURPLE + "  Works with Java " + Java_Version);
		newLogger().sendMessage(ChatColor.AQUA + "| |__ | |     | ||  _/ -_| '  \\() | |\\/| / _` || | |  " + ChatColor.LIGHT_PURPLE + "  Released on " + LTIMVersion_Date);
		newLogger().sendMessage(ChatColor.AQUA + "|____||_|    |___|\\__\\___|_|_|_|  |_|  |_\\__,_||_|_|  " + ChatColor.LIGHT_PURPLE + "  Twitter @leothawne");
		newLogger().sendMessage("");
	}
	public void Goodbye() {
		newLogger().sendMessage(ChatColor.AQUA + " (               (       )            ");
		newLogger().sendMessage(ChatColor.AQUA + " )\\ )            )\\ ) ( /( (      (   ");
		newLogger().sendMessage(ChatColor.AQUA + "(()/(    (   (  (()/( )\\()))\\ )  ))\\  ");
		newLogger().sendMessage(ChatColor.AQUA + " /(_))_  )\\  )\\  ((_)((_)\\(()/( /((_) ");
		newLogger().sendMessage(ChatColor.AQUA + "(_)) __|((_)((_) _| || |(_))(_)(_))   ");
		newLogger().sendMessage(ChatColor.AQUA + "  | (_ / _ / _ / _` || '_ | || / -_)  ");
		newLogger().sendMessage(ChatColor.AQUA + "   \\___\\___\\___\\__,_||_.__/\\_, \\___|  ");
		newLogger().sendMessage(ChatColor.AQUA + "                           |__/       ");
		newLogger().sendMessage("");
	}
	public void info(String message) {
		newLogger().sendMessage(ChatColor.AQUA + "[LT Item Mail " + ChatColor.WHITE + "INFO" + ChatColor.AQUA + "] " + ChatColor.LIGHT_PURPLE + "" + message);
	}
	public void warning(String message) {
		newLogger().sendMessage(ChatColor.AQUA + "[LT Item Mail " + ChatColor.YELLOW + "WARNING" + ChatColor.AQUA + "] " + ChatColor.LIGHT_PURPLE + "" + message);
	}
	public void severe(String message) {
		newLogger().sendMessage(ChatColor.AQUA + "[LT Item Mail " + ChatColor.RED + "ERROR" + ChatColor.AQUA + "] " + ChatColor.LIGHT_PURPLE + "" + message);
	}
}