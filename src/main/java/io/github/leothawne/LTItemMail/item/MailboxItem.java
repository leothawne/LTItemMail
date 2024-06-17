package io.github.leothawne.LTItemMail.item;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitTask;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;

public final class MailboxItem implements Item {
	@Override
	public final String getName() {
		return (String) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_NAME);
	}
	@Override
	public final List<String> getDescription(String data) {
		return Arrays.asList("Mailbox@");
	}
	@Override
	public final Item.Type getType() {
		return Item.Type.MAILBOX_BLOCK;
	}
	@Override
	public final Material getMaterial() {
		return Material.WHITE_SHULKER_BOX;
	}
	@Override
	public final String[] getData() {
		return null;
	}
	@Override
	public final LinkedList<Listener> getListeners() {
		return null;
	}
	@Override
	public final void runTasks() {}
	@Override
	public final LinkedList<BukkitTask> getTasks() {
		return null;
	}
	@Override
	public final ItemStack getItem(final String data) {
		final ItemStack item = new ItemStack(getMaterial(), 1);
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getName());
		meta.setLore(getDescription(data));
		item.setItemMeta(meta);
		return item;
	}
	@Override
	public final Recipe getRecipe() {
		final NamespacedKey key = new NamespacedKey(LTItemMail.getInstance(), getType().toString().toLowerCase());
		final ShapedRecipe recipe = new ShapedRecipe(key, getItem(null));
		recipe.shape("ppp", "pbp", "pgp");
		recipe.setIngredient('p', Material.STONE);
		recipe.setIngredient('b', Material.CHEST);
		recipe.setIngredient('g', Material.IRON_INGOT);
		return recipe;
	}
}