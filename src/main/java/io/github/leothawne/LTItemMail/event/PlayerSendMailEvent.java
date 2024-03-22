package io.github.leothawne.LTItemMail.event;

import java.util.LinkedList;

import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public final class PlayerSendMailEvent extends Event implements Cancellable {
	private final CommandSender from;
	private final OfflinePlayer playerTo;
	private final LinkedList<ItemStack> contents;
	private final boolean hasCost;
	private final double cost;
	private static final HandlerList handlers = new HandlerList();
	private boolean cancelled;
	public PlayerSendMailEvent(final CommandSender from, final OfflinePlayer playerTo, final LinkedList<ItemStack> contents, final boolean hasCost, final double cost) {
		this.from = from;
		this.playerTo = playerTo;
		this.contents = contents;
		this.hasCost = hasCost;
		this.cost = cost;
		cancelled = false;
	}
	public final CommandSender getPlayerFrom() {
		return from;
	}
	public final OfflinePlayer getPlayerTo() {
		return playerTo;
	}
	public final LinkedList<ItemStack> getContents(){
		return contents;
	}
	public final boolean hasCost() {
		return hasCost;
	}
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
	@Override
	public final boolean isCancelled() {
		return cancelled;
	}
	@Override
	public final void setCancelled(final boolean cancel) {
		cancelled = cancel;
	}
}