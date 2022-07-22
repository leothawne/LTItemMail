package io.github.leothawne.LTItemMail.task;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.api.HTTP;
import io.github.leothawne.LTItemMail.module.ConsoleModule;
import io.github.leothawne.LTItemMail.module.DataModule;
import io.github.leothawne.LTItemMail.type.ProjectPageType;

public final class VersionTask implements Runnable {
	private static LTItemMail plugin;
	private static ConsoleModule myLogger;
	public VersionTask(final LTItemMail plugin, final ConsoleModule myLogger) {
		VersionTask.plugin = plugin;
		VersionTask.myLogger = myLogger;
	}
	@Override
	public final void run() {
		final String version = VersionTask.plugin.getDescription().getVersion();
		final String url = DataModule.getPluginURL(version);
		final String response = HTTP.getData(url);
		if(response != null) {
			if(response.equalsIgnoreCase("disabled")) {
				VersionTask.myLogger.info("Hey you! Stop right there! This version (" + version + ") is no longer allowed to be used/played.");
				VersionTask.myLogger.info("Download a newer version: " + DataModule.getProjectPage(ProjectPageType.BUKKIT_DEV) + " or " + DataModule.getProjectPage(ProjectPageType.SPIGOT_MC));
				VersionTask.plugin.getServer().getPluginManager().disablePlugin(VersionTask.plugin);
			}
		} else {
			VersionTask.myLogger.warning("Unable to locate: " + url);
		}
	}
}