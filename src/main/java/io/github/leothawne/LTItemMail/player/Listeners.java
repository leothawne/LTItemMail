package io.github.leothawne.LTItemMail.player;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import io.github.leothawne.LTItemMail.LTItemMailLoader;

public class Listeners implements Listener {
	private LTItemMailLoader plugin;
	public Listeners(LTItemMailLoader plugin) {
		this.plugin = plugin;
	}
	private FileConfiguration configuration = null;
	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		Player player = (Player) event.getPlayer();
		if(player.hasPermission("LTItemMail.use") && player.hasPermission("LTItemMail.admin")) {
			configuration = plugin.getConfig();
			if(configuration.getBoolean("update.check") == true) {
				player.performCommand("itemmailadmin version");
			}
		}
	}
}