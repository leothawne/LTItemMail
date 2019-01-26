package io.github.leothawne.LTItemMail;

import io.github.leothawne.LTItemMail.api.bStats.Metrics;

public class MetricsLoader {
	private static LTItemMailLoader plugin;
	private static ConsoleLoader myLogger;
	public MetricsLoader(LTItemMailLoader plugin, ConsoleLoader myLogger) {
		MetricsLoader.plugin = plugin;
		MetricsLoader.myLogger = myLogger;
	}
	public static final void init() {
		Metrics metrics = new Metrics(plugin);
		if(metrics.isEnabled() == true) {
			myLogger.info("LT Item Mail is using bStats to collect data to improve the next versions. In case you want to know what data will be collected: https://bstats.org/getting-started");
		} else {
			myLogger.warning("bStats is disabled and LT Item Mail cannot use it. Please enable bStats! Thank you.");
		}
	}
}