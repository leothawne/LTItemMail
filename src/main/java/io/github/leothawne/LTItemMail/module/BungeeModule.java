package io.github.leothawne.LTItemMail.module;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.LTPlayer;
import io.github.leothawne.LTItemMail.module.MailboxModule.Action;
import net.md_5.bungee.api.ChatColor;

public final class BungeeModule implements PluginMessageListener {
	private static final List<String> onlinePlayers = new ArrayList<>();
	public static final List<String> getOnlinePlayers(){
		return onlinePlayers;
	}
	public BungeeModule() {
		new BukkitRunnable() {
			@Override
			public final void run() {
				try {
					final ByteArrayOutputStream bungee = new ByteArrayOutputStream();
					final DataOutputStream bungeeOut = new DataOutputStream(bungee);
					bungeeOut.writeUTF("PlayerList");
					bungeeOut.writeUTF("ALL");
					Bukkit.getServer().sendPluginMessage(LTItemMail.getInstance(), "BungeeCord", bungee.toByteArray());
				} catch (final IOException e) {
					if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
				}
			}
		}.runTaskTimer(LTItemMail.getInstance(), 0, 20);
	}
	@Override
	public final void onPluginMessageReceived(final String channel, final Player player, final byte[] message) {
		if(!channel.equals("BungeeCord")) return;
		final ByteArrayDataInput in = ByteStreams.newDataInput(message);
		final String subchannel = in.readUTF();
		try {
			switch(subchannel) {
				case "LTItemMail_MailboxReceived":
					byte[] func = new byte[in.readShort()];
					in.readFully(func);
					final DataInputStream funcMsg = new DataInputStream(new ByteArrayInputStream(func));
					final String[] function = funcMsg.readUTF().split(";");
					final LTPlayer sender = LTPlayer.fromName(function[0]);
					final LTPlayer receiver = LTPlayer.fromName(function[1]);
					final Integer mailboxID = Integer.parseInt(function[2]);
					String type = "chat";
					if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_TITLE)) type = "title";
					if(receiver != null && receiver.getBukkitPlayer().getPlayer() != null) {
						MailboxModule.log(receiver.getUniqueId(), null, Action.RECEIVED, mailboxID, null, null);
						switch(type) {
							case "chat":
								receiver.getBukkitPlayer().getPlayer().sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_FROM) + " " + ChatColor.GREEN + "" + sender.getName());
								break;
							case "title":
								receiver.getBukkitPlayer().getPlayer().sendTitle(ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_FROM) +  " " + ChatColor.GREEN + "" + sender.getName(), "", 20 * 1, 20 * 5, 20 * 1);
								break;
						}
					}
					break;
				case "PlayerList":
					final String server = in.readUTF();
					if(server.equals("ALL")) {
						final String[] players = in.readUTF().split(", ");
						onlinePlayers.clear();
						for(final String bungeePlayer : players) onlinePlayers.add(bungeePlayer);
					}
					break;
			}
		} catch(final IOException e) {
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
		}
	}
}