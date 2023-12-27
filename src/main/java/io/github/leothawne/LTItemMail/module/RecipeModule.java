package io.github.leothawne.LTItemMail.module;

import org.bukkit.Bukkit;

import io.github.leothawne.LTItemMail.item.MailboxItem;

public final class RecipeModule {
	private RecipeModule() {}
	public static final void register() {
		Bukkit.addRecipe(new MailboxItem().getRecipe());
	}
}