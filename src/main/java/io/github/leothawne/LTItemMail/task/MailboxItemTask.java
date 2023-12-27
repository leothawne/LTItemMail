package io.github.leothawne.LTItemMail.task;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import io.github.leothawne.LTItemMail.item.MailboxItem;
import io.github.leothawne.LTItemMail.item.model.Item;

public final class MailboxItemTask implements Runnable {
	final Item mailbox = new MailboxItem();
	@Override
	public final void run() {
		for(final Player player : Bukkit.getOnlinePlayers()) {
			final PlayerInventory inventory = player.getInventory();
			for(final ItemStack item : inventory.getContents()) {
				if(item != null && item.getItemMeta() != null) if(item.getType().equals(mailbox.getMaterial())) if(item.getItemMeta().getLore() != null) if(item.getItemMeta().getLore().size() == 1) if(item.getItemMeta().getLore().get(0).contains("Mailbox@")) {
					final ItemMeta meta = item.getItemMeta();
					meta.setLore(Arrays.asList("Mailbox@" + player.getName()));
					item.setItemMeta(meta);
				}
			}
		}
	}
}