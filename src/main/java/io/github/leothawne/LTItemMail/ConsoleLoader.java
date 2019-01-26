package io.github.leothawne.LTItemMail;

import org.bukkit.ChatColor;
import org.bukkit.command.ConsoleCommandSender;

public class ConsoleLoader {
	private LTItemMailLoader plugin;
	public ConsoleLoader(LTItemMailLoader plugin) {
		this.plugin = plugin;
	}
	private final ConsoleCommandSender newLogger() {
		return plugin.getServer().getConsoleSender();
	}
	private final String LTIMVersion = Version.getVersionNumber();
	private final String LTIMVersion_Date = Version.getVersionDate();
	private final String Minecraft_Version = Version.getMinecraftVersion();
	private final String Java_Version = Version.getJavaVersion();
	public final void Hello() {
		newLogger().sendMessage(ChatColor.AQUA + " (            (                      *                ");
		newLogger().sendMessage(ChatColor.AQUA + " )\\ ) *   )   )\\ )   )             (  `           (   ");
		newLogger().sendMessage(ChatColor.AQUA + "(()/` )  /(  (()/(( /(  (    )     )\\))(     ) (  )\\  ");
		newLogger().sendMessage(ChatColor.AQUA + " /(_)( )(_))  /(_))\\())))\\  (     ((_)()\\ ( /( )\\((_) ");
		newLogger().sendMessage(ChatColor.AQUA + "(_))(_(_())  (_))(_))//((_) )\\  ' (_()((_))(_)((_)_   " + ChatColor.LIGHT_PURPLE + "  V " + LTIMVersion + " (Minecraft " + Minecraft_Version + ")");
		newLogger().sendMessage(ChatColor.AQUA + "| | |_   _|  |_ _| |_(_)) _((_))  |  \\/  ((_)_ (_| |  " + ChatColor.LIGHT_PURPLE + "  Works with Java " + Java_Version);
		newLogger().sendMessage(ChatColor.AQUA + "| |__ | |     | ||  _/ -_| '  \\() | |\\/| / _` || | |  " + ChatColor.LIGHT_PURPLE + "  Released on " + LTIMVersion_Date);
		newLogger().sendMessage(ChatColor.AQUA + "|____||_|    |___|\\__\\___|_|_|_|  |_|  |_\\__,_||_|_|  " + ChatColor.LIGHT_PURPLE + "  Twitter @leonappi_");
		newLogger().sendMessage("");
	}
	public final void Goodbye() {
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
	public final void info(String message) {
		newLogger().sendMessage(ChatColor.AQUA + "[LT Item Mail " + ChatColor.WHITE + "INFO" + ChatColor.AQUA + "] " + ChatColor.LIGHT_PURPLE + "" + message);
	}
	public final void warning(String message) {
		newLogger().sendMessage(ChatColor.AQUA + "[LT Item Mail " + ChatColor.YELLOW + "WARNING" + ChatColor.AQUA + "] " + ChatColor.LIGHT_PURPLE + "" + message);
	}
	public final void severe(String message) {
		newLogger().sendMessage(ChatColor.AQUA + "[LT Item Mail " + ChatColor.RED + "ERROR" + ChatColor.AQUA + "] " + ChatColor.LIGHT_PURPLE + "" + message);
	}
}