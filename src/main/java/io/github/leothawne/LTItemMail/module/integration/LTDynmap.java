package io.github.leothawne.LTItemMail.module.integration;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.dynmap.DynmapCommonAPI;
import org.dynmap.DynmapCommonAPIListener;
import org.dynmap.markers.Marker;
import org.dynmap.markers.MarkerAPI;
import org.dynmap.markers.MarkerIcon;
import org.dynmap.markers.MarkerSet;

import io.github.leothawne.LTItemMail.LTItemMail;

public final class LTDynmap extends DynmapCommonAPIListener {
	private MarkerAPI api = null;
	public LTDynmap(){
		DynmapCommonAPIListener.register(this);
	}
	@Override
	public final void apiEnabled(final DynmapCommonAPI api) {
		if(api != null) this.api = api.getMarkerAPI();
	}
	public final void unregister() {
		DynmapCommonAPIListener.unregister(this);
	}
	public final void createMarker(final Player player, final Location location) {
		if(api != null) {
			final MarkerSet set = getSet();
			final String world = location.getWorld().getName();
			final Integer x = location.getBlockX();
			final Integer y = location.getBlockY();
			final Integer z = location.getBlockZ();
			final String id = player.getName() + "_" + world + "_" + x + "_" + y + "_" + z;
			deleteMarker(player, location);
			set.createMarker(id, player.getName() + "'s Mailbox (" + x + ", " + y + ", " + z + ")", false, world, x, y, z, getIcon(), true);
		}
	}
	public final void deleteMarker(final OfflinePlayer player, final Location location) {
		if(api != null) {
			final MarkerSet set = getSet();
			final Marker marker = set.findMarker(player.getName() + "_" + location.getWorld().getName() + "_" + location.getBlockX() + "_" + location.getBlockY() + "_" + location.getBlockZ());
			if(marker != null) marker.deleteMarker();
		}
	}
	private final MarkerSet getSet() {
		MarkerSet set = api.getMarkerSet("ltitemmail_markers");
		if(set == null) set = api.createMarkerSet("ltitemmail_markers", "Mailboxes", null, true);
		set.setDefaultMarkerIcon(getIcon());
		return set;
	}
	private final MarkerIcon getIcon() {
		MarkerIcon icon = api.getMarkerIcon("ltitemmail_mailbox");
		if(icon == null) icon = api.createMarkerIcon("ltitemmail_mailbox", "Mailboxes", LTItemMail.getInstance().getResource("mailbox_icon.png"));
		return icon;
	}
}