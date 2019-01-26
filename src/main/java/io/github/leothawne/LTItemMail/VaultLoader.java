package io.github.leothawne.LTItemMail;

import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class VaultLoader {
	private static LTItemMailLoader plugin;
	public VaultLoader(LTItemMailLoader plugin) {
		VaultLoader.plugin = plugin;
	}
	private static RegisteredServiceProvider<Economy> rsp = null;
	public static final boolean isInstalled() {
		if(plugin.getServer().getPluginManager().getPlugin("Vault") != null) {
			return true;
		}
		return false;
	}
	public static final boolean isReady() {
		rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
		if(rsp != null) {
			return true;
		}
		return false;
	}
	public static final Economy getEconomy() {
		Economy economy = rsp.getProvider();
		if(economy != null) {
			return economy;
		}
		return null;
	}
}