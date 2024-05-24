package io.github.leothawne.LTItemMail.module;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.module.integration.LTBlueMap;
import io.github.leothawne.LTItemMail.module.integration.LTDecentHolograms;
import io.github.leothawne.LTItemMail.module.integration.LTDynmap;
import io.github.leothawne.LTItemMail.module.integration.LTGriefPrevention;
import io.github.leothawne.LTItemMail.module.integration.LTLuckPerms;
import io.github.leothawne.LTItemMail.module.integration.LTPlaceholderAPI;
import io.github.leothawne.LTItemMail.module.integration.LTRedProtect;
import io.github.leothawne.LTItemMail.module.integration.LTTownyAdvanced;
import io.github.leothawne.LTItemMail.module.integration.LTVault;
import io.github.leothawne.LTItemMail.module.integration.LTWorldGuard;
import net.luckperms.api.LuckPerms;
import net.milkbowl.vault.economy.Economy;

public final class IntegrationModule {
	private static IntegrationModule instance = null;
	private final HashMap<Name, Plugin> plugins = new HashMap<>();
	private final HashMap<Function, Object> register = new HashMap<>();
	private IntegrationModule() {
		final PluginManager manager = Bukkit.getPluginManager();
		plugins.putIfAbsent(Name.VAULT, manager.getPlugin("Vault"));
		plugins.putIfAbsent(Name.GRIEFPREVENTION, manager.getPlugin("GriefPrevention"));
		plugins.putIfAbsent(Name.REDPROTECT, manager.getPlugin("RedProtect"));
		plugins.putIfAbsent(Name.TOWNYADVANCED, manager.getPlugin("Towny"));
		plugins.putIfAbsent(Name.WORLDGUARD, manager.getPlugin("WorldGuard"));
		plugins.putIfAbsent(Name.DYNMAP, manager.getPlugin("dynmap"));
		plugins.putIfAbsent(Name.LUCKPERMS, manager.getPlugin("LuckPerms"));
		plugins.putIfAbsent(Name.BLUEMAP, manager.getPlugin("BlueMap"));
		plugins.putIfAbsent(Name.DECENTHOLOGRAMS, manager.getPlugin("DecentHolograms"));
		plugins.putIfAbsent(Name.PLACEHOLDERAPI, manager.getPlugin("PlaceholderAPI"));
	}
	private final void warn(final Name name) {
		Plugin plugin = null;
		if(plugins.containsKey(name)) plugin = plugins.get(name);
		warn(plugin);
	}
	private final void warn(final Plugin plugin) {
		if(plugin != null && plugin.isEnabled()) plugin.getLogger().info("Hooked into " + LTItemMail.getInstance().getDescription().getName());
	}
	private final boolean register(final Function function) {
		switch(function) {
			case VAULT_ECONOMY:
				final RegisteredServiceProvider<Economy> vaultEconomy = Bukkit.getServicesManager().getRegistration(Economy.class);
				if(vaultEconomy != null) {
					final LTVault vault = new LTVault();
					vault.setEconomyPlugin(vaultEconomy.getPlugin());
					vault.setEconomy(vaultEconomy.getProvider());
					register.putIfAbsent(function, vault);
				}
				break;
			case GRIEFPREVENTION:
				register.putIfAbsent(function, new LTGriefPrevention());
				break;
			case REDPROTECT:
				register.putIfAbsent(function, new LTRedProtect());
				break;
			case TOWNYADVANCED:
				register.putIfAbsent(function, new LTTownyAdvanced());
				break;
			case WORLDGUARD:
				register.putIfAbsent(function, new LTWorldGuard());
				break;
			case DYNMAP:
				register.putIfAbsent(function, new LTDynmap());
				break;
			case LUCKPERMS:
				final RegisteredServiceProvider<LuckPerms> luckPerms = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
				if(luckPerms != null) register.putIfAbsent(function, new LTLuckPerms(luckPerms.getProvider()));
				break;
			case BLUEMAP:
				register.putIfAbsent(function, new LTBlueMap());
				break;
			case DECENTHOLOGRAMS:
				register.putIfAbsent(function, new LTDecentHolograms());
				break;
			case PLACEHOLDERAPI:
				register.putIfAbsent(function, new LTPlaceholderAPI());
				break;
		}
		return isRegistered(function);
	}
	public final void unload() {
		if(isRegistered(Function.DYNMAP)) ((LTDynmap) get(Function.DYNMAP)).unregister();
	}
	public static final IntegrationModule getInstance() {
		if(instance == null) instance = new IntegrationModule();
		return instance;
	}
	public final boolean isInstalled(final Name name) {
		Plugin plugin = null;
		if(plugins.containsKey(name)) plugin = plugins.get(name);
		return (plugin != null && plugin.isEnabled());
	}
	public final boolean isRegistered(final Function function) {
		return register.containsKey(function);
	}
	public final Object get(final Function function) {
		if(isRegistered(function)) return register.get(function);
		return null;
	}
	public final void load() {
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_VAULT)) if(isInstalled(IntegrationModule.Name.VAULT)) if(!isRegistered(IntegrationModule.Function.VAULT_ECONOMY)) {
			warn(IntegrationModule.Name.VAULT);
			if(register(IntegrationModule.Function.VAULT_ECONOMY)) warn(((LTVault) get(IntegrationModule.Function.VAULT_ECONOMY)).getEconomyPlugin());
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_GRIEFPREVENTION)) if(isInstalled(IntegrationModule.Name.GRIEFPREVENTION)) if(!isRegistered(IntegrationModule.Function.GRIEFPREVENTION)) {
			warn(IntegrationModule.Name.GRIEFPREVENTION);
			register(IntegrationModule.Function.GRIEFPREVENTION);
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_REDPROTECT)) if(isInstalled(IntegrationModule.Name.REDPROTECT)) if(!isRegistered(IntegrationModule.Function.REDPROTECT)) {
			warn(IntegrationModule.Name.REDPROTECT);
			register(IntegrationModule.Function.REDPROTECT);
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_TOWNYADVANCED)) if(isInstalled(IntegrationModule.Name.TOWNYADVANCED)) if(!isRegistered(IntegrationModule.Function.TOWNYADVANCED)) {
			warn(IntegrationModule.Name.TOWNYADVANCED);
			register(IntegrationModule.Function.TOWNYADVANCED);
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_WORLDGUARD)) if(isInstalled(IntegrationModule.Name.WORLDGUARD)) if(!isRegistered(IntegrationModule.Function.WORLDGUARD)) {
			warn(IntegrationModule.Name.WORLDGUARD);
			register(IntegrationModule.Function.WORLDGUARD);
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_DYNMAP)) if(isInstalled(IntegrationModule.Name.DYNMAP)) if(!isRegistered(IntegrationModule.Function.DYNMAP)) {
			warn(IntegrationModule.Name.DYNMAP);
			register(IntegrationModule.Function.DYNMAP);
		}
		if(isInstalled(IntegrationModule.Name.LUCKPERMS)) if(!isRegistered(IntegrationModule.Function.LUCKPERMS)) {
			warn(IntegrationModule.Name.LUCKPERMS);
			register(IntegrationModule.Function.LUCKPERMS);
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_BLUEMAP)) if(isInstalled(IntegrationModule.Name.BLUEMAP)) if(!isRegistered(IntegrationModule.Function.BLUEMAP)) {
			warn(IntegrationModule.Name.BLUEMAP);
			register(IntegrationModule.Function.BLUEMAP);
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_DECENTHOLOGRAMS)) if(isInstalled(IntegrationModule.Name.DECENTHOLOGRAMS)) if(!isRegistered(IntegrationModule.Function.DECENTHOLOGRAMS)) {
			warn(IntegrationModule.Name.DECENTHOLOGRAMS);
			register(IntegrationModule.Function.DECENTHOLOGRAMS);
		}
		if(isInstalled(IntegrationModule.Name.PLACEHOLDERAPI)) if(!isRegistered(IntegrationModule.Function.PLACEHOLDERAPI)) {
			warn(IntegrationModule.Name.PLACEHOLDERAPI);
			register(IntegrationModule.Function.PLACEHOLDERAPI);
		}
	}
	public enum Name {
		VAULT,
		GRIEFPREVENTION,
		REDPROTECT,
		TOWNYADVANCED,
		WORLDGUARD,
		DYNMAP,
		LUCKPERMS,
		BLUEMAP,
		DECENTHOLOGRAMS,
		PLACEHOLDERAPI
	}
	public enum Function {
		VAULT_ECONOMY,
		GRIEFPREVENTION,
		REDPROTECT,
		TOWNYADVANCED,
		WORLDGUARD,
		DYNMAP,
		LUCKPERMS,
		BLUEMAP,
		DECENTHOLOGRAMS,
		PLACEHOLDERAPI
	}
}