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
import io.github.leothawne.LTItemMail.item.model.Item;
import io.github.leothawne.LTItemMail.item.model.ItemType;
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
	public final ItemType getType() {
		return ItemType.MAILBOX_ITEM;
	}
	@Override
	public final Material getMaterial() {
		return Material.LIGHT_GRAY_SHULKER_BOX;
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
		ItemStack item = new ItemStack(getMaterial());
		final ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(getName());
		meta.setLore(getDescription(data));
		item.setItemMeta(meta);
		return item;
	}
	@Override
	public final Recipe getRecipe() {
		final ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(LTItemMail.getInstance(), getType().toString().toLowerCase()), getItem(null));
		recipe.shape("PPP", "PBP", "PFP");
		recipe.setIngredient('P', Material.STONE);
		recipe.setIngredient('B', Material.CHEST);
		recipe.setIngredient('F', Material.IRON_INGOT);
		return recipe;
	}
}