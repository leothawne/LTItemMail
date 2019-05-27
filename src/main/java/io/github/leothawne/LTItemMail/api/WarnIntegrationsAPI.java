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
package io.github.leothawne.LTItemMail.api;

import java.util.LinkedList;

import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;

import io.github.leothawne.LTItemMail.LTItemMail;

public final class WarnIntegrationsAPI {
	public WarnIntegrationsAPI(final LTItemMail mainPlugin, final LinkedList<String> plugins) {
		final PluginManager manager = mainPlugin.getServer().getPluginManager();
		for(final String plugin : plugins) {
			final Plugin getPlugin = manager.getPlugin(plugin);
			if(getPlugin != null && getPlugin.isEnabled()) {
				getPlugin.getLogger().info(mainPlugin.getName() + " were successfully hooked into " + getPlugin.getName() + "!");
			}
		}
	}
}