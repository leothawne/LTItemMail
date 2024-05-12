package io.github.leothawne.LTItemMail.event;

import java.util.LinkedList;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.LTPlayer;
/**
 * 
 * This event is called when a mailbox is sent.
 * 
 * @author leothawne
 * 
 */
public final class PlayerSendMailEvent extends Event implements Cancellable {
	private final CommandSender from;
	private final LTPlayer playerTo;
	private final LinkedList<ItemStack> contents;
	private final boolean hasCost;
	private final double cost;
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	public PlayerSendMailEvent(final CommandSender from, final LTPlayer playerTo, final LinkedList<ItemStack> contents, final boolean hasCost, final double cost) {
		this.from = from;
		this.playerTo = playerTo;
		this.contents = contents;
		this.hasCost = hasCost;
		this.cost = cost;
		cancelled = false;
	}
	/**
	 * 
	 * Gets who sent the mailbox.
	 * 
	 */
	public final CommandSender getFrom() {
		return from;
	}
	/**
	 * 
	 * Gets who received the mailbox.
	 * 
	 */
	public final LTPlayer getPlayerTo() {
		return playerTo;
	}
	/**
	 * 
	 * Gets the mailbox contents.
	 * 
	 */
	public final LinkedList<ItemStack> getContents(){
		return contents;
	}
	/**
	 * 
	 * Returns "true" if the mailbox was paid to be sent.
	 * 
	 */
	public final boolean hasCost() {
		return hasCost;
	}
	/**
	 * 
	 * If the mailbox was paid, returns the paid value, otherwise it will return "0.0".
	 * 
	 */
	public final double cost() {
		return cost;
	}
	@Override
	public final HandlerList getHandlers() {
		return handlers;
	}
	public static final HandlerList getHandlerList() {
		return handlers;
	}
	/**
	 * 
	 * Gets if the event was cancelled.
	 * 
	 */
	@Override
	public final boolean isCancelled() {
		return cancelled;
	}
	/**
	 * 
	 * Cancels the event. If cancelled, mailbox deliver is stopped before it reaches the addressee.
	 * 
	 */
	@Override
	public final void setCancelled(final boolean cancel) {
		cancelled = cancel;
	}
}