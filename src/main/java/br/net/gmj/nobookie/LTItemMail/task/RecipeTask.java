package br.net.gmj.nobookie.LTItemMail.task;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import br.net.gmj.nobookie.LTItemMail.LTItemMail;
import br.net.gmj.nobookie.LTItemMail.item.Item;
import br.net.gmj.nobookie.LTItemMail.item.MailboxItem;
import br.net.gmj.nobookie.LTItemMail.module.ConsoleModule;

public final class RecipeTask {
	private final List<Item> items;
	public RecipeTask() {
		items = Arrays.asList(new MailboxItem());
		new BukkitRunnable() {
			@Override
			public final void run() {
				for(final Item item : items) try {
					if(Bukkit.addRecipe(item.getRecipe())) {
						ConsoleModule.debug(item.getType().toString() + " recipe registered.");
					} else ConsoleModule.debug(item.getType().toString() + " recipe was not registered due to an unknown reason.");
				} catch(final IllegalStateException e) {
					ConsoleModule.debug(item.getType().toString() + " recipe is registered already.");
				}
			}
		}.runTaskTimer(LTItemMail.getInstance(), 1, 20 * 60 * 5);
	}
}