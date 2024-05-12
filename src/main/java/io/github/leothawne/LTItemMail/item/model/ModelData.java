package io.github.leothawne.LTItemMail.item.model;

public enum ModelData {
	INV_LIMITER(656624),
	INV_COSTBUTTON(280808),
	INV_GUI(803313),
	INV_LABELBUTTON(722580);
	
	public final int value;
	private ModelData(final int model) {
		this.value = model;
	}
}