package io.github.leothawne.LTItemMail.module.api;

import org.bukkit.plugin.Plugin;

import io.github.leothawne.LTItemMail.module.api.model.IVault;

public final class LTVault {
	public static final class Economy implements IVault.Economy {
		private final net.milkbowl.vault.economy.Economy api;
		private final Plugin plugin;
		public Economy(final net.milkbowl.vault.economy.Economy api, final Plugin plugin) {
			this.api = api;
			this.plugin = plugin;
		}
		@Override
		public final net.milkbowl.vault.economy.Economy getAPI() {
			return api;
		}
		@Override
		public final Plugin getPlugin() {
			return plugin;
		}
	}
	public static final class Permission implements IVault.Permission {
		private final net.milkbowl.vault.permission.Permission api;
		private final Plugin plugin;
		public Permission(final net.milkbowl.vault.permission.Permission api, final Plugin plugin) {
			this.api = api;
			this.plugin = plugin;
		}
		@Override
		public final net.milkbowl.vault.permission.Permission getAPI() {
			return api;
		}
		@Override
		public final Plugin getPlugin() {
			return plugin;
		}
	}
}