package io.github.leothawne.LTItemMail.api;

import java.util.LinkedList;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import io.github.leothawne.LTItemMail.LTItemMail;

public final class WarnIntegrationsAPI {
	public WarnIntegrationsAPI(final LTItemMail mainPlugin, final LinkedList<String> plugins) {
		final PluginManager manager = mainPlugin.getServer().getPluginManager();
		for(final String plugin : plugins) {
			final Plugin getPlugin = manager.getPlugin(plugin);
			if(getPlugin != null && getPlugin.isEnabled()) {
				getPlugin.getLogger().info(mainPlugin.getName() + " were successfully hooked into " + getPlugin.getName() + "!");
			}
		}
	}
}