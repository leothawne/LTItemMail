package io.github.leothawne.LTItemMail.module;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.api.MetricsAPI;

public final class MetricsModule {
	private MetricsModule() {}
	public static final void register() {
		new MetricsAPI(LTItemMail.getInstance(), 3647);
	}
}