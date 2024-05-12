package io.github.leothawne.LTItemMail.inventory;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.leothawne.LTItemMail.LTPlayer;
import io.github.leothawne.LTItemMail.item.model.ModelData;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import net.md_5.bungee.api.ChatColor;

public final class MailboxInventory {
	private MailboxInventory() {}
	public static final String getName(final Type type, final Integer mailboxID, final LTPlayer player) {
		switch(type) {
			case IN:
				if(mailboxID != null) {
					return (String) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_NAME) + "#" + String.valueOf(mailboxID);
				} else return (String) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_NAME) + "#";
			case OUT:
				if(player != null) {
					return (String) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_NAME) + "@" + player.getName();
				} else return (String) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_NAME) + "@";
		}
		return null;
	}
	public static final Inventory getInventory(final Type type, final Integer mailboxID, LTPlayer player, final LinkedList<ItemStack> contents, final String label) {
		Inventory inventory = null;
		switch(type) {
			case IN:
				inventory = Bukkit.createInventory(null, 36, getName(type, mailboxID, null));
				for(int i = 0; i < 27; i++) inventory.setItem(i, contents.get(i));
				buildGUI(inventory, label);
				break;
			case OUT:
				inventory = Bukkit.createInventory(null, 36, getName(type, mailboxID, player));
				buildGUI(inventory, label);
				break;
		}
		return inventory;
	}
	private static final void buildGUI(final Inventory inventory, final String message) {
		final ItemStack gui = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
		final ItemStack limiter = new ItemStack(Material.BLUE_STAINED_GLASS_PANE, 1);
		final ItemStack cost = new ItemStack(Material.EMERALD, 1);
		final ItemStack label = new ItemStack(Material.BOOK, 1);
		gui.setItemMeta(prepareItem(gui.getItemMeta(), ModelData.INV_GUI.value, " ", Arrays.asList("")));
		limiter.setItemMeta(prepareItem(limiter.getItemMeta(), ModelData.INV_LIMITER.value, " ", Arrays.asList("")));
		cost.setItemMeta(prepareItem(cost.getItemMeta(), ModelData.INV_COSTBUTTON.value, ChatColor.RESET + LanguageModule.get(LanguageModule.Type.MAILBOX_COST), Arrays.asList(ChatColor.RESET + "" + ChatColor.GREEN + "$0.0", ChatColor.RESET + "" + ChatColor.WHITE + LanguageModule.get(LanguageModule.Type.MAILBOX_COSTUPDATE))));
		String bookLore = " ";
		if(!message.isEmpty()) bookLore = ChatColor.RESET + "" + ChatColor.YELLOW + message;
		label.setItemMeta(prepareItem(label.getItemMeta(), ModelData.INV_LABELBUTTON.value, ChatColor.RESET + LanguageModule.get(LanguageModule.Type.MAILBOX_LABEL), Arrays.asList(bookLore)));
		inventory.setItem(27, gui);
		for(int i = 28; i < 30; i++) inventory.setItem(i, limiter);
		inventory.setItem(30, cost);
		inventory.setItem(31, limiter);
		inventory.setItem(32, label);
		for(int i = 33; i < 36; i++) inventory.setItem(i, limiter);
	}
	private static final ItemMeta prepareItem(final ItemMeta meta, final int model, final String name, final List<String> lore) {
		if(model <= 0) {
			meta.setCustomModelData(null);
		} else meta.setCustomModelData(model);
		meta.setDisplayName(name);
		meta.setLore(lore);
		return meta;
	}
	public enum Type {
		IN,
		OUT
	}
}