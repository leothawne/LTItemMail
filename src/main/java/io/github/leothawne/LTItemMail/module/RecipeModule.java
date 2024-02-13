package io.github.leothawne.LTItemMail.module;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.item.MailboxItem;
import io.github.leothawne.LTItemMail.item.model.Item;

public final class RecipeModule {
	private RecipeModule() {}
	private static final Item mailbox = new MailboxItem();
	public static final void register() {
		try {
			Bukkit.addRecipe(mailbox.getRecipe());
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) ConsoleModule.debug(mailbox.getType().toString().toLowerCase() + " registered.");
		} catch(final IllegalStateException exception) {
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) ConsoleModule.debug(mailbox.getType().toString().toLowerCase() + " is registered already.");
		}
	}
	public static final void scheduleFailsafe() {
		new BukkitRunnable() {
			@Override
			public final void run() {
				RecipeModule.register();
			}
		}.runTaskTimer(LTItemMail.getInstance(), 20 * 15, 20 * 15);
	}
}