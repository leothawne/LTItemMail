package io.github.leothawne.LTItemMail.module;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.api.MetricsAPI;

public final class MetricsModule {
	public static final MetricsAPI init(final LTItemMail plugin, final ConsoleModule console) {
		final MetricsAPI metrics = new MetricsAPI(plugin, 3647);
		if(metrics.isEnabled()) {
			console.info(plugin.getName() + " is using bStats: https://bstats.org/getting-started");
		} else {
			console.warning("bStats is disabled. Please enable bStats!");
		}
		return metrics;
	}
}