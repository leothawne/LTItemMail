package io.github.leothawne.LTItemMail.module;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.module.integration.LTGriefPrevention;
import io.github.leothawne.LTItemMail.module.integration.LTRedProtect;
import io.github.leothawne.LTItemMail.module.integration.LTTownyAdvanced;
import io.github.leothawne.LTItemMail.module.integration.LTWorldGuard;
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
	private final void warn(final TPlugin TPlugin) {
		Plugin plugin = null;
		if(plugins.containsKey(TPlugin)) plugin = plugins.get(TPlugin);
		if(plugin != null && plugin.isEnabled()) plugin.getLogger().info("Hooked into " + LTItemMail.getInstance().getDescription().getName());
	}
	private final boolean register(final FPlugin FPlugin) {
		switch(FPlugin) {
			case VAULT_ECONOMY:
				if(isInstalled(TPlugin.VAULT)) {
					final RegisteredServiceProvider<Economy> rsp = Bukkit.getServicesManager().getRegistration(Economy.class);
					if(rsp != null) register.putIfAbsent(FPlugin, rsp.getProvider());
				}
				break;
			case GRIEF_PREVENTION_API:
				register.putIfAbsent(FPlugin, new LTGriefPrevention());
				break;
			case RED_PROTECT_API:
				register.putIfAbsent(FPlugin, new LTRedProtect());
				break;
			case TOWNY_ADVANCED_API:
				register.putIfAbsent(FPlugin, new LTTownyAdvanced());
				break;
			case WORLD_GUARD_API:
				register.putIfAbsent(FPlugin, new LTWorldGuard());
				break;
		}
		return isRegistered(FPlugin);
	}
	public static final IntegrationModule getInstance() {
		if(instance == null) instance = new IntegrationModule();
		return instance;
	}
	public final boolean isInstalled(final TPlugin TPlugin) {
		Plugin plugin = null;
		if(plugins.containsKey(TPlugin)) plugin = plugins.get(TPlugin);
		return (plugin != null && plugin.isEnabled());
	}
	public final boolean isRegistered(final FPlugin FPlugin) {
		return register.containsKey(FPlugin);
	}
	public final Object get(final FPlugin FPlugin) {
		if(register.containsKey(FPlugin)) return register.get(FPlugin);
		return null;
	}
	public final void load() {
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_VAULT)) {
			if(isInstalled(IntegrationModule.TPlugin.VAULT)) {
				if(!isRegistered(IntegrationModule.FPlugin.VAULT_ECONOMY)) {
					ConsoleModule.info("Vault found.");
					if(!register(IntegrationModule.FPlugin.VAULT_ECONOMY)) ConsoleModule.warning("Economy plugin not found. Waiting.");
				}
			} else ConsoleModule.warning("Vault not found. Waiting.");
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_GRIEFPREVENTION)) {
			if(isInstalled(IntegrationModule.TPlugin.GRIEF_PREVENTION)) {
				if(!isRegistered(IntegrationModule.FPlugin.GRIEF_PREVENTION_API)) {
					ConsoleModule.info("GriefPrevention found.");
					warn(IntegrationModule.TPlugin.GRIEF_PREVENTION);
					register(IntegrationModule.FPlugin.GRIEF_PREVENTION_API);
				}
			} else ConsoleModule.warning("GriefPrevention not found. Waiting.");
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_REDPROTECT)) {
			if(isInstalled(IntegrationModule.TPlugin.RED_PROTECT)) {
				if(!isRegistered(IntegrationModule.FPlugin.RED_PROTECT_API)) {
					ConsoleModule.info("RedProtect found.");
					warn(IntegrationModule.TPlugin.RED_PROTECT);
					register(IntegrationModule.FPlugin.RED_PROTECT_API);
				}
			} else ConsoleModule.warning("RedProtect not found. Waiting.");
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_TOWNYADVANCED)) {
			if(isInstalled(IntegrationModule.TPlugin.TOWNY_ADVANCED)) {
				if(!isRegistered(IntegrationModule.FPlugin.TOWNY_ADVANCED_API)) {
					ConsoleModule.info("Towny found.");
					warn(IntegrationModule.TPlugin.TOWNY_ADVANCED);
					register(IntegrationModule.FPlugin.TOWNY_ADVANCED_API);
				}
			} else ConsoleModule.warning("Towny not found. Waiting.");
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_WORLDGUARD)) {
			if(isInstalled(IntegrationModule.TPlugin.WORLD_GUARD)) {
				if(!isRegistered(IntegrationModule.FPlugin.WORLD_GUARD_API)) {
					ConsoleModule.info("WorldGuard found.");
					warn(IntegrationModule.TPlugin.WORLD_GUARD);
					register(IntegrationModule.FPlugin.WORLD_GUARD_API);
				}
			} else ConsoleModule.warning("WorldGuard not found. Waiting.");
		}
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