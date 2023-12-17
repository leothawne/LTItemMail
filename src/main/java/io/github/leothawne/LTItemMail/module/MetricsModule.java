package io.github.leothawne.LTItemMail.module;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.api.MetricsAPI;

public final class MetricsModule {
	public static final MetricsAPI init() {
		final MetricsAPI metrics = new MetricsAPI(LTItemMail.getInstance(), 3647);
		return metrics;
	}
}