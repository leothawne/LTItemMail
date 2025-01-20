package br.net.gmj.nobookie.LTItemMail.util;

import java.util.LinkedList;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import net.md_5.bungee.api.ChatColor;

public final class BukkitUtil {
	private BukkitUtil() {}
	public static final String format(final String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	public static final boolean isMailboxEmpty(final ItemStack[] contents) {
		Boolean isEmpty = true;
		for(int i = 0; i < 27; i++) if(contents[i] != null) {
			isEmpty = false;
			break;
		}
		return isEmpty;
	}
	public static final LinkedList<ItemStack> getMailboxContents(final ItemStack[] contents){
		final LinkedList<ItemStack> items = new LinkedList<>();
		for(int i = 0; i < 27; i++) if(contents[i] != null) {
				items.add(contents[i]);
			} else items.add(new ItemStack(Material.AIR));
		return items;
	}
	public static final int getItemsCount(final LinkedList<ItemStack> items) {
		int count = 0;
		for(final ItemStack item : items) if(item.getType() != Material.AIR) count = count + item.getAmount();
		return count;
	}
}