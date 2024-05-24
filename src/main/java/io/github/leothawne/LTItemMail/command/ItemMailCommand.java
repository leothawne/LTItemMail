package io.github.leothawne.LTItemMail.command;

import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.LTPlayer;
import io.github.leothawne.LTItemMail.inventory.MailboxInventory;
import io.github.leothawne.LTItemMail.item.MailboxItem;
import io.github.leothawne.LTItemMail.item.model.Item;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.DataModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.IntegrationModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.MailboxModule;
import io.github.leothawne.LTItemMail.module.PermissionModule;
import net.md_5.bungee.api.ChatColor;

public final class ItemMailCommand implements CommandExecutor {
	@SuppressWarnings("incomplete-switch")
	@Override
	public final boolean onCommand(final CommandSender sender, final Command command, final String commandLabel, final String[] args) {
		Boolean hasPermission = false;
		if(args.length == 0) {
			hasPermission = true;
			HashMap<Integer, String> mailboxes = null;
			Player player = null;
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_OPEN) && sender instanceof Player && (mailboxes = DatabaseModule.Virtual.getMailboxesList((player = (Player) sender).getUniqueId())).size() > 0) {
				Integer first = 0;
				for(final Integer id : mailboxes.keySet()) if(DatabaseModule.Virtual.getStatus(id).equals(DatabaseModule.Virtual.Status.PENDING)) {
					first = id;
					break;
				}
				if(first > 0) {
					player.performCommand("ltitemmail:itemmail open " + first);
				} else Bukkit.dispatchCommand(sender, "ltitemmail:itemmail help");
			} else Bukkit.dispatchCommand(sender, "ltitemmail:itemmail help");
		} else if(args[0].equalsIgnoreCase("help")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_MAIN)) {
				sender.sendMessage(ChatColor.AQUA + "=+=+=+= [LT Item Mail " + LTItemMail.getInstance().getDescription().getVersion() + "] =+=+=+=");
				sender.sendMessage(ChatColor.GREEN + "/itemmail help " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_ITEMMAIL));
				sender.sendMessage(ChatColor.GREEN + "/itemmail version " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_VERSION));
				sender.sendMessage(ChatColor.GREEN + "/itemmail list " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_LIST));
				sender.sendMessage(ChatColor.GREEN + "/itemmail open <mailbox id> " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_OPEN));
				sender.sendMessage(ChatColor.GREEN + "/itemmail delete <mailbox id> " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_DELETE));
				sender.sendMessage(ChatColor.GREEN + "/itemmail costs " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_COSTS));
				sender.sendMessage(ChatColor.GREEN + "/itemmail info " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_MAIN));
				sender.sendMessage(ChatColor.GREEN + "/itemmail color <color> " + ChatColor.AQUA + "- ");
				sender.sendMessage(ChatColor.GREEN + "/mailitem <player> [label] " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_MAILITEM));
			}
		} else if(args[0].equalsIgnoreCase("version")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_VERSION)) {
				if(args.length == 1) {
					DataModule.showVersion(LTItemMail.getInstance().getDescription().getVersion(), sender);
				} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
			}
		} else if(args[0].equalsIgnoreCase("open")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_OPEN)) {
				if(sender instanceof Player) {
					final Player player = (Player) sender;
					if(args.length == 2) {
						try {
							final Integer mailboxID = Integer.valueOf(args[1]);
							if(DatabaseModule.Virtual.isMaiboxOwner(player.getUniqueId(), mailboxID) && !DatabaseModule.Virtual.isMailboxDeleted(mailboxID)) {
								MailboxModule.log(player.getUniqueId(), null, MailboxModule.Action.OPENED, mailboxID, null, null, null);
								switch(DatabaseModule.Virtual.getStatus(mailboxID)) {
									case ACCEPTED:
										player.openInventory(MailboxInventory.getInventory(MailboxInventory.Type.IN, mailboxID, null, DatabaseModule.Virtual.getMailbox(mailboxID), DatabaseModule.Virtual.getMailboxFrom(mailboxID), DatabaseModule.Virtual.getMailboxLabel(mailboxID), false));
										break;
									case PENDING:
										player.openInventory(MailboxInventory.getInventory(MailboxInventory.Type.IN_PENDING, mailboxID, null, DatabaseModule.Virtual.getMailbox(mailboxID), DatabaseModule.Virtual.getMailboxFrom(mailboxID), DatabaseModule.Virtual.getMailboxLabel(mailboxID), false));
										break;
									case UNDEFINED:
										player.sendMessage(DatabaseModule.Virtual.Status.class.getName() + ": " + DatabaseModule.Virtual.Status.UNDEFINED.toString());
										break;
								}
							}
						} catch (final NumberFormatException e) {
							player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_IDERROR));
						}
					} else player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
				} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_ERROR));
			}
		} else if(args[0].equalsIgnoreCase("list")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_LIST)) {
				if(sender instanceof Player) {
					final Player player = (Player) sender;
					if(args.length == 1) {
						final HashMap<Integer, String> mailboxes = DatabaseModule.Virtual.getMailboxesList(player.getUniqueId());
						if(mailboxes.size() > 0) {
							for(final Integer mailboxID : mailboxes.keySet()) {
								String from = "CONSOLE";
								final UUID uuidFrom = DatabaseModule.Virtual.getMailboxFrom(mailboxID);
								if(uuidFrom != null) from = Bukkit.getOfflinePlayer(uuidFrom).getName();
								String label = "";
								if(!DatabaseModule.Virtual.getMailboxLabel(mailboxID).isEmpty()) label = ": " + DatabaseModule.Virtual.getMailboxLabel(mailboxID);
								player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_NAME) + " #" + mailboxID + "" + ChatColor.RESET + " <= " + mailboxes.get(mailboxID) + " (@" + from + ")" + label);
							}
						} else player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_NONEW));
					} else player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
				} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_ERROR));
			}
		} else if(args[0].equalsIgnoreCase("delete")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_DELETE)) {
				if(sender instanceof Player) {
					final Player player = (Player) sender;
					if(args.length == 2) {
						try {
							final Integer mailboxID = Integer.valueOf(args[1]);
							if(DatabaseModule.Virtual.isMaiboxOwner(player.getUniqueId(), mailboxID) && !DatabaseModule.Virtual.isMailboxDeleted(mailboxID)) {
								DatabaseModule.Virtual.setMailboxDeleted(mailboxID);
								MailboxModule.log(player.getUniqueId(), null, MailboxModule.Action.DELETED, mailboxID, null, null, null);
								player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_DELETED) + " " + (String) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_NAME) + " #" + mailboxID);
							}
						} catch (final NumberFormatException e) {
							player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_IDERROR));
						}
					} else player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
				} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_ERROR));
			}
		} else if(args[0].equalsIgnoreCase("costs")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_SEND)) {
				if(args.length == 1) {
					if(IntegrationModule.getInstance().isInstalled(IntegrationModule.Name.VAULT)) {
						String costs = null;
						if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_TYPE_COST)) {
							costs = (Double) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_COST) + " x Item";
						} else costs = (Double) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_COST) + " x " + (String) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_NAME);
						if(costs != null) sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.TRANSACTION_COSTS) + " " + costs);
					} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.TRANSACTION_NOTINSTALLED));
				} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
			}
		} else if(args[0].equalsIgnoreCase("info")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_INFO)) {
				if(sender instanceof Player) {
					final Player player = (Player) sender;
					final LTPlayer ltPlayer = LTPlayer.fromUUID(player.getUniqueId());
					if(args.length == 1) {
						if(!ltPlayer.isRegistered()) DatabaseModule.User.register(ltPlayer.getUniqueId());
						player.sendMessage("");
						player.sendMessage(ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_REGISTRY) + " " + ltPlayer.getRegistryDate());
						String banned = ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_BANNED_NO);
						String banreason = "";
						if(ltPlayer.isBanned()) {
							banned = ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_BANNED_YES);
							banreason = ltPlayer.getBanReason();
						}
						player.sendMessage(ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_BANNED_MAIN) + " " + banned);
						if(banreason != null && !banreason.isEmpty()) player.sendMessage(ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_BANNED_REASON) + " " + banreason);
						player.sendMessage(ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_SENT) + " " + ltPlayer.getMailSentCount());
						player.sendMessage(ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_RECEIVED) + " " + ltPlayer.getMailReceivedCount());
						player.sendMessage("");
					} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
				} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_ERROR));
			}
		} else if(args[0].equalsIgnoreCase("color")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_COLOR)) {
				if(sender instanceof Player) {
					final Player player = (Player) sender;
					final LinkedList<String> colors = new LinkedList<>();
					for(Material color : Material.values()) if(color.toString().endsWith("_SHULKER_BOX")) colors.add(color.toString().replace("_SHULKER_BOX", "").toLowerCase());
					colors.sort(Comparator.naturalOrder());
					if(args.length == 2) {
						final String color = args[1].toLowerCase();
						final ItemStack current = player.getInventory().getItemInMainHand();
						final Item mailbox = new MailboxItem();
						if(colors.contains(color) && current != null && current.getItemMeta() != null && current.getType().toString().endsWith("_SHULKER_BOX") && current.getItemMeta().getLore() != null && current.getItemMeta().getLore().size() == 1 && current.getItemMeta().getLore().get(0).contains(mailbox.getDescription(null).get(0)) && current.getItemMeta().getLore().get(0).split("@").length == 2) {
							final ItemStack newMailbox = new ItemStack(Material.getMaterial(color.toUpperCase() + "_SHULKER_BOX"));
							newMailbox.setAmount(current.getAmount());
							newMailbox.setItemMeta(current.getItemMeta());
							current.setAmount(0);
							player.getInventory().setItemInMainHand(newMailbox);
						}
					} else if(args.length == 1) {
						String colorString = "";
						for(final String color : colors) if(colors.getLast().equals(color)) {
							colorString = colorString + ChatColor.WHITE + color + ChatColor.YELLOW + ".";
						} else colorString = colorString + ChatColor.WHITE + color + ChatColor.YELLOW + ", ";
						player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "Colors available: " + colorString);
					} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_SYNTAXERROR));
				} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_ERROR));
			}
		} else if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_MAIN)) {
			final String[] invalidCmd = LanguageModule.get(LanguageModule.Type.COMMAND_INVALID).split("%");
			sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + invalidCmd[0] + ChatColor.GREEN + "/itemmail " + ChatColor.YELLOW + invalidCmd[1]);
		}
		if(!hasPermission) sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_PERMISSIONERROR));
		return true;
	}
}
