package io.github.leothawne.LTItemMail.module;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.module.api.LTBlueMap;
import io.github.leothawne.LTItemMail.module.api.LTDecentHolograms;
import io.github.leothawne.LTItemMail.module.api.LTDynmap;
import io.github.leothawne.LTItemMail.module.api.LTEssentialsX;
import io.github.leothawne.LTItemMail.module.api.LTGriefPrevention;
import io.github.leothawne.LTItemMail.module.api.LTPlaceholderAPI;
import io.github.leothawne.LTItemMail.module.api.LTRedProtect;
import io.github.leothawne.LTItemMail.module.api.LTTownyAdvanced;
import io.github.leothawne.LTItemMail.module.api.LTVault;
import io.github.leothawne.LTItemMail.module.api.LTWorldGuard;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public final class IntegrationModule {
	private static IntegrationModule instance = null;
	private final Map<Name, Plugin> plugins = new HashMap<>();
	private final Map<Function, Object> register = new HashMap<>();
	private IntegrationModule() {
		final PluginManager manager = Bukkit.getPluginManager();
		plugins.putIfAbsent(Name.VAULT, manager.getPlugin("Vault"));
		plugins.putIfAbsent(Name.GRIEFPREVENTION, manager.getPlugin("GriefPrevention"));
		plugins.putIfAbsent(Name.REDPROTECT, manager.getPlugin("RedProtect"));
		plugins.putIfAbsent(Name.TOWNYADVANCED, manager.getPlugin("Towny"));
		plugins.putIfAbsent(Name.WORLDGUARD, manager.getPlugin("WorldGuard"));
		plugins.putIfAbsent(Name.DYNMAP, manager.getPlugin("dynmap"));
		plugins.putIfAbsent(Name.BLUEMAP, manager.getPlugin("BlueMap"));
		plugins.putIfAbsent(Name.DECENTHOLOGRAMS, manager.getPlugin("DecentHolograms"));
		plugins.putIfAbsent(Name.PLACEHOLDERAPI, manager.getPlugin("PlaceholderAPI"));
		plugins.putIfAbsent(Name.ESSENTIALSX_ANTIBUILD, manager.getPlugin("EssentialsAntiBuild"));
	}
	private final void warn(final Name sourceName, final Name pluginName) {
		Plugin source = null;
		Plugin plugin = null;
		if(plugins.containsKey(sourceName)) source = plugins.get(sourceName);
		if(plugins.containsKey(pluginName)) plugin = plugins.get(pluginName);
		warn(source, plugin);
	}
	private final void warn(final Name sourceName, final Plugin plugin) {
		Plugin source = null;
		if(plugins.containsKey(sourceName)) source = plugins.get(sourceName);
		warn(source, plugin);
	}
	private final void warn(final Plugin source, final Plugin plugin) {
		String sourceName = "";
		if(source != null && source.isEnabled()) sourceName = " through " + source.getDescription().getName();
		if(plugin != null && plugin.isEnabled()) plugin.getLogger().info("Hooked into " + LTItemMail.getInstance().getDescription().getName() + sourceName);
	}
	private final boolean register(final Function function) {
		switch(function) {
			case VAULT_ECONOMY:
				final RegisteredServiceProvider<Economy> economy = Bukkit.getServicesManager().getRegistration(Economy.class);
				if(economy != null) register.putIfAbsent(function, new LTVault.Economy(economy.getProvider(), economy.getPlugin()));
				break;
			case VAULT_PERMISSION:
				final RegisteredServiceProvider<Permission> permission = Bukkit.getServicesManager().getRegistration(Permission.class);
				if(permission != null) register.putIfAbsent(function, new LTVault.Permission(permission.getProvider(), permission.getPlugin()));
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
			case BLUEMAP:
				register.putIfAbsent(function, new LTBlueMap());
				break;
			case DECENTHOLOGRAMS:
				register.putIfAbsent(function, new LTDecentHolograms());
				break;
			case PLACEHOLDERAPI:
				register.putIfAbsent(function, new LTPlaceholderAPI());
				break;
			case ESSENTIALSX_ANTIBUILD:
				register.putIfAbsent(function, new LTEssentialsX.AntiBuild());
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
		Boolean detected = false;
		for(final Name name : plugins.keySet()) if(isInstalled(name)) {
			detected = true;
			break;
		}
		if(detected) ConsoleModule.info("Loading integrations...");
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_VAULT)) if(isInstalled(IntegrationModule.Name.VAULT)) {
			warn(null, IntegrationModule.Name.VAULT);
			if(!isRegistered(IntegrationModule.Function.VAULT_ECONOMY)) if(register(IntegrationModule.Function.VAULT_ECONOMY)) warn(IntegrationModule.Name.VAULT, ((LTVault.Economy) get(IntegrationModule.Function.VAULT_ECONOMY)).getPlugin());
			if(!isRegistered(IntegrationModule.Function.VAULT_PERMISSION)) if(register(IntegrationModule.Function.VAULT_PERMISSION)) warn(IntegrationModule.Name.VAULT, ((LTVault.Permission) get(IntegrationModule.Function.VAULT_PERMISSION)).getPlugin());
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_GRIEFPREVENTION)) if(isInstalled(IntegrationModule.Name.GRIEFPREVENTION)) if(!isRegistered(IntegrationModule.Function.GRIEFPREVENTION)) {
			warn(null, IntegrationModule.Name.GRIEFPREVENTION);
			register(IntegrationModule.Function.GRIEFPREVENTION);
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_REDPROTECT)) if(isInstalled(IntegrationModule.Name.REDPROTECT)) if(!isRegistered(IntegrationModule.Function.REDPROTECT)) {
			warn(null, IntegrationModule.Name.REDPROTECT);
			register(IntegrationModule.Function.REDPROTECT);
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_TOWNYADVANCED)) if(isInstalled(IntegrationModule.Name.TOWNYADVANCED)) if(!isRegistered(IntegrationModule.Function.TOWNYADVANCED)) {
			warn(null, IntegrationModule.Name.TOWNYADVANCED);
			register(IntegrationModule.Function.TOWNYADVANCED);
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_WORLDGUARD)) if(isInstalled(IntegrationModule.Name.WORLDGUARD)) if(!isRegistered(IntegrationModule.Function.WORLDGUARD)) {
			warn(null, IntegrationModule.Name.WORLDGUARD);
			register(IntegrationModule.Function.WORLDGUARD);
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_DYNMAP)) if(isInstalled(IntegrationModule.Name.DYNMAP)) if(!isRegistered(IntegrationModule.Function.DYNMAP)) {
			warn(null, IntegrationModule.Name.DYNMAP);
			register(IntegrationModule.Function.DYNMAP);
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_BLUEMAP)) if(isInstalled(IntegrationModule.Name.BLUEMAP)) if(!isRegistered(IntegrationModule.Function.BLUEMAP)) {
			warn(null, IntegrationModule.Name.BLUEMAP);
			register(IntegrationModule.Function.BLUEMAP);
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_DECENTHOLOGRAMS)) if(isInstalled(IntegrationModule.Name.DECENTHOLOGRAMS)) if(!isRegistered(IntegrationModule.Function.DECENTHOLOGRAMS)) {
			warn(null, IntegrationModule.Name.DECENTHOLOGRAMS);
			register(IntegrationModule.Function.DECENTHOLOGRAMS);
		}
		if(isInstalled(IntegrationModule.Name.PLACEHOLDERAPI)) if(!isRegistered(IntegrationModule.Function.PLACEHOLDERAPI)) {
			warn(null, IntegrationModule.Name.PLACEHOLDERAPI);
			register(IntegrationModule.Function.PLACEHOLDERAPI);
		}
		if(detected) ConsoleModule.info("Integrations loaded.");
	}
	public enum Name {
		VAULT,
		GRIEFPREVENTION,
		REDPROTECT,
		TOWNYADVANCED,
		WORLDGUARD,
		DYNMAP,
		BLUEMAP,
		DECENTHOLOGRAMS,
		PLACEHOLDERAPI,
		ESSENTIALSX_ANTIBUILD
	}
	public enum Function {
		VAULT_ECONOMY,
		VAULT_PERMISSION,
		GRIEFPREVENTION,
		REDPROTECT,
		TOWNYADVANCED,
		WORLDGUARD,
		DYNMAP,
		BLUEMAP,
		DECENTHOLOGRAMS,
		PLACEHOLDERAPI,
		ESSENTIALSX_ANTIBUILD
	}
}