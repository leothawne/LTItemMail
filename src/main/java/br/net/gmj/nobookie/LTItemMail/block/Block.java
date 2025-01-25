package br.net.gmj.nobookie.LTItemMail.block;

import org.bukkit.Location;

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
	 * All types of blocks.
	 * 
	 */
	public enum Type {
		MAILBOX_BLOCK
	}
}