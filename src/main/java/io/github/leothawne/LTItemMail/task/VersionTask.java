package io.github.leothawne.LTItemMail.task;

import org.bukkit.Bukkit;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.api.HTTP;
import io.github.leothawne.LTItemMail.module.ConsoleModule;
import io.github.leothawne.LTItemMail.module.DataModule;
import io.github.leothawne.LTItemMail.type.ProjectPageType;

public final class VersionTask implements Runnable {
	@Override
	public final void run() {
		final String version = LTItemMail.getInstance().getDescription().getVersion();
		final String url = DataModule.getPluginURL(version);
		final String response = HTTP.getData(url);
		if(response != null) {
			if(response.equalsIgnoreCase("disabled")) {
				ConsoleModule.info("Hey you! Stop right there! The version (" + version + ") is no longer allowed to be used/played.");
				ConsoleModule.info("Download a newer one: " + DataModule.getProjectPage(ProjectPageType.BUKKIT_DEV) + " or " + DataModule.getProjectPage(ProjectPageType.SPIGOT_MC));
				Bukkit.getPluginManager().disablePlugin(LTItemMail.getInstance());
			}
		} else ConsoleModule.warning("Unable to locate: " + url + ". Are you offline?");
	}
}