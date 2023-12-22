package io.github.leothawne.LTItemMail.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import io.github.leothawne.LTItemMail.LTItemMail;

public final class PlayerListener implements Listener {
	public PlayerListener() {}
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = (Player) event.getPlayer();
		if(player.hasPermission("LTItemMail.admin")) if(LTItemMail.getInstance().getConfiguration().getBoolean("update.check")) player.performCommand("itemmailadmin update");
		if(player.hasPermission("LTItemMail.use")) player.performCommand("itemmail list");
	}
}