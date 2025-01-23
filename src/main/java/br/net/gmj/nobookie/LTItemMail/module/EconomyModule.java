package br.net.gmj.nobookie.LTItemMail.module;

import java.math.BigDecimal;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import br.net.gmj.nobookie.LTItemMail.module.ConfigurationModule.Type;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import net.tnemc.core.TNECore;
import net.tnemc.core.api.TNEAPI;
import su.nightexpress.coinsengine.api.CoinsEngineAPI;

public final class EconomyModule {
	private static EconomyModule instance = null;
	private ExtensionModule.Name type;
	private Object currency = null;
	private Object api = null;
	private Plugin plugin = null;
	private EconomyModule() {
		final String[] config = ((String) ConfigurationModule.get(Type.PLUGIN_HOOK_ECONOMY_TYPE)).split("\\:");
		String coin = null;
		try {
			if(config.length > 1) {
				type = ExtensionModule.Name.valueOf(config[0].toUpperCase());
				coin = config[1];
			} else type = ExtensionModule.Name.valueOf(((String) ConfigurationModule.get(Type.PLUGIN_HOOK_ECONOMY_TYPE)).toUpperCase());
		} catch(final IllegalArgumentException e) {
			ConsoleModule.severe("Economy provider not found. Disabling.");
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
		}
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
					for(final su.nightexpress.coinsengine.api.currency.Currency c : CoinsEngineAPI.getCurrencyManager().getCurrencies()) if(c.getName().equalsIgnoreCase(coin)) {
						currency = c;
						break;
					}
					if(currency == null) {
						instance = null;
						return;
					}
				} else {
					instance = null;
					return;
				}
				break;
			case THENEWECONOMY:
				if(TNECore.api() != null && coin != null) {
					api = TNECore.api();
					for(final net.tnemc.core.currency.Currency c : TNECore.api().getCurrencies()) if(c.getIdentifier().equalsIgnoreCase(coin)) {
						currency = c;
						break;
					}
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
	public final boolean deposit(final Player player, final Double amount) {
		switch(type) {
			case VAULT:
				final EconomyResponse response = ((Economy) api).depositPlayer(player, amount);
				return response.transactionSuccess();
			case COINSENGINE:
				CoinsEngineAPI.addBalance(player, (su.nightexpress.coinsengine.api.currency.Currency) currency, amount);
				return true;
			case THENEWECONOMY:
				return ((TNEAPI) api).setHoldings(player.getName(), player.getLocation().getWorld().getName(), ((net.tnemc.core.currency.Currency) currency).getIdentifier(), ((TNEAPI) api).getHoldings(player.getName(), player.getLocation().getWorld().getName(), ((net.tnemc.core.currency.Currency) currency).getIdentifier()).add(BigDecimal.valueOf(amount)));
			default:
		}
		return false;
	}
	public final boolean withdraw(final Player player, final Double amount) {
		switch(type) {
			case VAULT:
				final EconomyResponse response = ((Economy) api).withdrawPlayer(player, amount);
				return response.transactionSuccess();
			case COINSENGINE:
				CoinsEngineAPI.removeBalance(player, (su.nightexpress.coinsengine.api.currency.Currency) currency, amount);
				return true;
			case THENEWECONOMY:
				return ((TNEAPI) api).setHoldings(player.getName(), player.getLocation().getWorld().getName(), ((net.tnemc.core.currency.Currency) currency).getIdentifier(), ((TNEAPI) api).getHoldings(player.getName(), player.getLocation().getWorld().getName(), ((net.tnemc.core.currency.Currency) currency).getIdentifier()).subtract(BigDecimal.valueOf(amount)));
			default:
	}
	return false;
	}
	public final boolean has(final Player player, final Double amount) {
		switch(type) {
			case VAULT:
				return ((Economy) api).has(player, amount);
			case COINSENGINE:
				final Double balance = CoinsEngineAPI.getBalance(player, (su.nightexpress.coinsengine.api.currency.Currency) currency);
				return (balance >= amount);
			case THENEWECONOMY:
				return ((TNEAPI) api).hasHoldings(player.getName(), player.getLocation().getWorld().getName(), ((net.tnemc.core.currency.Currency) currency).getIdentifier(), BigDecimal.valueOf(amount));
			default:
	}
	return false;
	}
	public static final EconomyModule getInstance() {
		if(instance == null) instance = new EconomyModule();
		return instance;
	}
}