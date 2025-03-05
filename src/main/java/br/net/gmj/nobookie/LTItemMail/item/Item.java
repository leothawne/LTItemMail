package br.net.gmj.nobookie.LTItemMail.item;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.scheduler.BukkitTask;

public interface Item {
	public String getName();
	public List<String> getDescription(String data);
	public Item.Type getType();
	public Material getMaterial();
	public String[] getData();
	public List<Listener> getListeners();
	public void runTasks();
	public List<BukkitTask> getTasks();
	public ItemStack getItem(String data);
	public Recipe getRecipe();
	public enum Type {
		MAILBOX_ITEM
	}
}