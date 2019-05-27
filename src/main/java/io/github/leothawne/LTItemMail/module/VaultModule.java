/*
 * Copyright (C) 2019 Murilo Amaral Nappi
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
package io.github.leothawne.LTItemMail.module;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.RegisteredServiceProvider;

import io.github.leothawne.LTItemMail.LTItemMail;
import net.milkbowl.vault.economy.Economy;

public final class VaultModule {
	public static final boolean isVaultInstalled(final LTItemMail plugin) {
		final Plugin vault = plugin.getServer().getPluginManager().getPlugin("Vault");
		return (vault != null && vault.isEnabled());
	}
	public static final Economy getEconomy(final LTItemMail plugin) {
		final RegisteredServiceProvider<Economy> rsp = plugin.getServer().getServicesManager().getRegistration(Economy.class);
		if(rsp != null) {
			return rsp.getProvider();
		}
		return null;
	}
}