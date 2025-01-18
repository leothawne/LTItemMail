package io.github.leothawne.LTItemMail.module.ext;

import org.bukkit.plugin.Plugin;

public final class LTVault {
	public static final class Permission {
		private final net.milkbowl.vault.permission.Permission api;
		private final Plugin plugin;
		public Permission(final net.milkbowl.vault.permission.Permission api, final Plugin plugin) {
			this.api = api;
			this.plugin = plugin;
		}
		public final net.milkbowl.vault.permission.Permission getAPI() {
			return api;
		}
		public final Plugin getPlugin() {
			return plugin;
		}
	}
}