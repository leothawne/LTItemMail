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