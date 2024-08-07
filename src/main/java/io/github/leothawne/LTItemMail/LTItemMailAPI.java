package io.github.leothawne.LTItemMail;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import io.github.leothawne.LTItemMail.block.Block;
import io.github.leothawne.LTItemMail.block.MailboxBlock;
import io.github.leothawne.LTItemMail.entity.LTPlayer;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.ConsoleModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.MailboxModule;
import io.github.leothawne.LTItemMail.util.Toasts;
import net.md_5.bungee.api.ChatColor;

/**
 * 
 * @author leothawne
 * 
 */
public final class LTItemMailAPI {
	private LTItemMailAPI() {}
	/**
	 * 
	 * Method used to send items anonymously
	 * to any player on the server. The mailbox
	 * will be assigned as a "Special Mailbox".
	 * 
	 * @param player The player who will receive (See {@link LTPlayer}).
	 * @param items A list of items that the player will receive.
	 * @param label The label you want to put on the mailbox.
	 * 
	 * @return "success" if it was successfully delivered. Otherwise it will return an error message.
	 * 
	 */
	public static final String sendSpecialMailbox(final LTPlayer player, final LinkedList<ItemStack> items, String label) {
		if(player != null) {
			final Player bukkitPlayer = player.getBukkitPlayer().getPlayer();
			if(label == null) label = "";
			DatabaseModule.Virtual.saveMailbox(null, player.getBukkitPlayer().getUniqueId(), items, label);
			if(bukkitPlayer != null) {
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
						bukkitPlayer.sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_SPECIAL));
						break;
					case TITLE:
						bukkitPlayer.sendTitle(ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_SPECIAL), "", 20 * 1, 20 * 5, 20 * 1);
						break;
					case TOAST:
						Toasts.display(player, LanguageModule.get(LanguageModule.Type.MAILBOX_SPECIAL), Toasts.Type.MAILBOX);
						if(!label.isEmpty()) {
							final String l = label;
							new BukkitRunnable() {
								@Override
								public final void run() {
									Toasts.display(player, l, Toasts.Type.MAILBOX);
								}
							}.runTaskLater(LTItemMail.getInstance(), 20 * 3);
						}
						break;
				}
			} else if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.BUNGEE_MODE)) {
				final ByteArrayDataOutput bungee = ByteStreams.newDataOutput();
				bungee.writeUTF("Message");
				bungee.writeUTF(player.getName());
				bungee.writeUTF((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_SPECIAL));
				Bukkit.getServer().sendPluginMessage(LTItemMail.getInstance(), "BungeeCord", bungee.toByteArray());
			}
			return "success";
		}
		return LanguageModule.Type.PLAYER_NEVERPLAYEDERROR.toString();
	}
	public static final List<Block> getBlockList(){
		final List<Block> blockList = new ArrayList<>();
		for(final MailboxBlock mailboxBlock : DatabaseModule.Block.getMailboxBlocks()) blockList.add(mailboxBlock);
		return blockList;
	}
}