package br.net.gmj.nobookie.LTItemMail.task;

import java.util.Arrays;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.meta.ItemMeta;

import br.net.gmj.nobookie.LTItemMail.item.Item;
import br.net.gmj.nobookie.LTItemMail.item.MailboxItem;

public final class MailboxTask implements Runnable {
	private final Item mailbox;
	public MailboxTask() {
		mailbox = new MailboxItem();
	}
	@Override
	public final void run() {
		for(final Player player : Bukkit.getOnlinePlayers()) {
			final PlayerInventory inventory = player.getInventory();
			for(final ItemStack item : inventory.getContents()) {
				if(item != null && item.getItemMeta() != null) if(item.getType().toString().endsWith("_SHULKER_BOX")) if(item.getItemMeta().getLore() != null) if(item.getItemMeta().getLore().size() == 1) if(item.getItemMeta().getLore().get(0).contains(mailbox.getDescription(null).get(0))) {
					final ItemMeta meta = item.getItemMeta();
					meta.setLore(Arrays.asList(mailbox.getDescription(null).get(0) + player.getName()));
					item.setItemMeta(meta);
				}
			}
		}
	}
}