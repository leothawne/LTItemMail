package io.github.leothawne.LTItemMail.module.integration;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import io.github.leothawne.LTItemMail.LTItemMail;
import net.milkbowl.vault.economy.Economy;

public final class IntegrationModule {
	private static IntegrationModule instance = null;
	private final HashMap<TPlugin, Plugin> plugins = new HashMap<>();
	private final HashMap<FPlugin, Object> register = new HashMap<>();
	private IntegrationModule() {
		plugins.putIfAbsent(TPlugin.VAULT, Bukkit.getPluginManager().getPlugin("Vault"));
		plugins.putIfAbsent(TPlugin.GRIEF_PREVENTION, Bukkit.getPluginManager().getPlugin("GriefPrevention"));
		plugins.putIfAbsent(TPlugin.RED_PROTECT, Bukkit.getPluginManager().getPlugin("RedProtect"));
		plugins.putIfAbsent(TPlugin.TOWNY_ADVANCED, Bukkit.getPluginManager().getPlugin("Towny"));
		plugins.putIfAbsent(TPlugin.WORLD_GUARD, Bukkit.getPluginManager().getPlugin("WorldGuard"));
	}
	public static final IntegrationModule getInstance(final boolean restart) {
		if(restart && instance != null) instance = null;
		if(instance == null) instance = new IntegrationModule();
		return instance;
	}
	public final void warn(final TPlugin TPlugin) {
		Plugin plugin = null;
		if(plugins.containsKey(TPlugin)) plugin = plugins.get(TPlugin);
		if(plugin != null && plugin.isEnabled()) plugin.getLogger().info("Hooked into " + LTItemMail.getInstance().getDescription().getName());
	}
	public final boolean isInstalled(final TPlugin TPlugin) {
		Plugin plugin = null;
		if(plugins.containsKey(TPlugin)) plugin = plugins.get(TPlugin);
		return (plugin != null && plugin.isEnabled());
	}
	public final boolean register(final FPlugin FPlugin) {
		switch(FPlugin) {
			case VAULT_ECONOMY:
				if(isInstalled(TPlugin.VAULT)) {
					final RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
					if(rsp != null) register.putIfAbsent(FPlugin, rsp);
				}
				break;
			case GRIEF_PREVENTION_API:
				register.putIfAbsent(FPlugin, new GriefPreventionAPI());
				break;
			case RED_PROTECT_API:
				register.putIfAbsent(FPlugin, new RedProtectAPI());
				break;
			case TOWNY_ADVANCED_API:
				register.putIfAbsent(FPlugin, new TownyAdvancedAPI());
				break;
			case WORLD_GUARD_API:
				register.putIfAbsent(FPlugin, new WorldGuardAPI());
				break;
		}
		return register.containsKey(FPlugin);
	}
	public final Object get(final FPlugin FPlugin) {
		if(register.containsKey(FPlugin)) return register.get(FPlugin);
		return null;
	}
	public enum TPlugin {
		VAULT,
		GRIEF_PREVENTION,
		RED_PROTECT,
		TOWNY_ADVANCED,
		WORLD_GUARD
	}
	public enum FPlugin {
		VAULT_ECONOMY,
		GRIEF_PREVENTION_API,
		RED_PROTECT_API,
		TOWNY_ADVANCED_API,
		WORLD_GUARD_API
	}
}