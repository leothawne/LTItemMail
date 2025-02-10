package br.net.gmj.nobookie.LTItemMail.block;

import java.util.List;

import org.bukkit.Location;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

/**
 * 
 * Interface to represent all types of LT Item Mail blocks.
 * 
 * @author Nobookie
 * 
 */
public interface Block {
	/**
	 * 
	 * Gets the block id.
	 * 
	 */
	public Integer getId();
	/**
	 * 
	 * Gets the block type.
	 * 
	 */
	public Block.Type getType();
	/**
	 * 
	 * Gets in which server the block was created.
	 * 
	 */
	public String getServer();
	/**
	 * 
	 * Gets the block current location.
	 * 
	 */
	public Location getLocation();
	/**
	 * 
	 * Converts from LT Item Mail block to Bukkit block.
	 * 
	 */
	public org.bukkit.block.Block getBukkitBlock();
	/**
	 * 
	 * Used internally. Do not mess with it.
	 * 
	 */
	public List<Listener> getListeners();
	/**
	 * 
	 * Used internally. Do not mess with it.
	 * 
	 */
	public void runTasks();
	/**
	 * 
	 * Used internally. Do not mess with it.
	 * 
	 */
	public List<BukkitTask> getTasks();
	/**
	 * 
	 * All types of blocks.
	 * 
	 */
	public enum Type {
		MAILBOX_BLOCK
	}
}