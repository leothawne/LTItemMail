package io.github.leothawne.LTItemMail.block;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import io.github.leothawne.LTItemMail.module.DatabaseModule;

public final class MailboxBlock implements Block {
	private final Integer id;
	private UUID owner;
	private final String world;
	private final Integer x;
	private final Integer y;
	private final Integer z;
	public MailboxBlock(final Integer id, final UUID owner, final String world, final Integer x, final Integer y, final Integer z) {
		this.id = id;
		this.owner = owner;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	@Override
	public final Integer getId() {
		return id;
	}
	@Override
	public final Block.Type getType(){
		return Block.Type.MAILBOX_BLOCK;
	}
	@Override
	public final Location getLocation() {
		return new Location(Bukkit.getWorld(world), x, y, z);
	}
	@Override
	public final org.bukkit.block.Block getBukkitBlock(){
		return getLocation().getBlock();
	}
	public final UUID getOwner() {
		return owner;
	}
	public final void remove(final Boolean virtual) {
		DatabaseModule.Block.breakMailbox(getLocation());
		if(!virtual) getBukkitBlock().setType(Material.AIR);
	}
	public final void replace() {
		DatabaseModule.Block.breakMailbox(getLocation());
		DatabaseModule.Block.placeMailbox(owner, getLocation());
	}
	public final void transferOwnership(final UUID newOwner) {
		if(newOwner == owner) return;
		DatabaseModule.Block.breakMailbox(getLocation());
		DatabaseModule.Block.placeMailbox(newOwner, getLocation());
		owner = newOwner;
	}
}