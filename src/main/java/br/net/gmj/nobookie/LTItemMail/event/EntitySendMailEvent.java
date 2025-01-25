package br.net.gmj.nobookie.LTItemMail.event;

import java.util.LinkedList;

import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

import br.net.gmj.nobookie.LTItemMail.entity.LTPlayer;
/**
 * 
 * This event is called when a mailbox is sent.
 * 
 * @author Nobookie
 * 
 */
public final class EntitySendMailEvent extends Event implements Cancellable {
	private final CommandSender from;
	private final LTPlayer playerTo;
	private final LinkedList<ItemStack> contents;
	private final Boolean hasCost;
	private final Double cost;
	private static final HandlerList handlers = new HandlerList();
	private Boolean cancelled;
	private String cancelReason;
	public EntitySendMailEvent(final CommandSender from, final LTPlayer playerTo, final LinkedList<ItemStack> contents, final Boolean hasCost, final Double cost) {
		this.from = from;
		this.playerTo = playerTo;
		this.contents = contents;
		this.hasCost = hasCost;
		this.cost = cost;
		cancelled = false;
		cancelReason = "";
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
	public final Boolean hasCost() {
		return hasCost;
	}
	/**
	 * 
	 * If the mailbox was paid, returns the paid value, otherwise it will return "0.0".
	 * 
	 */
	public final Double cost() {
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
	 * Cancels the event. If cancelled, mailbox deliver is stopped before it reaches the addressee and returns to the sender.
	 * 
	 */
	@Override
	public final void setCancelled(final boolean cancel) {
		cancelled = cancel;
	}
	/**
	 * 
	 * If the event is cancelled, gets the cancel reason.
	 * 
	 */
	public final String getCancelReason() {
		return cancelReason;
	}
	/**
	 * 
	 * Sets the event cancel reason.
	 * 
	 */
	public final void setCancelReason(final String reason) {
		cancelReason = reason;
	}
}