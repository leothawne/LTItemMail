package io.github.leothawne.LTItemMail.listener;

import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.inventory.MailboxInventory;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.MailboxLogModule;
import net.milkbowl.vault.economy.EconomyResponse;

public final class MailboxListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onInventoryClose(final InventoryCloseEvent event) {
		final Player player = (Player) event.getPlayer();
		final Inventory inventory = event.getInventory();
		final InventoryView inventoryView = player.getOpenInventory();
		if(inventoryView.getTitle().contains(MailboxInventory.getMailboxName(MailboxInventory.Type.IN, null, null)) && inventoryView.getTitle().split("#").length == 2) {
			final Integer mailboxID = Integer.valueOf(inventoryView.getTitle().split("#")[1]);
			final ItemStack[] contents = inventory.getContents();
			Boolean isEmpty = true;
			for(final ItemStack content : contents) if(content != null) isEmpty = false;
			if(!isEmpty) {
				final LinkedList<ItemStack> items = new LinkedList<>();
				for(final ItemStack content: contents) {
					if(content != null) {
						items.add(content);
					} else items.add(new ItemStack(Material.AIR));
				}
				DatabaseModule.Function.updateMailbox(mailboxID, items);
			} else DatabaseModule.Function.updateMailbox(mailboxID, new LinkedList<ItemStack>());
			player.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_CLOSED));
			inventory.clear();
		} else if(inventoryView.getTitle().contains(MailboxInventory.getMailboxName(MailboxInventory.Type.OUT, null, null)) && inventoryView.getTitle().split("@").length == 2) {
			final Player sender = (Player) event.getPlayer();
			final Player recipient = (Player) inventory.getHolder();
			final ItemStack[] contents = inventory.getContents();
			final LinkedList<ItemStack> contentsarray = new LinkedList<ItemStack>();
			for(final ItemStack content : contents) if(content == null) {
				contentsarray.add(new ItemStack(Material.AIR));
			} else contentsarray.add(content);
			Boolean isEmpty = true;
			int count = 0;
			for(final ItemStack content : contentsarray) if(content.getType() != Material.AIR) {
				isEmpty = false;
				count = count + content.getAmount();
			}
			inventory.clear();
			if(!isEmpty) {
				double newcost = 0;
				if(LTItemMail.getInstance().getConfiguration().getBoolean("cost-per-item")) {
					newcost = LTItemMail.getInstance().getConfiguration().getDouble("mail-cost") * count;
				} else newcost = LTItemMail.getInstance().getConfiguration().getDouble("mail-cost");
				if(LTItemMail.getInstance().getEconomy() != null) {
					if(LTItemMail.getInstance().getEconomy().has(sender, newcost)) {
						final EconomyResponse er = LTItemMail.getInstance().getEconomy().withdrawPlayer(sender, newcost);
						if(er.transactionSuccess()) {
							final String[] mailboxPaid = LanguageModule.get(LanguageModule.Type.TRANSACTION_PAID).split("%");
							sender.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + mailboxPaid[0] + "" + ChatColor.GREEN + newcost + "" + ChatColor.YELLOW + "" + mailboxPaid[1]);
							sendBox(sender, recipient, contentsarray);
						} else {
							sender.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.TRANSACTION_ERROR));
							for(final ItemStack item : contentsarray) sender.getInventory().addItem(item);
						}
					} else {
						final String[] transactionNoMoney = LanguageModule.get(LanguageModule.Type.TRANSACTION_NOMONEY).split("%");
						sender.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + transactionNoMoney[0] + "" + ChatColor.GREEN + newcost + "" + ChatColor.YELLOW + "" + transactionNoMoney[1]);
						for(final ItemStack item : contentsarray) sender.getInventory().addItem(item);
					}
				} else sendBox(sender, recipient, contentsarray);
			} else sender.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_ABORTED));
		}
	}
	private static final void sendBox(final CommandSender sender, final Player recipient, final LinkedList<ItemStack> contentsarray) {
		final String[] mailboxSent = LanguageModule.get(LanguageModule.Type.MAILBOX_SENT).split("%");
		sender.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.YELLOW + "" + mailboxSent[0] + "" + ChatColor.AQUA + "" + recipient.getName() + "" + ChatColor.YELLOW + "" + mailboxSent[1]);
		if(LTItemMail.getInstance().getConfiguration().getBoolean("use-title")) {
			recipient.sendTitle(ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_FROM) +  " " + ChatColor.GREEN + "" + sender.getName(), "", 20 * 1, 20 * 5, 20 * 1);
		} else recipient.sendMessage(ChatColor.DARK_GREEN + "[" + LTItemMail.getInstance().getConfiguration().getString("plugin-tag") + "] " + ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_FROM) + " " + ChatColor.GREEN + "" + sender.getName());
		Player senderPlayer = null;
		if(sender instanceof Player) senderPlayer = (Player) sender;
		UUID senderPlayerID = null;
		if(senderPlayer != null) senderPlayerID = senderPlayer.getUniqueId();
		final Integer mailboxID = DatabaseModule.Function.saveMailbox(senderPlayerID, recipient.getUniqueId(), contentsarray);
		if(senderPlayer != null) MailboxLogModule.log(senderPlayer.getUniqueId(), recipient.getUniqueId(), MailboxLogModule.Action.SENT, mailboxID);
		MailboxLogModule.log(recipient.getUniqueId(), null, MailboxLogModule.Action.RECEIVED, mailboxID);
	}
}