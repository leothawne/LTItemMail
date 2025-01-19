package io.github.leothawne.LTItemMail.module;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.module.ConfigurationModule.Type;
import io.github.leothawne.LTItemMail.util.FetchUtil;

public final class ConsoleModule {
	private ConsoleModule() {}
	private static final ConsoleCommandSender sender() {
		return Bukkit.getConsoleSender();
	}
	public static final void hello() {
		sender().sendMessage(ChatColor.DARK_AQUA + " _   _______ _____ __  __ ");
		sender().sendMessage(ChatColor.DARK_AQUA + "| | |__   __|_   _|  \\/  |");
		sender().sendMessage(ChatColor.DARK_AQUA + "| |    | |    | | | \\  / |" + ChatColor.WHITE + "  Build number: #" + ConfigurationModule.get(Type.BUILD_NUMBER));
		sender().sendMessage(ChatColor.DARK_AQUA + "| |    | |    | | | |\\/| |" + ChatColor.WHITE + "  Build date: " + FetchUtil.URL.get(DataModule.getDateURL((Integer) ConfigurationModule.get(Type.BUILD_NUMBER))));
		sender().sendMessage(ChatColor.DARK_AQUA + "|______|_|  |_____|_|  |_|" + ChatColor.WHITE + "  Need support or have questions? https://discord.gg/Nvnrv3P");
	}
	public static final void info(final String message) {
		sender().sendMessage(ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "LTIM " + ChatColor.DARK_GREEN + "INFO" + ChatColor.WHITE + "] " + message);
	}
	public static final void warning(final String message) {
		sender().sendMessage(ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "LTIM " + ChatColor.GOLD + "WARNING" + ChatColor.WHITE + "] " + ChatColor.YELLOW + message);
	}
	public static final void severe(final String message) {
		sender().sendMessage(ChatColor.WHITE + "[" + ChatColor.DARK_AQUA + "LTIM " + ChatColor.DARK_RED + "ERROR" + ChatColor.WHITE + "] " + ChatColor.RED + message);
	}
	public static final void debug(final String message) {
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) sender().sendMessage(ChatColor.GRAY + "[" + LTItemMail.class.getSimpleName() + "] " + message);
	}
	public static final void mailbox(final String message) {
		sender().sendMessage(ChatColor.WHITE + "[Mailbox-Log-Worker] " + message);
	}
	public static final void server(final List<String> warnings) {
		sender().sendMessage(ChatColor.WHITE + "========== " + ChatColor.DARK_AQUA + "LT Item Mail " + ChatColor.RED + "Version Control" + ChatColor.WHITE + " ==========");
		for(final String warning: warnings) sender().sendMessage(ChatColor.WHITE + warning);
		sender().sendMessage(ChatColor.WHITE + "==================================================");
	}
	public static final void server(final Map<Integer, Map<String, Map<String, List<String>>>> messages) {
		final List<CommandSender> receivers = new ArrayList<>();
		receivers.add(sender());
		for(final Player player : Bukkit.getOnlinePlayers()) if(PermissionModule.hasPermission(player, PermissionModule.Type.CMD_ADMIN_NOTIFY)) receivers.add(player);
		for(final CommandSender receiver : receivers) {
			receiver.sendMessage(ChatColor.WHITE + "========== " + ChatColor.DARK_AQUA + "LT Item Mail " + ChatColor.LIGHT_PURPLE + "Messages Board" + ChatColor.WHITE + " ==========");
			receiver.sendMessage("");
			for(final Integer id : messages.keySet()) {
				final Map<String, Map<String, List<String>>> contents1 = messages.get(id);
				for(final String title : contents1.keySet()) {
					final Map<String, List<String>> contents2 = contents1.get(title);
					for(final String datetime : contents2.keySet()) {
						receiver.sendMessage(ChatColor.WHITE + "[" + datetime + " | " + title + "]");
						for(final String message : contents2.get(datetime)) receiver.sendMessage(ChatColor.WHITE + "  " + message);
					}
					receiver.sendMessage("");
				}
			}
			receiver.sendMessage(ChatColor.WHITE + "=================================================");
		}
	}
	public static final void br() {
		/*sender().sendMessage("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣀⣴⣾⣷⣦⣀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		sender().sendMessage("⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣴⣾⡿⠿⠟⠻⠿⢿⣷⣦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		sender().sendMessage("⠀⠀⠀⠀⠀⠀⣀⣤⣾⣿⠟⠁⠀⠀⠀⠀⠀⠀⠈⠻⣿⣷⣤⣀⠀⠀⠀⠀⠀⠀");
		sender().sendMessage("⠀⠀⠀⣀⣴⣾⣿⣿⣿⡿⠿⠿⠿⠶⢶⣦⣤⣀⠀⠀⢹⣿⣿⣿⣷⣦⣀⠀⠀⠀");
		sender().sendMessage("⠀⠰⢾⣿⣿⣿⣿⣿⣿⡁⠀⠀⠀⠀⠀⠀⠈⠙⠳⣦⣈⣿⣿⣿⣿⣿⣿⡷⠆⠀");
		sender().sendMessage("⠀⠀⠀⠉⠻⢿⣿⣿⣿⣇⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⣹⣿⣿⣿⡿⠟⠉⠀⠀⠀");
		sender().sendMessage("⠀⠀⠀⠀⠀⠀⠉⠛⢿⣿⣦⡀⠀⠀⠀⠀⠀⠀⢀⣴⣿⡿⠛⠉⠀⠀⠀⠀⠀⠀");
		sender().sendMessage("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⠻⢿⣷⣶⣦⣴⣶⣾⡿⠟⠁⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		sender().sendMessage("⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠉⠻⢿⡿⠟⠉⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀");
		sender().sendMessage("");*/
		final List<ChatColor> cores = Arrays.asList(ChatColor.GREEN,
				ChatColor.YELLOW);
		int n = 0;
		String obrigado = "";
		for(char letra : new String("Obrigado por instalar o meu plugin! Aproveite =D").toCharArray()) {
			obrigado = obrigado + cores.get(n) + letra;
			n++;
			if(n >= cores.size()) n = 0;
		}
		sender().sendMessage(obrigado);
	}
}