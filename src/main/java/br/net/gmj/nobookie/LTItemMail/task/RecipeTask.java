package br.net.gmj.nobookie.LTItemMail.task;

import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;

import br.net.gmj.nobookie.LTItemMail.item.Item;
import br.net.gmj.nobookie.LTItemMail.item.MailboxItem;
import br.net.gmj.nobookie.LTItemMail.module.ConfigurationModule;
import br.net.gmj.nobookie.LTItemMail.module.ConsoleModule;

public final class RecipeTask implements Runnable {
	private final List<Item> items;
	public RecipeTask() {
		items = Arrays.asList(new MailboxItem());
	}
	@Override
	public final void run() {
		for(final Item item : items) try {
			Bukkit.addRecipe(item.getRecipe());
			ConsoleModule.debug(item.getType().toString() + " registered.");
		} catch(final IllegalStateException e) {
			ConsoleModule.debug(item.getType().toString() + " is registered already.");
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
		}
	}
}