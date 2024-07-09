package io.github.leothawne.LTItemMail.module;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.LTPlayer;
import io.github.leothawne.LTItemMail.util.Toasts;
import net.md_5.bungee.api.ChatColor;

public final class MailboxModule {
	private static final void write(final String content) {
		try {
			Files.createDirectories(Paths.get(LTItemMail.getInstance().getDataFolder() + File.separator + "logs"));
			final BufferedWriter writer = new BufferedWriter(new FileWriter(LTItemMail.getInstance().getDataFolder() + File.separator + "logs" + File.separator + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()) + ".log", true));
			writer.write("[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + "] " + content);
			writer.newLine();
			writer.close();
		} catch (final IOException e) {
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) ConsoleModule.mailbox(content);
	}
	public static final boolean log(final UUID from, UUID playerTo, final Action action, final Integer mailboxID, final Double mailboxCost, final LinkedList<ItemStack> contents, final Location mailboxBlock) {
		if(from == null || action == null) return false;
		String log = LTPlayer.fromUUID(from).getName() + " ";
		String contentString = "";
		switch(action) {
			case CANCELED:
				log = log + "prevented from sending a mailbox";
				break;
			case GAVE_BACK:
				log = log + "gave back Mailbox#" + mailboxID + " to " + LTPlayer.fromUUID(playerTo).getName();
				break;
			case PAID:
				log = log + "paid $" + mailboxCost;
				break;
			case REFUNDED:
				log = log + "was refunded $" + mailboxCost;
				break;
			case RECOVERED:
				log = log + "recovered lost items of Mailbox#" + mailboxID;
				break;
			case DELETED:
				log = log + "deleted Mailbox#" + mailboxID;
				break;
			case OPENED:
				log = log + "opened Mailbox#" + mailboxID;
				break;
			case RECEIVED:
				if(contents != null) {
					for(final ItemStack content : contents) if(content != null && !content.getType().equals(Material.AIR)) {
						String itemName = "";
						if(content.hasItemMeta()) {
							itemName = content.getItemMeta().getDisplayName();
						} else itemName = content.getType().toString();
						if(content.equals(contents.getLast())) {
							contentString = contentString + itemName + "[" + content.getAmount() + "]";
						} else contentString = contentString + itemName + "[" + content.getAmount() + "], ";
					}
				} else contentString = "#BungeeLimitation";
				log = log + "received Mailbox#" + mailboxID + " (Contents: " + contentString + ")";
				break;
			case SENT:
				if(contents != null) {
					for(final ItemStack content : contents) if(content != null && !content.getType().equals(Material.AIR)) {
						String itemName = "";
						if(content.hasItemMeta()) {
							itemName = content.getItemMeta().getDisplayName();
						} else itemName = content.getType().toString();
						if(content.equals(contents.getLast())) {
							contentString = contentString + itemName + "[" + content.getAmount() + "]";
						} else contentString = contentString + itemName + "[" + content.getAmount() + "], ";
					}
				} else contentString = "#BungeeLimitation";
				log = log + "sent to " + LTPlayer.fromUUID(playerTo).getName() + ": Mailbox#" + mailboxID + " (Contents: " + contentString + ")";
				break;
			case PLACED:
				log = log + "placed a mailbox at X: " + mailboxBlock.getBlockX() + ", Y: " + mailboxBlock.getBlockY() + ", Z: " + mailboxBlock.getBlockZ();
				break;
			case BROKE:
				log = log + "broke a mailbox at X: " + mailboxBlock.getBlockX() + ", Y: " + mailboxBlock.getBlockY() + ", Z: " + mailboxBlock.getBlockZ();
				break;
			case ADMIN_BROKE:
				log = log + "broke the mailbox of " + LTPlayer.fromUUID(playerTo).getName() + " at X: " + mailboxBlock.getBlockX() + ", Y: " + mailboxBlock.getBlockY() + ", Z: " + mailboxBlock.getBlockZ();
				break;
		}
		write(log);
		return true;
	}
	public static final int send(final CommandSender sender, final LTPlayer receiver, final LinkedList<ItemStack> contentsarray, final String label) {
		Player senderPlayer = null;
		if(sender instanceof Player) senderPlayer = (Player) sender;
		UUID senderPlayerID = null;
		if(senderPlayer != null) senderPlayerID = senderPlayer.getUniqueId();
		final Integer mailboxID = DatabaseModule.Virtual.saveMailbox(senderPlayerID, receiver.getUniqueId(), contentsarray, label);
		log(senderPlayerID, receiver.getUniqueId(), Action.SENT, mailboxID, null, contentsarray, null);
		final String[] mailboxSent = LanguageModule.get(LanguageModule.Type.MAILBOX_SENT).split("%");
		sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + mailboxSent[0] + "" + ChatColor.AQUA + "" + receiver.getName() + "" + ChatColor.YELLOW + "" + mailboxSent[1]);
		if(receiver.getBukkitPlayer().getPlayer() != null) {
			final Player bukkitReceiver = receiver.getBukkitPlayer().getPlayer();
			log(receiver.getUniqueId(), null, Action.RECEIVED, mailboxID, null, contentsarray, null);
			MailboxModule.Display display;
			try {
				display = MailboxModule.Display.valueOf(((String) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_DISPLAY)).toUpperCase());
			} catch(final IllegalArgumentException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) {
					ConsoleModule.severe("New mail display must be CHAT, TITLE or TOAST");
					e.printStackTrace();
				}
				display = MailboxModule.Display.CHAT;
			}
			switch(display) {
				case CHAT:
					bukkitReceiver.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_FROM) + " " + ChatColor.GREEN + "" + sender.getName());
					break;
				case TITLE:
					bukkitReceiver.sendTitle(ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_FROM) +  " " + ChatColor.GREEN, sender.getName(), 20 * 1, 20 * 5, 20 * 1);
					break;
				case TOAST:
					Toasts.display(receiver, LanguageModule.get(LanguageModule.Type.MAILBOX_FROM) + " " + sender.getName(), Toasts.Type.MAILBOX);
					if(!label.isEmpty()) Bukkit.getScheduler().runTaskLater(LTItemMail.getInstance(), new Runnable() {
						@Override
						public final void run() {
							Toasts.display(receiver, label, Toasts.Type.MAILBOX);
						}
					}, 20 * 3);
					break;
			}
		} else if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.BUNGEE_MODE)) {
			final ByteArrayDataOutput bungee = ByteStreams.newDataOutput();
			if(senderPlayer != null) {
				bungee.writeUTF("Forward");
				bungee.writeUTF("ALL");
				bungee.writeUTF("LTItemMail_MailboxReceived");
				final ByteArrayDataOutput function = ByteStreams.newDataOutput();
				final String data = senderPlayer.getName() + ";" + receiver.getName() + ";" + mailboxID;
				function.writeUTF(data);
				bungee.writeShort(function.toByteArray().length);
				bungee.write(function.toByteArray());
			} else {
				bungee.writeUTF("Message");
				bungee.writeUTF(receiver.getName());
				bungee.writeUTF((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_FROM) + " " + ChatColor.GREEN + "" + sender.getName());
			}
			Bukkit.getServer().sendPluginMessage(LTItemMail.getInstance(), "BungeeCord", bungee.toByteArray());
		}
		return mailboxID;
	}
	public enum Action {
		PAID,
		REFUNDED,
		RECOVERED,
		DELETED,
		OPENED,
		RECEIVED,
		SENT,
		PLACED,
		BROKE,
		ADMIN_BROKE,
		CANCELED,
		GAVE_BACK
	}
	public enum Display {
		CHAT,
		TITLE,
		TOAST
	}
}