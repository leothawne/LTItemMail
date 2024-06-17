package io.github.leothawne.LTItemMail.module.api;

import org.bukkit.plugin.Plugin;

public interface IVault {
	public interface Economy {
		public net.milkbowl.vault.economy.Economy getAPI();
		public Plugin getPlugin();
	}
	public interface Permission {
		public net.milkbowl.vault.permission.Permission getAPI();
		public Plugin getPlugin();
	}
}