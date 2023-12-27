package io.github.leothawne.LTItemMail.item.model;

import java.util.LinkedList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.Recipe;
import org.bukkit.scheduler.BukkitTask;

public interface Item {
	public String getName();
	public List<String> getDescription(String data);
	public ItemType getType();
	public Material getMaterial();
	public String[] getData();
	public LinkedList<Listener> getListeners();
	public void runTasks();
	public LinkedList<BukkitTask> getTasks();
	public ItemStack getItem(String data);
	public Recipe getRecipe();
}