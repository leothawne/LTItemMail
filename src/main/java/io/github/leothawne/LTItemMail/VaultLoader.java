/*
 * Copyright (C) 2019 Murilo Amaral Nappi (murilonappi@gmail.com)
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <http://www.gnu.org/licenses/>.
 */
package io.github.leothawne.LTItemMail;

import org.bukkit.plugin.RegisteredServiceProvider;

import net.milkbowl.vault.economy.Economy;

public class VaultLoader {
	private static LTItemMailLoader plugin;
	public VaultLoader(LTItemMailLoader plugin) {
		VaultLoader.plugin = plugin;
	}
	private static RegisteredServiceProvider<Economy> rsp = null;
	public static final boolean isInstalled() {
		if(plugin.getServer().getPluginManager().getPlugin("Vault") != null) {
			return true;
		}
		return false;
	}
	public static final boolean ready() {
		rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
		if(rsp != null) {
			return true;
		}
		return false;
	}
	public static final Economy getEconomy() {
		Economy economy = rsp.getProvider();
		if(economy != null) {
			return economy;
		}
		return null;
	}
}