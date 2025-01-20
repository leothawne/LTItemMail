package br.net.gmj.nobookie.LTItemMail.util;

import java.util.LinkedList;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;

import br.net.gmj.nobookie.LTItemMail.LTItemMail;
import br.net.gmj.nobookie.LTItemMail.item.Item;
import net.md_5.bungee.api.ChatColor;

public final class BukkitUtil {
	private BukkitUtil() {}
	public static final String format(final String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	public static final class Inventory {
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
	public static final class DataContainer {
		public static final ItemMeta setMailbox(final ItemMeta meta) {
			meta.getPersistentDataContainer().set(new NamespacedKey(LTItemMail.getInstance(), Item.Type.MAILBOX_ITEM.toString()), PersistentDataType.STRING, Item.Type.MAILBOX_ITEM.toString());
			return meta;
		}
		public static final ItemStack setMailbox(final ItemStack item) {
			item.setItemMeta(setMailbox(item.getItemMeta()));
			return item;
		}
		public static final boolean isMailbox(final ItemMeta meta) {
			return meta.getPersistentDataContainer().has(new NamespacedKey(LTItemMail.getInstance(), Item.Type.MAILBOX_ITEM.toString()), PersistentDataType.STRING);
		}
		public static final boolean isMailbox(final ItemStack item) {
			return isMailbox(item.getItemMeta());
		}
	}
}