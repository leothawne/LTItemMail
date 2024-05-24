package io.github.leothawne.LTItemMail.item.model;

public enum ItemData {
	MAILBOX_LIMITER(656624),
	MAILBOX_BUTTON_COST(280808),
	MAILBOX_GUI_NORMAL(803313),
	MAILBOX_GUI_PENDING(803314),
	MAILBOX_GUI_ADMIN(803315),
	MAILBOX_BUTTON_LABEL(722580),
	MAILBOX_BUTTON_DENY(884369),
	MAILBOX_BUTTON_ACCEPT(381527);
	
	public final int value;
	private ItemData(final int model) {
		this.value = model;
	}
}