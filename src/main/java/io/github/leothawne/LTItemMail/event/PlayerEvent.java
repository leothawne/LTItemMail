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
package io.github.leothawne.LTItemMail.event;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerEvent implements Listener {
	private static FileConfiguration configuration;
	public PlayerEvent(FileConfiguration configuration) {
		PlayerEvent.configuration = configuration;
	}
	@EventHandler
	public static final void onPlayerJoin(PlayerJoinEvent event) {
		Player player = (Player) event.getPlayer();
		if(player.hasPermission("LTItemMail.use") && player.hasPermission("LTItemMail.admin")) {
			if(configuration.getBoolean("update.check") == true) {
				player.performCommand("itemmailadmin version");
			}
		}
	}
}