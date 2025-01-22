package br.net.gmj.nobookie.LTItemMail;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import br.net.gmj.nobookie.LTItemMail.block.Block;
import br.net.gmj.nobookie.LTItemMail.block.MailboxBlock;
import br.net.gmj.nobookie.LTItemMail.entity.LTPlayer;
import br.net.gmj.nobookie.LTItemMail.module.ConfigurationModule;
import br.net.gmj.nobookie.LTItemMail.module.ConsoleModule;
import br.net.gmj.nobookie.LTItemMail.module.DatabaseModule;
import br.net.gmj.nobookie.LTItemMail.module.ExtensionModule;
import br.net.gmj.nobookie.LTItemMail.module.LanguageModule;
import br.net.gmj.nobookie.LTItemMail.module.MailboxModule;
import br.net.gmj.nobookie.LTItemMail.module.ext.LTUltimateAdvancementAPI;
import net.md_5.bungee.api.ChatColor;

/**
 * 
 * The API class.
 * 
 * @author Nobookie
 * 
 */
public final class LTItemMailAPI {
	private LTItemMailAPI() {}
	private static final LTUltimateAdvancementAPI ultimateAdvancementAPI = (LTUltimateAdvancementAPI) ExtensionModule.getInstance().get(ExtensionModule.Function.ULTIMATEADVANCEMENTAPI);
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
						if(ultimateAdvancementAPI != null) {
							ultimateAdvancementAPI.show(player, LanguageModule.get(LanguageModule.Type.MAILBOX_SPECIAL));
							if(!label.isEmpty()) {
								final String l = label;
								new BukkitRunnable() {
									@Override
									public final void run() {
										ultimateAdvancementAPI.show(player, l);
									}
								}.runTaskLater(LTItemMail.getInstance(), 20 * 3);
							}
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
	/**
	 * 
	 * Gets all existing LT Item Mail
	 * blocks standing in one or
	 * more worlds of the specified
	 * block type.
	 * 
	 * @return A list of LT Item Mail blocks.
	 * 
	 */
	public static final List<Block> getBlockList(){
		final List<Block> blockList = new ArrayList<>();
		for(final MailboxBlock mailboxBlock : DatabaseModule.Block.getMailboxBlocks()) blockList.add(mailboxBlock);
		return blockList;
	}
	/**
	 * 
	 * Gets all existing types of
	 * LT Item Mail blocks standing
	 * in one or more worlds.
	 * 
	 * @param blockType The type of block to be retrieved (See {@link Block.Type}).
	 * 
	 * @return A list of LT Item Mail blocks.
	 * 
	 */
	public static final List<Block> getBlockList(final Block.Type blockType){
		final List<Block> blockList = new ArrayList<>();
		switch(blockType) {
			case MAILBOX_BLOCK:
				for(final MailboxBlock mailboxBlock : DatabaseModule.Block.getMailboxBlocks()) blockList.add(mailboxBlock);
				return blockList;
		}
		return Collections.emptyList();
	}
}