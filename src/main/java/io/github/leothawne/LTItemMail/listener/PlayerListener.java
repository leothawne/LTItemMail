package io.github.leothawne.LTItemMail.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.module.PermissionModule;

public final class PlayerListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public final void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = (Player) event.getPlayer();
		if(PermissionModule.hasPermission(player, PermissionModule.Type.CMD_ADMIN_NOTIFY)) if(LTItemMail.getInstance().getConfiguration().getBoolean("update.check")) player.performCommand("itemmailadmin update");
		if(PermissionModule.hasPermission(player, PermissionModule.Type.CMD_PLAYER_NOTIFY)) player.performCommand("itemmail list");
	}
}