package io.github.leothawne.LTItemMail.module;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import io.github.leothawne.LTItemMail.LTItemMail;
import net.milkbowl.vault.economy.Economy;

public final class VaultModule {
	public static final boolean isVaultInstalled(final LTItemMail plugin) {
		final Plugin vault = plugin.getServer().getPluginManager().getPlugin("Vault");
		return (vault != null && vault.isEnabled());
	}
	public static final Economy getEconomy(final LTItemMail plugin) {
		final RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
		if(rsp != null) {
			return rsp.getProvider();
		}
		return null;
	}
}