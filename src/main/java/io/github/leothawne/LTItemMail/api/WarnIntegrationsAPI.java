package io.github.leothawne.LTItemMail.api;

import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.module.ConsoleModule;

public final class WarnIntegrationsAPI {
	public WarnIntegrationsAPI(final List<String> plugins) {
		final PluginManager manager = Bukkit.getPluginManager();
		for(final String plugin : plugins) {
			final Plugin getPlugin = manager.getPlugin(plugin);
			if(getPlugin != null && getPlugin.isEnabled()) ConsoleModule.info(LTItemMail.getInstance().getName() + " successfully hooked into " + getPlugin.getName() + "!");
		}
	}
}