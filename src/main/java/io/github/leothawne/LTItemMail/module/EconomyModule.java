package io.github.leothawne.LTItemMail.module;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import io.github.leothawne.LTItemMail.module.ConfigurationModule.Type;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;
import su.nightexpress.coinsengine.api.currency.Currency;

public final class EconomyModule {
	private static EconomyModule instance = null;
	private ExtensionModule.Name type;
	private Currency currency = null;
	private Economy api = null;
	private Plugin plugin = null;
	private EconomyModule() {
		final String[] config = ((String) ConfigurationModule.get(Type.PLUGIN_HOOK_ECONOMY_TYPE)).split("\\:");
		String coin = null;
		if(config.length > 1) {
			type = ExtensionModule.Name.valueOf(config[0]);
			coin = config[1];
		} else type = ExtensionModule.Name.valueOf(((String) ConfigurationModule.get(Type.PLUGIN_HOOK_ECONOMY_TYPE)).toUpperCase());
		if(type == null) {
			instance = null;
			return;
		}
		switch(type) {
			case VAULT:
				final RegisteredServiceProvider<Economy> vault = Bukkit.getServicesManager().getRegistration(Economy.class);
				if(vault != null) {
					api = vault.getProvider();
					plugin = vault.getPlugin();
				} else {
					instance = null;
					return;
				}
				break;
			case COINSENGINE:
				if(CoinsEngineAPI.class != null && coin != null) {
					currency = CoinsEngineAPI.getCurrency(coin);
					if(currency == null) {
						instance = null;
						return;
					}
				} else {
					instance = null;
					return;
				}
				break;
			default:
				instance = null;
				return;
		}
		if(plugin != null) {
			ExtensionModule.getInstance().warn(type, plugin);
		} else ExtensionModule.getInstance().warn(null, type);
	}
	@SuppressWarnings("incomplete-switch")
	public final boolean deposit(final Player player, final Double amount) {
		switch(type) {
			case VAULT:
				final EconomyResponse response = api.depositPlayer(player, amount);
				return response.transactionSuccess();
			case COINSENGINE:
				CoinsEngineAPI.addBalance(player, currency, amount);
				return true;
		}
		return false;
	}
	@SuppressWarnings("incomplete-switch")
	public final boolean withdraw(final Player player, final Double amount) {
		switch(type) {
			case VAULT:
				final EconomyResponse response = api.withdrawPlayer(player, amount);
				return response.transactionSuccess();
			case COINSENGINE:
				CoinsEngineAPI.removeBalance(player, currency, amount);
				return true;
	}
	return false;
	}
	@SuppressWarnings("incomplete-switch")
	public final boolean has(final Player player, final Double amount) {
		switch(type) {
			case VAULT:
				return api.has(player, amount);
			case COINSENGINE:
				final Double balance = CoinsEngineAPI.getBalance(player, currency);
				return (balance >= amount);
	}
	return false;
	}
	public static final EconomyModule getInstance() {
		if(instance == null) instance = new EconomyModule();
		return instance;
	}
}