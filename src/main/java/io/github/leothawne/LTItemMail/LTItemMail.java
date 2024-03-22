package io.github.leothawne.LTItemMail;

import java.sql.Connection;
import java.sql.SQLException;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.LTItemMail.command.ItemMailAdminCommand;
import io.github.leothawne.LTItemMail.command.ItemMailCommand;
import io.github.leothawne.LTItemMail.command.MailItemCommand;
import io.github.leothawne.LTItemMail.command.tabCompleter.ItemMailAdminCommandTabCompleter;
import io.github.leothawne.LTItemMail.command.tabCompleter.ItemMailCommandTabCompleter;
import io.github.leothawne.LTItemMail.command.tabCompleter.MailItemCommandTabCompleter;
import io.github.leothawne.LTItemMail.lib.BStats;
import io.github.leothawne.LTItemMail.listener.MailboxBlockListener;
import io.github.leothawne.LTItemMail.listener.MailboxListener;
import io.github.leothawne.LTItemMail.listener.PlayerListener;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.ConsoleModule;
import io.github.leothawne.LTItemMail.module.DataModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import io.github.leothawne.LTItemMail.module.RecipeModule;
import io.github.leothawne.LTItemMail.module.integration.IntegrationModule;
import io.github.leothawne.LTItemMail.task.MailboxTask;
import io.github.leothawne.LTItemMail.task.VersionTask;

public final class LTItemMail extends JavaPlugin {
	private static LTItemMail instance;
	private final void registerEvents(final Listener...listeners) {
		for(final Listener listener : listeners) Bukkit.getServer().getPluginManager().registerEvents(listener, this);
	}
	private FileConfiguration configuration;
	private FileConfiguration language;
	private Connection connection;
	@Override
	public final void onEnable() {
		instance = this;
		new BStats(LTItemMail.getInstance(), 3647);
		ConsoleModule.Hello();
		loadConfig();
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_ENABLE)) {
			VersionTask.run();
			loadLang();
			new BukkitRunnable() {
				@Override
				public final void run() {
					loadIntegrations();
				}
			}.runTaskTimer(this, 0, 20 * 15);
			DatabaseModule.check();
			connection = DatabaseModule.load();
			final Integer dbVer = DatabaseModule.checkDbVer();
			if(dbVer < Integer.valueOf(DataModule.getVersion(DataModule.VersionType.DATABASE))) {
				for(Integer i = dbVer; i < Integer.valueOf(DataModule.getVersion(DataModule.VersionType.DATABASE)); i++) {
					ConsoleModule.warning("Updating database. (" + i + " -> " + (i + 1) + ")");
					if(DatabaseModule.updateDb(i)) {
						ConsoleModule.warning("Database updated. (" + i + " -> " + (i + 1) + ")");
					} else ConsoleModule.severe("Database update failed. (" + i + " -> " + (i + 1) + ")");
				}
			} else ConsoleModule.info("Database up to date. (" + dbVer + ")");
			RecipeModule.scheduleRegister();
			MailboxTask.run();
			registerEvents(new MailboxListener(),
					new PlayerListener(),
					new MailboxBlockListener());
			getCommand("itemmail").setExecutor(new ItemMailCommand());
			getCommand("itemmail").setTabCompleter(new ItemMailCommandTabCompleter());
			getCommand("itemmailadmin").setExecutor(new ItemMailAdminCommand());
			getCommand("itemmailadmin").setTabCompleter(new ItemMailAdminCommandTabCompleter());
			getCommand("mailitem").setExecutor(new MailItemCommand());
			getCommand("mailitem").setTabCompleter(new MailItemCommandTabCompleter());
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_UPDATE_CHECK)) Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "itemmailadmin update");
		} else {
			ConsoleModule.severe("You've choosen to disable me.");
			new BukkitRunnable() {
				@Override
				public final void run() {
					Bukkit.getPluginManager().disablePlugin(instance);
				}
			}.runTaskTimer(this, 0, 20);
		}
	}
	@Override
	public final void onDisable() {
		Bukkit.getScheduler().cancelTasks(this);
		try {
			connection.close();
		} catch (final SQLException e) {
			e.printStackTrace();
		}
	}
	public static final LTItemMail getInstance() {
		return instance;
	}
	public final FileConfiguration getConfiguration() {
		return configuration;
	}
	public final FileConfiguration getLanguage() {
		return language;
	}
	public final Connection getConnection() {
		return connection;
	}
	public final void reload() {
		loadConfig();
		loadLang();
	}
	private final void loadConfig() {
		ConfigurationModule.check();
		configuration = ConfigurationModule.load();
	}
	private final void loadLang() {
		LanguageModule.check();
		language = LanguageModule.load();
	}
	private final IntegrationModule integration = IntegrationModule.getInstance();
	private final void loadIntegrations() {
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_VAULT)) {
			if(integration.isInstalled(IntegrationModule.TPlugin.VAULT)) {
				if(!integration.isRegistered(IntegrationModule.FPlugin.VAULT_ECONOMY)) ConsoleModule.info("Vault found.");
				if(!integration.isRegistered(IntegrationModule.FPlugin.VAULT_ECONOMY)) if(!integration.register(IntegrationModule.FPlugin.VAULT_ECONOMY)) ConsoleModule.warning("Economy plugin not found. Waiting.");
			} else if(!integration.isRegistered(IntegrationModule.FPlugin.VAULT_ECONOMY)) ConsoleModule.warning("Vault not found. Waiting.");
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_GRIEFPREVENTION)) {
			if(integration.isInstalled(IntegrationModule.TPlugin.GRIEF_PREVENTION)) {
				if(!integration.isRegistered(IntegrationModule.FPlugin.GRIEF_PREVENTION_API)) ConsoleModule.info("GriefPrevention found.");
				if(!integration.isRegistered(IntegrationModule.FPlugin.GRIEF_PREVENTION_API)) integration.warn(IntegrationModule.TPlugin.GRIEF_PREVENTION);
				if(!integration.isRegistered(IntegrationModule.FPlugin.GRIEF_PREVENTION_API)) integration.register(IntegrationModule.FPlugin.GRIEF_PREVENTION_API);
			} else if(!integration.isRegistered(IntegrationModule.FPlugin.GRIEF_PREVENTION_API)) ConsoleModule.warning("GriefPrevention not found. Waiting.");
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_REDPROTECT)) {
			if(integration.isInstalled(IntegrationModule.TPlugin.RED_PROTECT)) {
				if(!integration.isRegistered(IntegrationModule.FPlugin.RED_PROTECT_API)) ConsoleModule.info("RedProtect found.");
				if(!integration.isRegistered(IntegrationModule.FPlugin.RED_PROTECT_API)) integration.warn(IntegrationModule.TPlugin.RED_PROTECT);
				if(!integration.isRegistered(IntegrationModule.FPlugin.RED_PROTECT_API)) integration.register(IntegrationModule.FPlugin.RED_PROTECT_API);
			} else if(!integration.isRegistered(IntegrationModule.FPlugin.RED_PROTECT_API)) ConsoleModule.warning("RedProtect not found. Waiting.");
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_TOWNYADVANCED)) {
			if(integration.isInstalled(IntegrationModule.TPlugin.TOWNY_ADVANCED)) {
				if(!integration.isRegistered(IntegrationModule.FPlugin.TOWNY_ADVANCED_API)) ConsoleModule.info("Towny found.");
				if(!integration.isRegistered(IntegrationModule.FPlugin.TOWNY_ADVANCED_API)) integration.warn(IntegrationModule.TPlugin.TOWNY_ADVANCED);
				if(!integration.isRegistered(IntegrationModule.FPlugin.TOWNY_ADVANCED_API)) integration.register(IntegrationModule.FPlugin.TOWNY_ADVANCED_API);
			} else if(!integration.isRegistered(IntegrationModule.FPlugin.TOWNY_ADVANCED_API)) ConsoleModule.warning("Towny not found. Waiting.");
		}
		if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_HOOK_WORLDGUARD)) {
			if(integration.isInstalled(IntegrationModule.TPlugin.WORLD_GUARD)) {
				if(!integration.isRegistered(IntegrationModule.FPlugin.WORLD_GUARD_API)) ConsoleModule.info("WorldGuard found.");
				if(!integration.isRegistered(IntegrationModule.FPlugin.WORLD_GUARD_API)) integration.warn(IntegrationModule.TPlugin.WORLD_GUARD);
				if(!integration.isRegistered(IntegrationModule.FPlugin.WORLD_GUARD_API)) integration.register(IntegrationModule.FPlugin.WORLD_GUARD_API);
			} else if(!integration.isRegistered(IntegrationModule.FPlugin.WORLD_GUARD_API)) ConsoleModule.warning("WorldGuard not found. Waiting.");
		}
	}
}