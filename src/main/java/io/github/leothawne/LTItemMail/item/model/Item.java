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
	public Item.Type getType();
	public Material getMaterial();
	public String[] getData();
	public LinkedList<Listener> getListeners();
	public void runTasks();
	public LinkedList<BukkitTask> getTasks();
	public ItemStack getItem(String data);
	public Recipe getRecipe();
	public enum Type {
		MAILBOX_BLOCK
	}
	public enum Data {
		MAILBOX_LIMITER(999999001),
		MAILBOX_BUTTON_COST(999999002),
		MAILBOX_GUI_NORMAL(999999003),
		MAILBOX_GUI_PENDING(999999004),
		MAILBOX_GUI_ADMIN(999999005),
		MAILBOX_BUTTON_LABEL(999999006),
		MAILBOX_BUTTON_DENY(999999007),
		MAILBOX_BUTTON_ACCEPT(999999008);
		
		public final int value;
		private Data(final int model) {
			this.value = model;
		}
	}
}