package io.github.leothawne.LTItemMail;

import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class VaultLoader {
	private LTItemMailLoader plugin;
	public VaultLoader(LTItemMailLoader plugin) {
		this.plugin = plugin;
	}
	RegisteredServiceProvider<Economy> rsp = null;
	public boolean isInstalled() {
		if(plugin.getServer().getPluginManager().getPlugin("Vault") != null) {
			return true;
		}
		return false;
	}
	public boolean isReady() {
		rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
		if(rsp != null) {
			return true;
		}
		return false;
	}
	public Economy getEconomy() {
		Economy economy = rsp.getProvider();
		if(economy != null) {
			return economy;
		}
		return null;
	}
}