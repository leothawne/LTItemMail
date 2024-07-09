package io.github.leothawne.LTItemMail.block;

import org.bukkit.Location;

public interface Block {
	public Integer getId();
	public Block.Type getType();
	public Location getLocation();
	public org.bukkit.block.Block getBukkitBlock();
	public enum Type {
		MAILBOX_BLOCK
	}
}