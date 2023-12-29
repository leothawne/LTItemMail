package io.github.leothawne.LTItemMail.module;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.item.MailboxItem;

public final class RecipeModule {
	private RecipeModule() {}
	public static final void register() {
		new BukkitRunnable() {
			@Override
			public final void run() {
				try {
					Bukkit.getServer().addRecipe(new MailboxItem().getRecipe());
				} catch(final IllegalStateException exception) {}
			}
		}.runTaskTimer(LTItemMail.getInstance(), 0, 20 * 5);
	}
}