package br.net.gmj.nobookie.LTItemMail.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import br.net.gmj.nobookie.LTItemMail.LTItemMail;
import br.net.gmj.nobookie.LTItemMail.block.MailboxBlock;
import br.net.gmj.nobookie.LTItemMail.entity.LTPlayer;
import br.net.gmj.nobookie.LTItemMail.inventory.MailboxInventory;
import br.net.gmj.nobookie.LTItemMail.module.ConfigurationModule;
import br.net.gmj.nobookie.LTItemMail.module.DataModule;
import br.net.gmj.nobookie.LTItemMail.module.DatabaseModule;
import br.net.gmj.nobookie.LTItemMail.module.EconomyModule;
import br.net.gmj.nobookie.LTItemMail.module.LanguageModule;
import br.net.gmj.nobookie.LTItemMail.module.MailboxModule;
import br.net.gmj.nobookie.LTItemMail.module.PermissionModule;
import br.net.gmj.nobookie.LTItemMail.util.BukkitUtil;
import br.net.gmj.nobookie.LTItemMail.util.TabUtil;

@LTCommandInfo(
		name = "ltitemmail:itemmail",
		description = "Used to send items and to check your mailbox.",
		aliases = "itemmail,ima,imail",
		permission = "ltitemmail.player",
		usage = "<command> [help|version|list|open|delete|info|price|color|blocks]")
public final class ItemMailCommand extends LTCommandExecutor {
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
				sender.sendMessage(ChatColor.AQUA + "=+=+=+= [LT Item Mail] =+=+=+=");
				sender.sendMessage(ChatColor.GREEN + "/itemmail help " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_ITEMMAIL));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_VERSION)) sender.sendMessage(ChatColor.GREEN + "/itemmail version " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_VERSION));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_LIST)) sender.sendMessage(ChatColor.GREEN + "/itemmail list " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_LIST));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_OPEN)) sender.sendMessage(ChatColor.GREEN + "/itemmail open <id> " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_OPEN));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_DELETE)) sender.sendMessage(ChatColor.GREEN + "/itemmail delete <id> " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_DELETE));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_PRICE)) sender.sendMessage(ChatColor.GREEN + "/itemmail price " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_COSTS));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_INFO)) sender.sendMessage(ChatColor.GREEN + "/itemmail info " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_MAIN));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_BLOCKS)) sender.sendMessage(ChatColor.GREEN + "/itemmail blocks " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_BLOCKS));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_COLOR)) sender.sendMessage(ChatColor.GREEN + "/itemmail color <color> " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_COLOR));
				if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_SEND)) sender.sendMessage(ChatColor.GREEN + "/mailitem <player> [label] " + ChatColor.AQUA + "- " + LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_MAILITEM));
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
							final Integer mailboxID = Integer.valueOf(args[1].replace("#", ""));
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
								if(uuidFrom != null) from = LTPlayer.fromUUID(uuidFrom).getName();
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
							final Integer mailboxID = Integer.valueOf(args[1].replace("#", ""));
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
		} else if(args[0].equalsIgnoreCase("price")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_PRICE)) {
				if(args.length == 1) {
					if(EconomyModule.getInstance() != null) {
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
						if(!ltPlayer.isRegistered()) DatabaseModule.User.register(ltPlayer);
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
						if(colors.contains(color) && current != null && current.getItemMeta() != null && current.getType().toString().endsWith("_SHULKER_BOX") && BukkitUtil.DataContainer.isMailbox(current)) {
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
		} else if(args[0].equalsIgnoreCase("blocks")) {
			if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_BLOCKS)) {
				if(sender instanceof Player) {
					final Player player = (Player) sender;
					final List<MailboxBlock> mailboxes = new ArrayList<>();
					for(final MailboxBlock block : DatabaseModule.Block.getMailboxBlocks()) if(block.getOwner().equals(player.getUniqueId())) mailboxes.add(block);
					if(mailboxes.size() > 0) {
						player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.BLOCK_LIST));
						Integer number = 1;
						for(final MailboxBlock block : mailboxes) {
							final Location loc = block.getLocation();
							player.sendMessage(ChatColor.YELLOW + "    - #" + ChatColor.GREEN + String.valueOf(number) + ChatColor.YELLOW + " : " + LanguageModule.get(LanguageModule.Type.BLOCK_LIST_WORLD) + "=" + ChatColor.GREEN + loc.getWorld().getName() + ChatColor.YELLOW + ", X=" + ChatColor.GREEN + String.valueOf(loc.getBlockX()) + ChatColor.YELLOW + ", Y=" + ChatColor.GREEN + String.valueOf(loc.getBlockY()) + ChatColor.YELLOW + ", Z=" + ChatColor.GREEN + String.valueOf(loc.getBlockZ()));
							number++;
						}
					}
				} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + LanguageModule.get(LanguageModule.Type.PLAYER_ERROR));
			}
		} else if(hasPermission = PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_MAIN)) {
			final String[] invalidCmd = LanguageModule.get(LanguageModule.Type.COMMAND_INVALID).split("%");
			sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + invalidCmd[0] + ChatColor.GREEN + "/itemmail " + ChatColor.YELLOW + invalidCmd[1]);
		}
		if(!hasPermission) sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.PLAYER_PERMISSIONERROR));
		return true;
	}
	@Override
	public final List<String> onTabComplete(final CommandSender sender, final Command cmd, final String alias, final String[] args){
		if(args.length == 1) {
			final List<String> commands  = new ArrayList<>();
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_MAIN)) commands.add("help");
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_VERSION)) commands.add("version");
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_LIST)) commands.add("list");
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_OPEN)) commands.add("open");
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_DELETE)) commands.add("delete");
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_INFO)) commands.add("info");
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_PRICE)) commands.add("price");
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_COLOR)) commands.add("color");
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_BLOCKS)) commands.add("blocks");
			return TabUtil.partial(args[0], commands);
		}
		if(args.length == 2) {
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_OPEN) || PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_DELETE)) if(args[0].equals("open") || args[0].equals("delete")) if(sender instanceof Player) {
				final Player player = (Player) sender;
				final HashMap<Integer, String> mailboxes = DatabaseModule.Virtual.getMailboxesList(player.getUniqueId());
				final LinkedList<String> response = new LinkedList<>();
				for(final Integer i : mailboxes.keySet()) response.add(String.valueOf(i));
				response.sort(Comparator.naturalOrder());
				return TabUtil.partial(args[1], response);
			}
			if(PermissionModule.hasPermission(sender, PermissionModule.Type.CMD_PLAYER_COLOR) && args[0].equals("color")) {
				final LinkedList<String> colors = new LinkedList<>();
				for(Material color : Material.values()) if(color.toString().endsWith("_SHULKER_BOX")) colors.add(color.toString().replace("_SHULKER_BOX", "").toLowerCase());
				colors.sort(Comparator.naturalOrder());
				return TabUtil.partial(args[1], colors);
			}
		}
		return Collections.emptyList();
	}
}
