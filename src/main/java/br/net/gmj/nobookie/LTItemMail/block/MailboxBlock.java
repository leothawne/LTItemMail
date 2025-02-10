package br.net.gmj.nobookie.LTItemMail.block;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

import br.net.gmj.nobookie.LTItemMail.LTItemMail;
import br.net.gmj.nobookie.LTItemMail.LTItemMailAPI;
import br.net.gmj.nobookie.LTItemMail.block.listener.MailboxBlockListener;
import br.net.gmj.nobookie.LTItemMail.block.task.MailboxBlockTask;
import br.net.gmj.nobookie.LTItemMail.module.DatabaseModule;

/**
 * 
 * @author Nobookie
 * 
 */
public final class MailboxBlock implements Block {
	private final Integer id;
	private UUID owner;
	private final String server;
	private final String world;
	private final Integer x;
	private final Integer y;
	private final Integer z;
	/**
	 * 
	 * Use {@link LTItemMailAPI#getBlockList()} instead.
	 * 
	 * 
	 */
	public MailboxBlock(final Integer id, final UUID owner, final String server, final String world, final Integer x, final Integer y, final Integer z) {
		this.id = id;
		this.owner = owner;
		this.server = server;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}
	/**
	 * 
	 * Used internally. Do not mess with it.
	 * 
	 */
	public MailboxBlock() {
		id = null;
		owner = null;
		server = null;
		world = null;
		x = null;
		y = null;
		z = null;
	}
	/**
	 * 
	 * Gets the block id.
	 * 
	 */
	@Override
	public final Integer getId() {
		return id;
	}
	/**
	 * 
	 * Gets the block type.
	 * 
	 */
	@Override
	public final Block.Type getType(){
		return Block.Type.MAILBOX_BLOCK;
	}
	/**
	 * 
	 * Gets in which server the block was created.
	 * 
	 */
	@Override
	public final String getServer() {
		return server;
	}
	/**
	 * 
	 * Gets the block current location.
	 * 
	 */
	@Override
	public final Location getLocation() {
		return new Location(Bukkit.getWorld(world), x, y, z);
	}
	/**
	 * 
	 * Converts from LT Item Mail block to Bukkit block.
	 * 
	 */
	@Override
	public final org.bukkit.block.Block getBukkitBlock(){
		return getLocation().getBlock();
	}
	/**
	 * 
	 * Used internally. Do not mess with it.
	 * 
	 */
	@Override
	public final List<Listener> getListeners(){
		return Arrays.asList(new MailboxBlockListener());
	}
	private final List<BukkitTask> tasks = new ArrayList<>();
	/**
	 * 
	 * Used internally. Do not mess with it.
	 * 
	 */
	@Override
	public final void runTasks() {
		tasks.add(Bukkit.getScheduler().runTaskTimer(LTItemMail.getInstance(), new MailboxBlockTask(), 20, 20));
	}
	/**
	 * 
	 * Used internally. Do not mess with it.
	 * 
	 */
	@Override
	public final List<BukkitTask> getTasks(){
		return tasks;
	}
	/**
	 * 
	 * Gets the owner of the mailbox block.
	 * 
	 */
	public final UUID getOwner() {
		return owner;
	}
	/**
	 * 
	 * Removes the mailbox block.
	 * Unregisters the block from the database.
	 * The mailbox block will not drop.
	 * 
	 * @param virtual If set to true, the current block will be set to air.
	 * 
	 */
	public final void remove(final Boolean virtual) {
		DatabaseModule.Block.breakMailbox(getLocation());
		if(!virtual) getBukkitBlock().setType(Material.AIR);
	}
	/**
	 * 
	 * Replaces the mailbox block.
	 * Unregisters the block and register again on the database.
	 * 
	 */
	public final void replace() {
		DatabaseModule.Block.breakMailbox(getLocation());
		DatabaseModule.Block.placeMailbox(owner, getLocation());
	}
	/**
	 * 
	 * Transfers the mailbox block to a new owner.
	 * @param newOwner The new owner's unique id.
	 * 
	 */
	public final void transferOwnership(final UUID newOwner) {
		if(newOwner == owner) return;
		DatabaseModule.Block.breakMailbox(getLocation());
		DatabaseModule.Block.placeMailbox(newOwner, getLocation());
		owner = newOwner;
	}
}