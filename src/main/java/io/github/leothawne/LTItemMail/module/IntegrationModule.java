package io.github.leothawne.LTItemMail.module;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import io.github.leothawne.LTItemMail.LTItemMail;
import net.milkbowl.vault.economy.Economy;

public final class IntegrationModule {
	private IntegrationModule() {}
	public static final void warn(final TPlugin TPlugin) {
		Plugin plugin = null;
		switch(TPlugin) {
			case VAULT:
				plugin = Bukkit.getPluginManager().getPlugin("Vault");
				break;
		}
		if(plugin != null && plugin.isEnabled()) ConsoleModule.info(LTItemMail.getInstance().getDescription().getName() + " successfully hooked into " + plugin.getDescription().getName());
	}
	public static boolean isInstalled(final TPlugin TPlugin) {
		Plugin plugin = null;
		switch(TPlugin) {
			case VAULT:
				plugin = Bukkit.getPluginManager().getPlugin("Vault");
				return (plugin != null && plugin.isEnabled());
		}
		return false;
	}
	public static final Object register(final FPlugin FPlugin) {
		switch(FPlugin) {
			case VAULT_ECONOMY:
				if(isInstalled(TPlugin.VAULT)) {
					final RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
					if(rsp != null) return rsp.getProvider();
				}
				break;
		}
		return null;
	}
	public enum TPlugin {
		VAULT
	}
	public enum FPlugin {
		VAULT_ECONOMY
	}
}