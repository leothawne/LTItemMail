package io.github.leothawne.LTItemMail.module;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.leothawne.LTItemMail.LTItemMail;
import net.md_5.bungee.api.ChatColor;

public final class ConfigurationModule {
	private ConfigurationModule() {}
	private static final File configFile = new File(LTItemMail.getInstance().getDataFolder(), "config.yml");
	public static final void check() {
		if(!configFile.exists()) {
			ConsoleModule.warning("Extracting config.yml...");
			LTItemMail.getInstance().saveDefaultConfig();
			ConsoleModule.info("Done.");
		}
	}
	public static final FileConfiguration load() {
		if(configFile.exists()) {
			final FileConfiguration configuration = new YamlConfiguration();
			try {
				configuration.load(configFile);
				ConsoleModule.info("Loaded config.yml.");
				if(configuration.getInt("config-version") != Integer.valueOf(DataModule.getVersion(DataModule.VersionType.CONFIG_YML))) {
					ConsoleModule.severe("config.yml file outdated. New settings will be added. Or you can manually delete the config file and let the plugin extract the new one.");
					configuration.set("config-version", Integer.valueOf(DataModule.getVersion(DataModule.VersionType.CONFIG_YML)));
					configuration.save(configFile);
				}
				return configuration;
			} catch (final IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		return null;
	}
	public static final Object get(final Type type) {
		Object result = null;
		String path = null;
		switch(type) {
			case MAILBOX_COST:
				result = 30.0;
				path = "mail.cost.value";
				break;
			case MAILBOX_NAME:
				result = "Mailbox";
				path = "mail.name";
				break;
			case MAILBOX_TITLE:
				result = false;
				path = "mail.use-title";
				break;
			case MAILBOX_TYPE_COST:
				result = false;
				path = "mail.cost.per-item";
				break;
			case PLUGIN_CONFIG:
				result = DataModule.getVersion(DataModule.VersionType.CONFIG_YML);
				path = "config-version";
				break;
			case PLUGIN_ENABLE:
				result = true;
				path = "plugin.enable";
				break;
			case PLUGIN_HOOK_VAULT:
				result = true;
				path = "hook.vault";
				break;
			case PLUGIN_TAG:
				result = "LTIM";
				path = "plugin.tag";
				break;
			case PLUGIN_TYPE_LANGUAGE:
				result = "english";
				path = "plugin.language";
				break;
			case PLUGIN_UPDATE_CHECK:
				result = true;
				path = "update.check";
				break;
			case PLUGIN_HOOK_GRIEFPREVENTION:
				result = true;
				path = "hook.griefprevention";
				break;
			case PLUGIN_HOOK_REDPROTECT:
				result = true;
				path = "hook.redprotect";
				break;
			case PLUGIN_HOOK_TOWNYADVANCED:
				result = true;
				path = "hook.towny";
				break;
			case PLUGIN_HOOK_WORLDGUARD:
				result = true;
				path = "hook.worldguard";
				break;
			case PLUGIN_DEBUG:
				result = false;
				path = "plugin.debug";
				break;
		}
		if(path != null) if(LTItemMail.getInstance().getConfiguration().isSet(path)) {
			result = LTItemMail.getInstance().getConfiguration().get(path);
			if(type.equals(Type.PLUGIN_TAG) || type.equals(Type.MAILBOX_NAME)) result = formatBukkitCodes((String) LTItemMail.getInstance().getConfiguration().get(path));
		} else if(result != null) {
			ConsoleModule.warning("Configuration fallback: [" + path + ":" + result + "]");
			LTItemMail.getInstance().getConfiguration().set(path, result);
			try {
				LTItemMail.getInstance().getConfiguration().save(configFile);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	private static final String formatBukkitCodes(final String text) {
		return ChatColor.translateAlternateColorCodes('&', text);
	}
	public enum Type {
		PLUGIN_ENABLE,
		PLUGIN_TYPE_LANGUAGE,
		PLUGIN_TAG,
		PLUGIN_HOOK_VAULT,
		PLUGIN_HOOK_GRIEFPREVENTION,
		PLUGIN_HOOK_REDPROTECT,
		PLUGIN_HOOK_TOWNYADVANCED,
		PLUGIN_HOOK_WORLDGUARD,
		PLUGIN_DEBUG,
		MAILBOX_TITLE,
		MAILBOX_TYPE_COST,
		MAILBOX_COST,
		MAILBOX_NAME,
		PLUGIN_UPDATE_CHECK,
		PLUGIN_CONFIG
	}
}