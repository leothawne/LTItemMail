package io.github.leothawne.LTItemMail;

import io.github.leothawne.LTItemMail.api.bStats.Metrics;

public class MetricsLoader {
	private LTItemMailLoader plugin;
	private ConsoleLoader myLogger;
	public MetricsLoader(LTItemMailLoader plugin, ConsoleLoader myLogger) {
		this.plugin = plugin;
		this.myLogger = myLogger;
	}
	public void init() {
		Metrics metrics = new Metrics(plugin);
		if(metrics.isEnabled() == false) {
			myLogger.warning("LT Item Mail is using bStats. Make sure bStats is enabled! Thank you.");
		}
	}
}