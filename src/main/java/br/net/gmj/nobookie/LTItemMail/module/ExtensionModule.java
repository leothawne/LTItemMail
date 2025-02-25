package br.net.gmj.nobookie.LTItemMail.module;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.server.ServerCommandEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;

import br.net.gmj.nobookie.LTItemMail.LTItemMail;
import br.net.gmj.nobookie.LTItemMail.module.ext.LTBlueMap;
import br.net.gmj.nobookie.LTItemMail.module.ext.LTDecentHolograms;
import br.net.gmj.nobookie.LTItemMail.module.ext.LTDynmap;
import br.net.gmj.nobookie.LTItemMail.module.ext.LTGriefPrevention;
import br.net.gmj.nobookie.LTItemMail.module.ext.LTHeadDatabase;
import br.net.gmj.nobookie.LTItemMail.module.ext.LTPlaceholderAPI;
import br.net.gmj.nobookie.LTItemMail.module.ext.LTRedProtect;
import br.net.gmj.nobookie.LTItemMail.module.ext.LTTownyAdvanced;
import br.net.gmj.nobookie.LTItemMail.module.ext.LTUltimateAdvancementAPI;
import br.net.gmj.nobookie.LTItemMail.module.ext.LTVault;
import br.net.gmj.nobookie.LTItemMail.module.ext.LTWorldGuard;
import br.net.gmj.nobookie.LTItemMail.module.ext.listener.LTGriefPreventionListener;
import br.net.gmj.nobookie.LTItemMail.module.ext.listener.LTRedProtectListener;
import br.net.gmj.nobookie.LTItemMail.module.ext.listener.LTTownyListener;
import br.net.gmj.nobookie.LTItemMail.util.FetchUtil;
import net.milkbowl.vault.permission.Permission;

public final class ExtensionModule {
	private static ExtensionModule instance = null;
	private final Map<Name, Plugin> plugins = new HashMap<>();
	private final Map<Function, Object> register = new HashMap<>();
	private ExtensionModule() {
		final PluginManager manager = Bukkit.getPluginManager();
		plugins.putIfAbsent(Name.VAULT, manager.getPlugin("Vault"));
		plugins.putIfAbsent(Name.COINSENGINE, manager.getPlugin("CoinsEngine"));
		plugins.putIfAbsent(Name.THENEWECONOMY, manager.getPlugin("TheNewEconomy"));
		plugins.putIfAbsent(Name.GRIEFPREVENTION, manager.getPlugin("GriefPrevention"));
		plugins.putIfAbsent(Name.REDPROTECT, manager.getPlugin("RedProtect"));
		plugins.putIfAbsent(Name.TOWNYADVANCED, manager.getPlugin("Towny"));
		plugins.putIfAbsent(Name.WORLDGUARD, manager.getPlugin("WorldGuard"));
		plugins.putIfAbsent(Name.DYNMAP, manager.getPlugin("dynmap"));
		plugins.putIfAbsent(Name.BLUEMAP, manager.getPlugin("BlueMap"));
		plugins.putIfAbsent(Name.DECENTHOLOGRAMS, manager.getPlugin("DecentHolograms"));
		plugins.putIfAbsent(Name.PLACEHOLDERAPI, manager.getPlugin("PlaceholderAPI"));
		plugins.putIfAbsent(Name.ULTIMATEADVANCEMENTAPI, manager.getPlugin("UltimateAdvancementAPI"));
		plugins.putIfAbsent(Name.HEADDATABASE, manager.getPlugin("HeadDatabase"));
	}
	public final void warn(final Name sourceName, final Name pluginName) {
		Plugin source = null;
		Plugin plugin = null;
		if(plugins.containsKey(sourceName)) source = plugins.get(sourceName);
		if(plugins.containsKey(pluginName)) plugin = plugins.get(pluginName);
		warn(source, plugin);
	}
	public final void warn(final Name sourceName, final Plugin plugin) {
		Plugin source = null;
		if(plugins.containsKey(sourceName)) source = plugins.get(sourceName);
		warn(source, plugin);
	}
	public final void warn(final Plugin source, final Plugin plugin) {
		String sourceName = "";
		if(source != null && source.isEnabled()) sourceName = " through " + source.getDescription().getName();
		if(plugin != null && plugin.isEnabled()) plugin.getLogger().info("Hooked into " + LTItemMail.getInstance().getDescription().getName() + sourceName);
	}
	private final boolean register(final Function function) {
		switch(function) {
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
			case ULTIMATEADVANCEMENTAPI:
				register.putIfAbsent(function, new LTUltimateAdvancementAPI());
				break;
			case HEADDATABASE:
				register.putIfAbsent(function, new LTHeadDatabase());
				break;
		}
		return isRegistered(function);
	}
	public final void unload() {
		if(isRegistered(Function.DYNMAP)) ((LTDynmap) get(Function.DYNMAP)).unload();
		if(isRegistered(Function.BLUEMAP)) ((LTBlueMap) get(Function.BLUEMAP)).unload();
		if(isRegistered(Function.PLACEHOLDERAPI)) ((LTPlaceholderAPI) get(Function.PLACEHOLDERAPI)).unload();
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
		if(detected) ConsoleModule.info("Loading extensions...");
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_ECONOMY_ENABLE)) EconomyModule.init();
		if(isInstalled(Name.VAULT)) if(!isRegistered(Function.VAULT_PERMISSION)) if(register(Function.VAULT_PERMISSION)) warn(Name.VAULT, ((LTVault.Permission) get(Function.VAULT_PERMISSION)).getPlugin());
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_GRIEFPREVENTION)) if(isInstalled(Name.GRIEFPREVENTION)) if(!isRegistered(Function.GRIEFPREVENTION)) {
			warn(null, Name.GRIEFPREVENTION);
			register(Function.GRIEFPREVENTION);
			new LTGriefPreventionListener();
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_REDPROTECT)) if(isInstalled(Name.REDPROTECT)) if(!isRegistered(Function.REDPROTECT)) {
			warn(null, Name.REDPROTECT);
			register(Function.REDPROTECT);
			new LTRedProtectListener();
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_TOWNYADVANCED)) if(isInstalled(Name.TOWNYADVANCED)) if(!isRegistered(Function.TOWNYADVANCED)) {
			warn(null, Name.TOWNYADVANCED);
			register(Function.TOWNYADVANCED);
			new LTTownyListener();
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_WORLDGUARD)) if(isInstalled(Name.WORLDGUARD)) if(!isRegistered(Function.WORLDGUARD)) {
			warn(null, Name.WORLDGUARD);
			register(Function.WORLDGUARD);
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_DYNMAP)) if(isInstalled(Name.DYNMAP)) if(!isRegistered(Function.DYNMAP)) {
			warn(null, Name.DYNMAP);
			register(Function.DYNMAP);
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_BLUEMAP)) if(isInstalled(Name.BLUEMAP)) if(!isRegistered(Function.BLUEMAP)) {
			warn(null, Name.BLUEMAP);
			register(Function.BLUEMAP);
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_DECENTHOLOGRAMS)) if(isInstalled(Name.DECENTHOLOGRAMS)) if(!isRegistered(Function.DECENTHOLOGRAMS)) {
			warn(null, Name.DECENTHOLOGRAMS);
			register(Function.DECENTHOLOGRAMS);
			if(isRegistered(Function.DECENTHOLOGRAMS)) ((LTDecentHolograms) get(Function.DECENTHOLOGRAMS)).cleanup();
		}
		if(isInstalled(Name.PLACEHOLDERAPI)) if(!isRegistered(Function.PLACEHOLDERAPI)) {
			warn(null, Name.PLACEHOLDERAPI);
			register(Function.PLACEHOLDERAPI);
		}
		Boolean toastFallback = false;
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_ULTIMATEADVANCEMENTAPI)) {
			if(isInstalled(Name.ULTIMATEADVANCEMENTAPI)) {
				if(!isRegistered(Function.ULTIMATEADVANCEMENTAPI)) {
					warn(null, Name.ULTIMATEADVANCEMENTAPI);
					register(Function.ULTIMATEADVANCEMENTAPI);
				}
			} else toastFallback = true;
		} else toastFallback = true;
		if(toastFallback && ((String) ConfigurationModule.get(ConfigurationModule.Type.MAILBOX_DISPLAY)).equalsIgnoreCase("TOAST")) {
			ConsoleModule.warning("You must install and enable UltimateAdvancementAPI in config.yml to use TOAST notifications. Falling back to CHAT notifications.");
			LTItemMail.getInstance().configuration.set(ConfigurationModule.Type.MAILBOX_DISPLAY.path(), "CHAT");
			try {
				LTItemMail.getInstance().configuration.save(FetchUtil.FileManager.get("config.yml"));
			} catch (final IOException e) {
				ConsoleModule.severe("Error while saving config.yml.");
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
		}
		if(isInstalled(Name.HEADDATABASE)) if(!isRegistered(Function.HEADDATABASE)) {
			warn(null, Name.HEADDATABASE);
			register(Function.HEADDATABASE);
		}
		if(detected) {
			ConsoleModule.info("Extensions loaded.");
		} else ConsoleModule.info("No extensions detected.");
		new PlugManSafeGuard();
	}
	private final class PlugManSafeGuard implements Listener {
		private PlugManSafeGuard() {
			try {
				Plugin plugMan = null;
				if(Bukkit.getPluginManager().isPluginEnabled("PlugManX")) {
					 plugMan = Bukkit.getPluginManager().getPlugin("PlugManX");
				} else if(Bukkit.getPluginManager().isPluginEnabled("PlugMan")) plugMan = Bukkit.getPluginManager().getPlugin("PlugMan");
				if(plugMan != null) {
					ConsoleModule.warning("PlugMan detected! Reloading LT Item Mail with PlugMan can cause issues. Safe Guard activated!");
					if(!ConfigurationModule.devMode) Bukkit.getPluginManager().registerEvents(this, LTItemMail.getInstance());
					final FileConfiguration config = plugMan.getConfig();
					final List<String> ignored = config.getStringList("ignored-plugins");
					if(!ignored.contains(LTItemMail.getInstance().getDescription().getName())){
						ignored.add(LTItemMail.getInstance().getDescription().getName());
						config.set("ignored-plugins", ignored);
						config.save(new File(plugMan.getDataFolder(), "config.yml"));
					}
				}
			} catch(final Exception e) {}
		}
		@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
		public final void onServerCommandEvent(final ServerCommandEvent event) {
			final String[] command = event.getCommand().split(" ");
			event.setCancelled(prevent(event.getSender(), command));
		}
		@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
		public final void onPlayerCommandEvent(final PlayerCommandPreprocessEvent event) {
			final String[] command = event.getMessage().replace("/", "").split(" ");
			event.setCancelled(prevent(event.getPlayer(), command));
		}
		private final boolean prevent(final CommandSender sender, final String[] command) {
			Boolean plugman = false;
			for(final String cmd : command) {
				if(cmd.equalsIgnoreCase("plugman")) plugman = true;
				if(plugman && cmd.equalsIgnoreCase(LTItemMail.getInstance().getDescription().getName())) {
					sender.sendMessage(ChatColor.DARK_RED + "Don't! >:(");
					return true;
				}
			}
			return false;
		}
	}
	public static final ExtensionModule reload() {
		if(instance != null) {
			instance.unload();
			instance = null;
			instance = new ExtensionModule();
			return instance;
		}
		return getInstance();
	}
	public static final ExtensionModule getInstance() {
		if(instance == null) instance = new ExtensionModule();
		return instance;
	}
	public enum Name {
		VAULT,
		COINSENGINE,
		THENEWECONOMY,
		GRIEFPREVENTION,
		REDPROTECT,
		TOWNYADVANCED,
		WORLDGUARD,
		DYNMAP,
		BLUEMAP,
		DECENTHOLOGRAMS,
		PLACEHOLDERAPI,
		ULTIMATEADVANCEMENTAPI,
		HEADDATABASE
	}
	public enum Function {
		VAULT_PERMISSION,
		GRIEFPREVENTION,
		REDPROTECT,
		TOWNYADVANCED,
		WORLDGUARD,
		DYNMAP,
		BLUEMAP,
		DECENTHOLOGRAMS,
		PLACEHOLDERAPI,
		ULTIMATEADVANCEMENTAPI,
		HEADDATABASE
	}
}