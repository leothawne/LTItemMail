package io.github.leothawne.LTItemMail.module.integration;

import org.bukkit.plugin.Plugin;

import net.milkbowl.vault.economy.Economy;

public final class LTVault {
	private Plugin economyPlugin = null;
	private Economy economyAPI = null;
	public final void setEconomy(final Economy economyAPI) {
		this.economyAPI = economyAPI;
	}
	public final void setEconomyPlugin(final Plugin economyPlugin) {
		this.economyPlugin = economyPlugin;
	}
	public final Plugin getEconomyPlugin() {
		return economyPlugin;
	}
	public final Economy getEconomy() {
		return economyAPI;
	}
}