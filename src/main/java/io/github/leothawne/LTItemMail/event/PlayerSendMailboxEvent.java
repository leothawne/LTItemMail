package io.github.leothawne.LTItemMail.event;

import java.util.LinkedList;

import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.inventory.ItemStack;

public final class PlayerSendMailboxEvent extends Event {
	private final Player playerFrom;
	private final OfflinePlayer playerTo;
	private final LinkedList<ItemStack> contents;
	private final boolean hasCost;
	private final double cost;
	private HandlerList handlers;
	public PlayerSendMailboxEvent(final Player playerFrom, final OfflinePlayer playerTo, final LinkedList<ItemStack> contents, final boolean hasCost, final double cost) {
		this.playerFrom = playerFrom;
		this.playerTo = playerTo;
		this.contents = contents;
		this.hasCost = hasCost;
		this.cost = cost;
		this.handlers = new HandlerList();
	}
	public final Player getPlayerFrom() {
		return playerFrom;
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
	public final HandlerList getHandlerList() {
		return handlers;
	}
}