package io.github.leothawne.LTItemMail.task;

import org.bukkit.Bukkit;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.api.HTTP;
import io.github.leothawne.LTItemMail.module.DataModule;
import io.github.leothawne.LTItemMail.type.ProjectPageType;

public final class VersionTask implements Runnable {
	public VersionTask() {}
	@Override
	public final void run() {
		final String version = LTItemMail.getInstance().getDescription().getVersion();
		final String url = DataModule.getPluginURL(version);
		final String response = HTTP.getData(url);
		if(response != null) {
			if(response.equalsIgnoreCase("disabled")) {
				LTItemMail.getInstance().getConsole().info("Hey you! Stop right there! This version (" + version + ") is no longer allowed to be used/played.");
				LTItemMail.getInstance().getConsole().info("Download a newer version: " + DataModule.getProjectPage(ProjectPageType.BUKKIT_DEV) + " or " + DataModule.getProjectPage(ProjectPageType.SPIGOT_MC));
				Bukkit.getPluginManager().disablePlugin(LTItemMail.getInstance());
			}
		} else LTItemMail.getInstance().getConsole().warning("Unable to locate: " + url);
	}
}