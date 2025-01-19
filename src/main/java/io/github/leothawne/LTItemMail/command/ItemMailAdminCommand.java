package io.github.leothawne.LTItemMail.command;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.block.MailboxBlock;
import io.github.leothawne.LTItemMail.entity.LTPlayer;
import io.github.leothawne.LTItemMail.inventory.MailboxInventory;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.ConsoleModule;
import io.github.leothawne.LTItemMail.module.DataModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.MailboxModule;
import io.github.leothawne.LTItemMail.module.PermissionModule;
import io.github.leothawne.LTItemMail.util.FetchUtil;
import net.md_5.bungee.api.ChatColor;

public final class ItemMailAdminCommand implements CommandExecutor {
	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		Boolean hasPermission = false;
		if(args.length == 0) {
			hasPermission = true;
			Bukkit.dispatchCommand(sender, "ltitemmail:itemmailadmin help");
		} else if(args[0].equalsIgnoreCase("help")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_MAIN)) {
				sender.sendMessage(ChatColor.AQUA + "=+=+=+= [LT Item Mail " + LTItemMail.getInstance().getDescription().getVersion() + " :: Administration] =+=+=+=");
				sender.sendMessage(ChatColor.GREEN + "/itemmailadmin help " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_ADMIN_ITEMMAILADMIN));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_UPDATE)) sender.sendMessage(ChatColor.GREEN + "/itemmailadmin update " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_ADMIN_UPDATE_MAIN));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_LIST)) sender.sendMessage(ChatColor.GREEN + "/itemmailadmin list <player> " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_ADMIN_LIST));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_RECOVER)) sender.sendMessage(ChatColor.GREEN + "/itemmailadmin recover <id> " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_ADMIN_RECOVER));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_RELOAD)) sender.sendMessage(ChatColor.GREEN + "/itemmailadmin reload " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_ADMIN_RELOAD));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_BAN)) sender.sendMessage(ChatColor.GREEN + "/itemmailadmin ban <player> " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_ADMIN_BAN_MAIN));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_UNBAN)) sender.sendMessage(ChatColor.GREEN + "/itemmailadmin unban <player> " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_ADMIN_UNBAN_MAIN));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_BANLIST)) sender.sendMessage(ChatColor.GREEN + "/itemmailadmin banlist " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_ADMIN_BANLIST_MAIN));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_INFO)) sender.sendMessage(ChatColor.GREEN + "/itemmailadmin info <player> " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_MAIN));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_BLOCKS)) sender.sendMessage(ChatColor.GREEN + "/itemmailadmin blocks <player> " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_ADMIN_BLOCKS));
			}
		} else if(args[0].equalsIgnoreCase("update")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_UPDATE)) {
				if(args.length == 1) {
					Bukkit.getScheduler().runTask(LTItemMail.getInstance(), new Runnable() {
						@Override
						public final void run() {
							final Integer localBuild = (Integer) ConfigurationModule.get(ConfigurationModule.Type.BUILD_NUMBER);
							final Integer remoteBuild = Integer.parseInt(FetchUtil.URL.get(DataModule.getUpdateURL()));
							if(remoteBuild > localBuild) {
								final Integer outOfDate = remoteBuild - localBuild;
								final String[] found = LanguageModule.get(LanguageModule.Type.COMMAND_ADMIN_UPDATE_FOUND).split("%");
								sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + found[0] + outOfDate + found[1] + " https://jenkins.gmj.net.br/job/LTItemMail/" + remoteBuild + "/");
							} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.COMMAND_ADMIN_UPDATE_NONEW));
						}
					});
				} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
			}
		} else if(args[0].equalsIgnoreCase("list")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_LIST)) {
				if(args.length == 2) {
					final LTPlayer ltPlayer = LTPlayer.fromName(args[1]);
					if(ltPlayer != null) {
						final HashMap<Integer, String> deleted = DatabaseModule.Virtual.getDeletedMailboxesList(ltPlayer.getUniqueId());
						final HashMap<Integer, String> mailboxes = new HashMap<>();
						for(final Integer mailboxID : deleted.keySet()) if(!DatabaseModule.Virtual.getStatus(mailboxID).equals(DatabaseModule.Virtual.Status.DENIED)) mailboxes.put(mailboxID, deleted.get(mailboxID));
						if(mailboxes.size() > 0) {
							sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_OPENEDBOXES) + " " + ltPlayer.getName() + ":");
							for(final Integer mailboxID : mailboxes.keySet()) {
								String from = "CONSOLE";
								final UUID uuidFrom = DatabaseModule.Virtual.getMailboxFrom(mailboxID);
								if(uuidFrom != null) from = Bukkit.getOfflinePlayer(uuidFrom).getName();
								String label = "";
								if(!DatabaseModule.Virtual.getMailboxLabel(mailboxID).isEmpty()) label = ": " + DatabaseModule.Virtual.getMailboxLabel(mailboxID);
								String empty = "";
								final LinkedList<ItemStack> items = DatabaseModule.Virtual.getMailbox(mailboxID);
								if(items.size() == 0) empty = " [" + LanguageModule.get(LanguageModule.Type.MAILBOX_EMPTY) + "]";
								sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_NAME) + " #" + mailboxID + "" + ChatColor.RESET + " <= " + mailboxes.get(mailboxID) + " (@" + from + ")" + empty + label); 
							}
						} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.MAILBOX_EMPTYLIST) + " " + ltPlayer.getName());
					} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_NEVERPLAYEDERROR));
				} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
			}
		} else if(args[0].equalsIgnoreCase("info")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_INFO)) {
				if(args.length == 2) {
					final LTPlayer ltPlayer = LTPlayer.fromName(args[1]);
					if(ltPlayer != null) {
						if(!ltPlayer.isRegistered()) DatabaseModule.User.register(ltPlayer);
						sender.sendMessage("");
						sender.sendMessage(ChatColor.YELLOW + ltPlayer.getName());
						String divider = "";
						for(int i = 0; i < ltPlayer.getName().toCharArray().length; i++) divider = divider + "-";
						sender.sendMessage(ChatColor.YELLOW + divider);
						sender.sendMessage(ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_REGISTRY) + " " + ltPlayer.getRegistryDate());
						String banned = ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_BANNED_NO);
						String banreason = "";
						if(ltPlayer.isBanned()) {
							banned = ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_BANNED_YES);
							banreason = ltPlayer.getBanReason();
						}
						sender.sendMessage(ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_BANNED_MAIN) + " " + banned);
						if(banreason != null && !banreason.isEmpty()) sender.sendMessage(ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_BANNED_REASON) + " " + banreason);
						sender.sendMessage(ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_SENT) + " " + ltPlayer.getMailSentCount());
						sender.sendMessage(ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_RECEIVED) + " " + ltPlayer.getMailReceivedCount());
						sender.sendMessage("");
					} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_NEVERPLAYEDERROR));
				} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
			}
		} else if(args[0].equalsIgnoreCase("recover")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_RECOVER)) {
				if(sender instanceof Player) {
					final Player player = (Player) sender;
					if(args.length == 2) {
						try {
							final Integer mailboxID = Integer.valueOf(args[1]);
							final LinkedList<ItemStack> items = DatabaseModule.Virtual.getMailbox(mailboxID);
							if(DatabaseModule.Virtual.isMailboxDeleted(mailboxID) && !DatabaseModule.Virtual.getStatus(mailboxID).equals(DatabaseModule.Virtual.Status.DENIED) && items.size() > 0) {
								player.openInventory(MailboxInventory.getInventory(MailboxInventory.Type.IN, mailboxID, null, items, DatabaseModule.Virtual.getMailboxFrom(mailboxID), DatabaseModule.Virtual.getMailboxLabel(mailboxID), true));
								MailboxModule.log(player.getUniqueId(), null, MailboxModule.Action.RECOVERED, mailboxID, null, null, null);
							} else player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_NOLOST));
						} catch (final NumberFormatException e) {
							player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_IDERROR));
						}
					} else player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
				} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_ERROR));
			}
		} else if(args[0].equalsIgnoreCase("reload")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_RELOAD)) {
				if(args.length == 1) {
					LTItemMail.getInstance().reload();
					ConsoleModule.warning(LTItemMail.getInstance().getDescription().getName() + " reloaded!");
					sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "Reloaded!");
				} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
			}
		} else if(args[0].equalsIgnoreCase("ban")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_BAN)) {
				if(args.length > 1) {
					final LTPlayer ltPlayer = LTPlayer.fromName(args[1]);
					String banreason = "";
					for(int i = 2; i < args.length; i++) banreason = banreason + args[i] + " ";
					if(ltPlayer != null) {
						if(!ltPlayer.isRegistered()) DatabaseModule.User.register(ltPlayer);
						if(!ltPlayer.isBanned()) {
							DatabaseModule.User.ban(ltPlayer.getUniqueId(), banreason);
							banreason = " => " + banreason;
							sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + ltPlayer.getName() + " " + LanguageModule.get(LanguageModule.Type.COMMAND_ADMIN_BAN_BANNED) + banreason);
						} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + ltPlayer.getName() + " " + LanguageModule.get(LanguageModule.Type.COMMAND_ADMIN_BAN_ALREADY));
					} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_NEVERPLAYEDERROR));
				} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
			}
		} else if(args[0].equalsIgnoreCase("unban")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_UNBAN)) {
				if(args.length == 2) {
					final LTPlayer ltPlayer = LTPlayer.fromName(args[1]);
					if(ltPlayer != null) {
						if(!ltPlayer.isRegistered()) DatabaseModule.User.register(ltPlayer);
						if(ltPlayer.isBanned()) {
							DatabaseModule.User.unban(ltPlayer.getUniqueId());
							sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + ltPlayer.getName() + " " + LanguageModule.get(LanguageModule.Type.COMMAND_ADMIN_UNBAN_UNBANNED));
						} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + ltPlayer.getName() + " " + LanguageModule.get(LanguageModule.Type.COMMAND_ADMIN_UNBAN_ALREADY));
					} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_NEVERPLAYEDERROR));
				} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
			}
		} else if(args[0].equalsIgnoreCase("banlist")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_BANLIST)) {
				if(args.length == 1) {
					final LinkedList<String> banlist = DatabaseModule.User.getBansList();
					if(banlist.size() > 0) {
						banlist.sort(Comparator.naturalOrder());
						String banString = (String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.COMMAND_ADMIN_BANLIST_LIST) + " ";
						for(final String username : banlist) {
							String end = ", ";
							if(banlist.getLast().equals(username)) end = ".";
							banString = banString + username + end;
						}
						sender.sendMessage(banString);
					} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.COMMAND_ADMIN_BANLIST_EMPTY));
				} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
			}
		} else if(args[0].equalsIgnoreCase("blocks")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_BLOCKS)) {
				if(args.length > 1) {
					final LTPlayer ltPlayer = LTPlayer.fromName(args[1]);
					if(ltPlayer != null) {
						final List<MailboxBlock> mailboxes = new ArrayList<>();
						for(final MailboxBlock block : DatabaseModule.Block.getMailboxBlocks()) if(block.getOwner().equals(ltPlayer.getUniqueId())) mailboxes.add(block);
						if(mailboxes.size() > 0) {
							sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.BLOCK_ADMIN_LIST) + " " + ChatColor.GREEN + ltPlayer.getName() + ":");
							Integer number = 1;
							for(final MailboxBlock block : mailboxes) {
								final Location loc = block.getLocation();
								sender.sendMessage(ChatColor.YELLOW + "    - #" + ChatColor.GREEN + String.valueOf(number) + ChatColor.YELLOW + " : " + LanguageModule.get(LanguageModule.Type.BLOCK_LIST_WORLD) + "=" + ChatColor.GREEN + loc.getWorld().getName() + ChatColor.YELLOW + ", X=" + ChatColor.GREEN + String.valueOf(loc.getBlockX()) + ChatColor.YELLOW + ", Y=" + ChatColor.GREEN + String.valueOf(loc.getBlockY()) + ChatColor.YELLOW + ", Z=" + ChatColor.GREEN + String.valueOf(loc.getBlockZ()));
								number++;
							}
						}
					} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_NEVERPLAYEDERROR));
				} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
			}
		} else if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_ADMIN_MAIN)) {
			final String[] invalidCmd = LanguageModule.get(LanguageModule.Type.COMMAND_INVALID).split("%");
			sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + invalidCmd[0] + ChatColor.GREEN + "/itemmailadmin " + ChatColor.YELLOW + invalidCmd[1]);
		}
		if(!hasPermission) sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_PERMISSIONERROR));
		return true;
	}
}
