package io.github.leothawne.LTItemMail.listener;

import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public final class PlayerListener implements Listener {
	private static FileConfiguration configuration;
	private static HashMap<UUID, Boolean> playerBusy;
	public PlayerListener(final FileConfiguration configuration, final HashMap<UUID, Boolean> playerBusy) {
		PlayerListener.configuration = configuration;
		PlayerListener.playerBusy = playerBusy;
	}
	@EventHandler(priority = EventPriority.MONITOR)
	public final void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = (Player) event.getPlayer();
		PlayerListener.playerBusy.put(player.getUniqueId(), false);
		if(player.hasPermission("LTItemMail.use") && player.hasPermission("LTItemMail.admin")) {
			if(PlayerListener.configuration.getBoolean("update.check") == true) {
				player.performCommand("itemmailadmin version");
			}
		}
	}
}