package io.github.leothawne.LTItemMail.module;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
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

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.LTPlayer;
import net.md_5.bungee.api.ChatColor;

public final class MailboxModule {
	private static final void write(final String content) {
		try {
			Files.createDirectories(Paths.get(LTItemMail.getInstance().getDataFolder() + File.separator + "logs"));
			final BufferedWriter writer = new BufferedWriter(new FileWriter(LTItemMail.getInstance().getDataFolder() + File.separator + "logs" + File.separator + "mailboxes_" + DateTimeFormatter.ofPattern("yyyy-MM-dd").format(LocalDateTime.now()) + ".log", true));
			writer.write("[" + DateTimeFormatter.ofPattern("HH:mm:ss").format(LocalDateTime.now()) + "] " + content);
			writer.newLine();
			writer.close();
		} catch (final IOException e) {
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) ConsoleModule.mailbox(content);
	}
	/*private final Logger getLogger() {
		return log;
	}*/
	public static final boolean log(final UUID from, UUID playerTo, final Action action, final Integer mailboxID, final LinkedList<ItemStack> contents, final Location mailboxBlock) {
		if(from == null || action == null) return false;
		String log = LTPlayer.fromUUID(from).getName() + " ";
		String contentString = "";
		switch(action) {
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
				} else contentString = "#Bungee-LimitationError";
				log = log + "received Mailbox#" + mailboxID + " (Contents: " + contentString + ")";
				break;
			case SENT:
				String offlineFrom = "";
				String offlineTo = "";
				final LTPlayer ltPlayerFrom = LTPlayer.fromUUID(from);
				if(ltPlayerFrom.getBukkitPlayer().getPlayer() == null) offlineFrom = " (offline)";
				final LTPlayer ltPlayerTo = LTPlayer.fromUUID(playerTo);
				if(ltPlayerTo.getBukkitPlayer().getPlayer() == null) offlineTo = " (offline)";
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
				} else contentString = "#Bungee-LimitationError";
				log = log + offlineFrom + "sent to " + ltPlayerTo.getName() + offlineTo + ": Mailbox#" + mailboxID + " (Contents: " + contentString + ")";
				break;
			case PLACED:
				log = log + "placed a mailbox at X: " + mailboxBlock.getBlockX() + ", Y: " + mailboxBlock.getBlockY() + ", Z: " + mailboxBlock.getBlockZ();
				break;
			case BROKE:
				log = log + "broke a mailbox at X: " + mailboxBlock.getBlockX() + ", Y: " + mailboxBlock.getBlockY() + ", Z: " + mailboxBlock.getBlockZ();
				break;
			case ADMIN_BROKE:
				log = log + "broke the mailbox of " + Bukkit.getOfflinePlayer(playerTo).getName() + " at X: " + mailboxBlock.getBlockX() + ", Y: " + mailboxBlock.getBlockY() + ", Z: " + mailboxBlock.getBlockZ();
				break;
		}
		write(log);
		return true;
	}
	public static final void send(final CommandSender sender, final LTPlayer receiver, final LinkedList<ItemStack> contentsarray, final String label) {
		Player senderPlayer = null;
		if(sender instanceof Player) senderPlayer = (Player) sender;
		UUID senderPlayerID = null;
		if(senderPlayer != null) senderPlayerID = senderPlayer.getUniqueId();
		final Integer mailboxID = DatabaseModule.Virtual.saveMailbox(senderPlayerID, receiver.getUniqueId(), contentsarray, label);
		log(senderPlayerID, receiver.getUniqueId(), Action.SENT, mailboxID, contentsarray, null);
		final String[] mailboxSent = LanguageModule.get(LanguageModule.Type.MAILBOX_SENT).split("%");
		sender.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.YELLOW + "" + mailboxSent[0] + "" + ChatColor.AQUA + "" + receiver.getName() + "" + ChatColor.YELLOW + "" + mailboxSent[1]);
		if(receiver.getBukkitPlayer().getPlayer() != null) {
			log(receiver.getUniqueId(), null, Action.RECEIVED, mailboxID, contentsarray, null);
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_TITLE)) {
				receiver.getBukkitPlayer().getPlayer().sendTitle(ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_FROM) +  " " + ChatColor.GREEN + "" + sender.getName(), "", 20 * 1, 20 * 5, 20 * 1);
			} else receiver.getBukkitPlayer().getPlayer().sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_FROM) + " " + ChatColor.GREEN + "" + sender.getName());
		} else if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.BUNGEE_MODE)) {
			try {
				final ByteArrayOutputStream bungee = new ByteArrayOutputStream();
				final DataOutputStream bungeeOut = new DataOutputStream(bungee);
				//código pra obter o nome do servidor que o destinatário está e gravar no log
				// escrever aqui
				if(senderPlayer != null) {
					bungeeOut.writeUTF("Forward");
					bungeeOut.writeUTF("ALL");
					bungeeOut.writeUTF("LTItemMail_MailboxReceived");
					final ByteArrayOutputStream function = new ByteArrayOutputStream();
					final DataOutputStream functionOut = new DataOutputStream(function);
					String data = senderPlayer.getName() + ";" + receiver.getName() + ";" + mailboxID;
					functionOut.writeUTF(data);
					bungeeOut.writeShort(function.toByteArray().length);
					bungeeOut.write(function.toByteArray());
				} else {
					bungeeOut.writeUTF("Message");
					bungeeOut.writeUTF(receiver.getName());
					bungeeOut.writeUTF((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_FROM) + " " + ChatColor.GREEN + "" + sender.getName());
				}
				Bukkit.getServer().sendPluginMessage(LTItemMail.getInstance(), "BungeeCord", bungee.toByteArray());
			} catch (final IOException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
		}
	}
	public enum Action {
		RECOVERED,
		DELETED,
		OPENED,
		RECEIVED,
		SENT,
		PLACED,
		BROKE,
		ADMIN_BROKE
	}
}