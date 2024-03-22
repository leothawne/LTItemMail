package io.github.leothawne.LTItemMail.listener;

import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.event.PlayerSendMailEvent;
import io.github.leothawne.LTItemMail.inventory.MailboxInventory;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.MailboxModule;
import io.github.leothawne.LTItemMail.module.integration.IntegrationModule;
import net.md_5.bungee.api.ChatColor;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;

public final class MailboxListener implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public final void onInventoryClose(final InventoryCloseEvent event) {
		final Player player = (Player) event.getPlayer();
		final Inventory inventory = event.getInventory();
		final InventoryView inventoryView = player.getOpenInventory();
		if(inventoryView.getTitle().contains(MailboxInventory.getMailboxName(MailboxInventory.Type.IN, null, null)) && inventoryView.getTitle().split("#").length == 2) {
			final Integer mailboxID = Integer.valueOf(inventoryView.getTitle().split("#")[1]);
			final ItemStack[] contents = inventory.getContents();
			Boolean isEmpty = true;
			for(int i = 0; i < 27; i++) if(contents[i] != null) isEmpty = false;
			if(!isEmpty) {
				final LinkedList<ItemStack> items = new LinkedList<>();
				for(int i = 0; i < 27; i++) {
					if(contents[i] != null) {
						items.add(contents[i]);
					} else items.add(new ItemStack(Material.AIR));
				}
				DatabaseModule.Virtual.updateMailbox(mailboxID, items);
			} else DatabaseModule.Virtual.updateMailbox(mailboxID, new LinkedList<ItemStack>());
			player.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_CLOSED));
			inventory.clear();
		} else if(inventoryView.getTitle().contains(MailboxInventory.getMailboxName(MailboxInventory.Type.OUT, null, null)) && inventoryView.getTitle().split("@").length == 2) {
			final Player sender = (Player) event.getPlayer();
			final OfflinePlayer receiver = Bukkit.getOfflinePlayer(inventoryView.getTitle().split("@")[1]);
			final ItemStack[] contents = inventory.getContents();
			final LinkedList<ItemStack> contentsarray = new LinkedList<ItemStack>();
			for(int i = 0; i < 27; i++) if(contents[i] != null) {
				contentsarray.add(contents[i]);
			} else contentsarray.add(new ItemStack(Material.AIR));
			Boolean isEmpty = true;
			int count = 0;
			for(int i = 0; i < 27; i++) if(contentsarray.get(i).getType() != Material.AIR) {
				isEmpty = false;
				count = count + contentsarray.get(i).getAmount();
			}
			inventory.clear();
			if(!isEmpty) {
				final String label = contents[31].getItemMeta().getDisplayName();
				double newcost = 0;
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_TYPE_COST)) {
					newcost = (Double) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_COST) * count;
				} else newcost = (Double) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_COST);
				final Economy eco = (Economy) IntegrationModule.getInstance().get(IntegrationModule.FPlugin.VAULT_ECONOMY);
				if(eco != null) {
					if(eco.has(sender, newcost)) {
						final EconomyResponse er = eco.withdrawPlayer(sender, newcost);
						if(er.transactionSuccess()) {
							final PlayerSendMailEvent sendEvent = new PlayerSendMailEvent(sender, receiver, contentsarray, true, newcost);
							Bukkit.getPluginManager().callEvent(sendEvent);
							if(!sendEvent.isCancelled()) {
								final String[] mailboxPaid = LanguageModule.get(LanguageModule.Type.TRANSACTION_PAID).split("%");
								sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + mailboxPaid[0] + "" + ChatColor.GREEN + newcost + "" + ChatColor.YELLOW + "" + mailboxPaid[1]);
								sendBox(sender, receiver, contentsarray, label);
							} else {
								sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_BLOCKED));
								sendBox(Bukkit.getConsoleSender(), sender, contentsarray, LanguageModule.get(LanguageModule.Type.MAILBOX_BLOCKED) + " (@" + sender.getName() + " => @" + receiver.getName() + ")");
							}
						} else {
							sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.TRANSACTION_ERROR));
							sendBox(Bukkit.getConsoleSender(), sender, contentsarray, label);
						}
					} else {
						final String[] transactionNoMoney = LanguageModule.get(LanguageModule.Type.TRANSACTION_NOMONEY).split("%");
						sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + transactionNoMoney[0] + "" + ChatColor.GREEN + newcost + "" + ChatColor.YELLOW + "" + transactionNoMoney[1]);
						sendBox(Bukkit.getConsoleSender(), sender, contentsarray, label);
					}
				} else {
					final PlayerSendMailEvent sendEvent = new PlayerSendMailEvent(sender, receiver, contentsarray, false, 0);
					Bukkit.getPluginManager().callEvent(sendEvent);
					if(!sendEvent.isCancelled()) {
						sendBox(sender, receiver, contentsarray, label);
					} else {
						sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_BLOCKED));
						sendBox(Bukkit.getConsoleSender(), sender, contentsarray, LanguageModule.get(LanguageModule.Type.MAILBOX_BLOCKED) + " (@" + sender.getName() + " => @" + receiver.getName() + ")");
					}
				}
			} else sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_ABORTED));
		}
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	private final void onInventoryClick(final InventoryClickEvent event) {
		final Player player = (Player) event.getWhoClicked();
		final InventoryView inventoryView = player.getOpenInventory();
		if((inventoryView.getTitle().contains(MailboxInventory.getMailboxName(MailboxInventory.Type.IN, null, null)) && inventoryView.getTitle().split("#").length == 2) || (inventoryView.getTitle().contains(MailboxInventory.getMailboxName(MailboxInventory.Type.OUT, null, null)) && inventoryView.getTitle().split("@").length == 2)) if(event.getRawSlot() > 26 && event.getRawSlot() < 36) event.setCancelled(true);
	}
	private static final void sendBox(final CommandSender sender, final OfflinePlayer receiver, final LinkedList<ItemStack> contentsarray, final String label) {
		final String[] mailboxSent = LanguageModule.get(LanguageModule.Type.MAILBOX_SENT).split("%");
		sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + mailboxSent[0] + "" + ChatColor.AQUA + "" + receiver.getName() + "" + ChatColor.YELLOW + "" + mailboxSent[1]);
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_TITLE)) {
			if(receiver.getPlayer() != null) receiver.getPlayer().sendTitle(ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_FROM) +  " " + ChatColor.GREEN + "" + sender.getName(), "", 20 * 1, 20 * 5, 20 * 1);
		} else if(receiver.getPlayer() != null) receiver.getPlayer().sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_FROM) + " " + ChatColor.GREEN + "" + sender.getName());
		Player senderPlayer = null;
		if(sender instanceof Player) senderPlayer = (Player) sender;
		UUID senderPlayerID = null;
		if(senderPlayer != null) senderPlayerID = senderPlayer.getUniqueId();
		final Integer mailboxID = DatabaseModule.Virtual.saveMailbox(senderPlayerID, receiver.getUniqueId(), contentsarray, label);
		if(senderPlayer != null) MailboxModule.log(senderPlayer.getUniqueId(), receiver.getUniqueId(), MailboxModule.Action.SENT, mailboxID, null);
		MailboxModule.log(receiver.getUniqueId(), null, MailboxModule.Action.RECEIVED, mailboxID, null);
	}
}