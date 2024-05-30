package io.github.leothawne.LTItemMail.module.integration;

import org.bukkit.plugin.Plugin;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;

public final class LTVault {
	public static final class EconomyHolder {
		private final Economy api;
		private final Plugin plugin;
		private EconomyHolder(final Economy api, final Plugin plugin) {
			this.api = api;
			this.plugin = plugin;
		}
		public final Economy getAPI() {
			return api;
		}
		public final Plugin getPlugin() {
			return plugin;
		}
		public static final EconomyHolder create(final Economy api, final Plugin plugin) {
			return new EconomyHolder(api, plugin);
		}
	}
	public static final class PermissionHolder {
		private final Permission api;
		private final Plugin plugin;
		private PermissionHolder(final Permission api, final Plugin plugin) {
			this.api = api;
			this.plugin = plugin;
		}
		public final Permission getAPI() {
			return api;
		}
		public final Plugin getPlugin() {
			return plugin;
		}
		public static final PermissionHolder create(final Permission api, final Plugin plugin) {
			return new PermissionHolder(api, plugin);
		}
	}
}