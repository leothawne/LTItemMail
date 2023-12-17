package io.github.leothawne.LTItemMail.module;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public final class VaultModule {
	public static final boolean isVaultInstalled() {
		final Plugin vault = Bukkit.getPluginManager().getPlugin("Vault");
		return (vault != null && vault.isEnabled());
	}
	public static final Economy getEconomy() {
		final RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
		if(rsp != null) return rsp.getProvider();
		return null;
	}
}