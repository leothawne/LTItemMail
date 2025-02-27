package br.net.gmj.nobookie.LTItemMail.event;

import org.bukkit.event.Cancellable;

import br.net.gmj.nobookie.LTItemMail.block.Block;
import br.net.gmj.nobookie.LTItemMail.entity.LTPlayer;
/**
 * 
 * Event called when a mailbox block is broken by a player.
 * 
 * @author Nobookie
 * 
 */
public class PlayerBreakMailboxBlockEvent extends BreakMailboxBlockEvent implements Cancellable {
	private final LTPlayer player;
	private Boolean cancelled = false;
	public PlayerBreakMailboxBlockEvent(final Block block, final BreakMailboxBlockEvent.Reason reason, final LTPlayer player) {
		super(block, reason, false);
		this.player = player;
	}
	/**
	 * 
	 * Gets who is involved on this event.
	 * 
	 */
	public final LTPlayer getPlayer() {
		return player;
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
	 * Cancels the event. If cancelled, the block will not be broken.
	 * 
	 */
	@Override
	public final void setCancelled(final boolean cancel) {
		cancelled = cancel;
	}
}