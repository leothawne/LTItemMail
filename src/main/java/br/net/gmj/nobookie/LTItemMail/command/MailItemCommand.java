package br.net.gmj.nobookie.LTItemMail.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import br.net.gmj.nobookie.LTItemMail.entity.LTPlayer;
import br.net.gmj.nobookie.LTItemMail.inventory.MailboxInventory;
import br.net.gmj.nobookie.LTItemMail.module.ConfigurationModule;
import br.net.gmj.nobookie.LTItemMail.module.DatabaseModule;
import br.net.gmj.nobookie.LTItemMail.module.LanguageModule;
import br.net.gmj.nobookie.LTItemMail.module.PermissionModule;
import net.md_5.bungee.api.ChatColor;

public final class MailItemCommand implements CommandExecutor {
	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_SEND)) {
			if(sender instanceof Player) {
				final LTPlayer player = LTPlayer.fromUUID(((Player) sender).getUniqueId());
				if(!player.isRegistered()) DatabaseModule.User.register(player);
				if(!player.isBanned()) {
					if(args.length == 0) {
						player.getBukkitPlayer().getPlayer().sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_MISSINGERROR));
					} else if(args.length >= 1) {
						final LTPlayer playerTo = LTPlayer.fromName(args[0]);
						if(playerTo != null) {
							if(playerTo.getUniqueId().equals(player.getUniqueId())) {
								if(args.length == 2 && args[1].equalsIgnoreCase("--bypass") && PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_BYPASS)) {
									player.getBukkitPlayer().getPlayer().sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "Ok...");
									player.getBukkitPlayer().getPlayer().openInventory(MailboxInventory.getInventory(MailboxInventory.Type.OUT, null, playerTo, null, player.getUniqueId(), "", false));
								} else player.getBukkitPlayer().getPlayer().sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_SELFERROR));
							} else {
								String label = "";
								if(args.length > 1) for(int i = 1; i < args.length; i++) label = label + args[i] + " ";
								player.getBukkitPlayer().getPlayer().openInventory(MailboxInventory.getInventory(MailboxInventory.Type.OUT, null, playerTo, null, player.getUniqueId(), label, false));
							}
						} else player.getBukkitPlayer().getPlayer().sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_NEVERPLAYEDERROR));
					} else player.getBukkitPlayer().getPlayer().sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
				} else player.getBukkitPlayer().getPlayer().sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_BANNED));
			} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_ERROR));
		} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_PERMISSIONERROR));
		return true;
	}
}