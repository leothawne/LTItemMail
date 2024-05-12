package io.github.leothawne.LTItemMail;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.LinkedList;

import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
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
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.BUNGEE_MODE)) {
				try {
					final ByteArrayOutputStream bungee = new ByteArrayOutputStream();
					final DataOutputStream bungeeOut = new DataOutputStream(bungee);
					bungeeOut.writeUTF("Message");
					bungeeOut.writeUTF(player.getName());
					bungeeOut.writeUTF((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_SPECIAL));
					Bukkit.getServer().sendPluginMessage(LTItemMail.getInstance(), "BungeeCord", bungee.toByteArray());
				} catch (final IOException e) {
					if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
				}
			} else {
				if(player.getBukkitPlayer().getPlayer() != null) {
					if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_TITLE)) {
						player.getBukkitPlayer().getPlayer().sendTitle(ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_SPECIAL), "", 20 * 1, 20 * 5, 20 * 1);
					} else player.getBukkitPlayer().getPlayer().sendMessage((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TAG) + " " + ChatColor.AQUA + "" + LanguageModule.get(LanguageModule.Type.MAILBOX_SPECIAL));
				}
			}
			if(label == null) label = "";
			DatabaseModule.Virtual.saveMailbox(null, player.getBukkitPlayer().getUniqueId(), items, label);
			return "success";
		}
		return LanguageModule.Type.PLAYER_NEVERPLAYEDERROR.toString();
	}
}