package io.github.leothawne.LTItemMail.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.PermissionModule;

public final class PlayerListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public final void onPlayerJoin(final PlayerJoinEvent event) {
		new BukkitRunnable() {
			@Override
			public final void run() {
				final Player player = (Player) event.getPlayer();
				if(PermissionModule.hasPermission(player, PermissionModule.Type.CMD_ADMIN_NOTIFY) && PermissionModule.hasPermission(player, PermissionModule.Type.CMD_ADMIN_UPDATE)) if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_UPDATE_CHECK)) player.performCommand("ltitemmail:itemmailadmin update");
				if(PermissionModule.hasPermission(player, PermissionModule.Type.CMD_PLAYER_NOTIFY) && PermissionModule.hasPermission(player, PermissionModule.Type.CMD_PLAYER_LIST)) player.performCommand("ltitemmail:itemmail list");
			}
		}.runTaskLater(LTItemMail.getInstance(), 20 * 5);
	}
}