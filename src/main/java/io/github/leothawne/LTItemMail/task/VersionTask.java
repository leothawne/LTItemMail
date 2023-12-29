package io.github.leothawne.LTItemMail.task;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.module.ConsoleModule;
import io.github.leothawne.LTItemMail.module.DataModule;
import io.github.leothawne.LTItemMail.module.HTTPModule;

public final class VersionTask {
	private VersionTask() {}
	public static final void run() {
		new BukkitRunnable() {
			@Override
			public final void run() {
				final String version = LTItemMail.getInstance().getDescription().getVersion();
				final String url = DataModule.getPluginURL(version);
				final String response = HTTPModule.get(url);
				if(response != null) {
					if(response.toLowerCase().contains("disabled")) {
						ConsoleModule.severe("Hey you! Stop right there! The version (" + version + ") is no longer allowed to be used/played.");
						ConsoleModule.severe("Download a newer one: https://" + DataModule.getProjectPage(DataModule.ProjectType.BUKKIT_DEV) + " or https://" + DataModule.getProjectPage(DataModule.ProjectType.SPIGOT_MC));
						Bukkit.getPluginManager().disablePlugin(LTItemMail.getInstance());
					}
				} else {
					ConsoleModule.warning("Unable to locate: " + url + ". Are you offline?");
					this.cancel();
				}
			}
		}.runTaskTimer(LTItemMail.getInstance(), 0, 20 * 10);
	}
}