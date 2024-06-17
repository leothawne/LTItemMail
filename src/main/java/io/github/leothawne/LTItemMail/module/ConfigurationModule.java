package io.github.leothawne.LTItemMail.module;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.util.BukkitUtil;

public final class ConfigurationModule {
	private ConfigurationModule() {}
	private static final File configFile = new File(LTItemMail.getInstance().getDataFolder(), "config.yml");
	public static final void check() {
		if(!configFile.exists()) {
			ConsoleModule.info("Extracting config.yml...");
			LTItemMail.getInstance().saveDefaultConfig();
			ConsoleModule.info("Done.");
		}
	}
	public static final FileConfiguration load() {
		if(configFile.exists()) {
			final FileConfiguration configuration = new YamlConfiguration();
			try {
				configuration.load(configFile);
				ConsoleModule.info("Configuration loaded.");
				if(configuration.getInt("config-version") != Integer.valueOf(DataModule.getVersion(DataModule.VersionType.CONFIG_YML))) {
					ConsoleModule.warning("Configuration outdated!");
					ConsoleModule.warning("New settings will be added.");
					configuration.set("config-version", Integer.valueOf(DataModule.getVersion(DataModule.VersionType.CONFIG_YML)));
					configuration.save(configFile);
				}
				return configuration;
			} catch (final IOException | InvalidConfigurationException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
		}
		return null;
	}
	public static final void setBoardRead(final Integer id) {
		final List<Integer> boards = getBoardsRead();
		if(!boards.contains(id)) {
			boards.add(id);
			LTItemMail.getInstance().getConfiguration().set("boards-read", boards);
			try {
				LTItemMail.getInstance().getConfiguration().save(configFile);
			} catch (final IOException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
		}
	}
	public static final void disableDatabaseConversion() {
		LTItemMail.getInstance().getConfiguration().set("database.convert", false);
		try {
			LTItemMail.getInstance().getConfiguration().save(configFile);
		} catch (final IOException e) {
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
		}
	}
	public static final List<Integer> getBoardsRead(){
		List<Integer> boards;
		if(LTItemMail.getInstance().getConfiguration().isSet("boards-read")) {
			boards = LTItemMail.getInstance().getConfiguration().getIntegerList("boards-read");
		} else boards = new ArrayList<>();
		return boards;
	}
	public static final Object get(final Type type) {
		Object result = null;
		String path = null;
		switch(type) {
			case MAILBOX_TEXTURES:
				result = false;
				path = "mail.textures";
				break;
			case MAILBOX_COST:
				result = 30.0;
				path = "mail.cost.value";
				break;
			case MAILBOX_NAME:
				result = "&3&lMailbox&r&4";
				path = "mail.name";
				break;
			case MAILBOX_DISPLAY:
				result = "CHAT";
				path = "mail.display";
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
				result = false;
				path = "hook.vault";
				break;
			case PLUGIN_TAG:
				result = "&6[LTIM]";
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
			case PLUGIN_UPDATE_PERIODIC_NOTIFICATION:
				result = true;
				path = "update.periodic-notification";
				break;
			case PLUGIN_HOOK_GRIEFPREVENTION:
				result = false;
				path = "hook.griefprevention";
				break;
			case PLUGIN_HOOK_REDPROTECT:
				result = false;
				path = "hook.redprotect";
				break;
			case PLUGIN_HOOK_TOWNYADVANCED:
				result = false;
				path = "hook.towny";
				break;
			case PLUGIN_HOOK_WORLDGUARD:
				result = false;
				path = "hook.worldguard";
				break;
			case PLUGIN_HOOK_DYNMAP:
				result = false;
				path = "hook.dynmap";
				break;
			case PLUGIN_HOOK_BLUEMAP:
				result = false;
				path = "hook.bluemap";
				break;
			case PLUGIN_HOOK_DECENTHOLOGRAMS:
				result = false;
				path = "hook.decentholograms";
				break;
			case PLUGIN_DEBUG:
				result = false;
				path = "plugin.debug";
				break;
			case DATABASE_TYPE:
				result = "flatfile";
				path = "database.type";
				break;
			case DATABASE_CONVERT:
				result = false;
				path = "database.convert";
				break;
			case DATABASE_FLATFILE_FILE:
				result = "mailboxes.db";
				path = "database.flatfile.file";
				break;
			case DATABASE_MYSQL_HOST:
				result = "127.0.0.1:3306";
				path = "database.mysql.host";
				break;
			case DATABASE_MYSQL_USER:
				result = "root";
				path = "database.mysql.user";
				break;
			case DATABASE_MYSQL_PASSWORD:
				result = "pass";
				path = "database.mysql.password";
				break;
			case DATABASE_MYSQL_NAME:
				result = "ltitemmail";
				path = "database.mysql.database";
				break;
			case BUNGEE_MODE:
				result = false;
				path = "plugin.bungee-mode";
				break;
		}
		if(path != null) if(LTItemMail.getInstance().getConfiguration().isSet(path)) {
			result = LTItemMail.getInstance().getConfiguration().get(path);
			if(type.equals(Type.PLUGIN_TAG) || type.equals(Type.MAILBOX_NAME) || type.equals(Type.MAILBOX_NAME)) result = BukkitUtil.format((String) result);
		} else if(result != null) {
			ConsoleModule.info("Configuration fallback: [" + path + ":" + result + "]");
			LTItemMail.getInstance().getConfiguration().set(path, result);
			try {
				LTItemMail.getInstance().getConfiguration().save(configFile);
			} catch (final IOException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
		}
		return result;
	}
	public static final void addMissing() {
		for(final Type type : Type.values()) get(type);
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
		PLUGIN_HOOK_DYNMAP,
		PLUGIN_HOOK_BLUEMAP,
		PLUGIN_HOOK_DECENTHOLOGRAMS,
		PLUGIN_DEBUG,
		MAILBOX_DISPLAY,
		MAILBOX_TYPE_COST,
		MAILBOX_COST,
		MAILBOX_NAME,
		MAILBOX_TEXTURES,
		PLUGIN_UPDATE_CHECK,
		PLUGIN_UPDATE_PERIODIC_NOTIFICATION,
		PLUGIN_CONFIG,
		DATABASE_TYPE,
		DATABASE_CONVERT,
		DATABASE_FLATFILE_FILE,
		DATABASE_MYSQL_HOST,
		DATABASE_MYSQL_USER,
		DATABASE_MYSQL_PASSWORD,
		DATABASE_MYSQL_NAME,
		BUNGEE_MODE
	}
}