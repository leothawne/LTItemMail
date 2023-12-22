package io.github.leothawne.LTItemMail.command;

import java.util.HashMap;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.api.HTTP;
import io.github.leothawne.LTItemMail.inventory.MailboxInventory;
import io.github.leothawne.LTItemMail.module.DataModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.MailboxLogModule;
import io.github.leothawne.LTItemMail.type.MailboxType;

public final class ItemMailAdminCommand implements CommandExecutor {
	public ItemMailAdminCommand() {}
	@Override
	public final boolean onCommand(final CommandSender sender, final Command cmd, final String commandLabel, final String[] args) {
		if(sender.hasPermission("LTItemMail.admin")) {
			if(args.length == 0) {
				sender.sendMessage(ChatColor.AQUA + "=+=+=+= [LT Item Mail :: Admin] =+=+=+=");
				sender.sendMessage(ChatColor.GREEN + "/itemmailadmin " + ChatColor.AQUA + "- Commands for administrators.");
				sender.sendMessage(ChatColor.GREEN + "/itemmailadmin update " + ChatColor.AQUA + "- Check for new updates.");
				sender.sendMessage(ChatColor.GREEN + "/itemmailadmin list <player> " + ChatColor.AQUA + "- List opened mailboxes of a specific player.");
				sender.sendMessage(ChatColor.GREEN + "/itemmailadmin recover <mailbox id> " + ChatColor.AQUA + "- Recover lost items (if there is any).");
				sender.sendMessage(ChatColor.YELLOW + "You can also use "+ ChatColor.GREEN + "/itemmailadmin "+ ChatColor.YELLOW + "as "+ ChatColor.GREEN + "/imad"+ ChatColor.YELLOW + ".");
			} else if(args[0].equalsIgnoreCase("update")) {
				if(args.length == 1) {
					final CommandSender finalSender = sender;
					new BukkitRunnable() {
						@Override
						public final void run() {
							String[] LocalVersion = LTItemMail.getInstance().getDescription().getVersion().split("\\.");
							int Local_VersionNumber1 = Integer.parseInt(LocalVersion[0]);
							int Local_VersionNumber2 = Integer.parseInt(LocalVersion[1]);
							int Local_VersionNumber3 = Integer.parseInt(LocalVersion[2]);
							String[] Server1 = HTTP.getData(DataModule.getUpdateURL()).split("-");
							String[] Server2 = Server1[0].split("\\.");
							int Server2_VersionNumber1 = Integer.parseInt(Server2[0]);
							int Server2_VersionNumber2 = Integer.parseInt(Server2[1]);
							int Server2_VersionNumber3 = Integer.parseInt(Server2[2]);
							String updateMessage = ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "A newer version is available: " + ChatColor.GREEN + Server1[0] + ChatColor.YELLOW + " (released on " + ChatColor.GREEN + Server1[1] + ChatColor.YELLOW + ").";
							if(Server2_VersionNumber1 > Local_VersionNumber1) {
								finalSender.sendMessage(updateMessage);
							} else if(Server2_VersionNumber1 == Local_VersionNumber1 && Server2_VersionNumber2 > Local_VersionNumber2) {
								finalSender.sendMessage(updateMessage);
							} else if(Server2_VersionNumber1 == Local_VersionNumber1 && Server2_VersionNumber2 == Local_VersionNumber2 && Server2_VersionNumber3 > Local_VersionNumber3) {
								finalSender.sendMessage(updateMessage);
							} else finalSender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "The plugin is up to date!");
						}
					}.runTaskAsynchronously(LTItemMail.getInstance());
				} else sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "Too many arguments!");
			} else if(args[0].equalsIgnoreCase("list") && sender instanceof Player) {
				final Player player = (Player) sender;
				if(args.length == 2) {
					@SuppressWarnings("deprecation")
					final OfflinePlayer offPlayer = Bukkit.getOfflinePlayer(args[1]);
					final HashMap<Integer, String> mailboxes = DatabaseModule.Function.getOpenedMailboxesList(offPlayer.getUniqueId());
					if(mailboxes.size() > 0) {
						player.sendMessage(offPlayer.getName() + "'s opened mailboxes:");
						for(final Integer mailboxID : mailboxes.keySet()) {
							String x = "";
							final LinkedList<ItemStack> items = DatabaseModule.Function.getMailbox(mailboxID);
							if(items.size() == 0) x = " [Empty]";
							player.sendMessage("Mailbox #" + mailboxID + " : " + mailboxes.get(mailboxID) + x); 
						}
					} else player.sendMessage("No opened mailboxes for player " + offPlayer.getName() + ".");
				}
			} else if(args[0].equalsIgnoreCase("recover") && sender instanceof Player) {
				final Player player = (Player) sender;
				if(args.length == 2) try {
					final Integer mailboxID = Integer.valueOf(args[1]);
					final LinkedList<ItemStack> items = DatabaseModule.Function.getMailbox(mailboxID);
					if(items.size() > 0) {
						player.openInventory(MailboxInventory.getMailboxInventory(MailboxType.IN, mailboxID, null, items));
						MailboxLogModule.log(player.getUniqueId(), null, MailboxLogModule.ActionType.RECOVERED, mailboxID);
					} else player.sendMessage("There is no lost items on this mailbox.");
				} catch (final NumberFormatException e) {
					player.sendMessage("Mailbox ID must be a number!");
				}
			} else sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "Invalid command! Type " + ChatColor.GREEN + "/itemmailadmin " + ChatColor.YELLOW + "to see all available commands.");
		} else {
			sender.sendMessage(ChatColor.AQUA + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + " :: Admin] " + ChatColor.YELLOW + "" + LanguageModule.get("no-permission"));
			LTItemMail.getInstance().getConsole().severe(sender.getName() + " does not have permission [LTItemMail.admin].");
		}
		return true;
	}
}
