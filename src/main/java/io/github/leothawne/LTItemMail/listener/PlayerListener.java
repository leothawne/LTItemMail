package io.github.leothawne.LTItemMail.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.PermissionModule;

public final class PlayerListener implements Listener {
	public PlayerListener() {
		Bukkit.getServer().getPluginManager().registerEvents(this, LTItemMail.getInstance());
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onPlayerJoin(final PlayerJoinEvent event) {
		final Player player = (Player) event.getPlayer();
		DatabaseModule.User.updateUUID(player);
		Bukkit.getScheduler().runTaskLater(LTItemMail.getInstance(), new Runnable() {
			@Override
			public final void run() {
				if(PermissionModule.hasPermission(player, PermissionModule.Type.CMD_ADMIN_NOTIFY) && PermissionModule.hasPermission(player, PermissionModule.Type.CMD_ADMIN_UPDATE)) if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_UPDATE_CHECK)) player.performCommand("ltitemmail:itemmailadmin update");
				if(PermissionModule.hasPermission(player, PermissionModule.Type.CMD_PLAYER_NOTIFY) && PermissionModule.hasPermission(player, PermissionModule.Type.CMD_PLAYER_LIST)) player.performCommand("ltitemmail:itemmail list");
			}
		}, 20 * 5);
	}
}