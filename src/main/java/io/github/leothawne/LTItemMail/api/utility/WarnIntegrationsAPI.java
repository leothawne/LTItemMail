package io.github.leothawne.LTItemMail.api.utility;

import java.util.List;

import org.bukkit.plugin.Plugin;

import io.github.leothawne.LTItemMail.LTItemMailLoader;

public class WarnIntegrationsAPI {
	public WarnIntegrationsAPI(LTItemMailLoader mainPlugin, List<String> plugins) {
		for(String plugin : plugins) {
			Plugin getPlugin = mainPlugin.getServer().getPluginManager().getPlugin(plugin);
			if(getPlugin != null) {
				getPlugin.getLogger().warning(getPlugin.getName() + " was successfully integrated with " + mainPlugin.getName() + "!");
			}
		}
	}
}