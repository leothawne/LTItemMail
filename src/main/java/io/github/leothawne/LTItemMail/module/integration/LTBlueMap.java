package io.github.leothawne.LTItemMail.module.integration;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

import com.flowpowered.math.vector.Vector3d;

import de.bluecolored.bluemap.api.BlueMapAPI;
import de.bluecolored.bluemap.api.BlueMapMap;
import de.bluecolored.bluemap.api.BlueMapWorld;
import de.bluecolored.bluemap.api.gson.MarkerGson;
import de.bluecolored.bluemap.api.markers.Marker;
import de.bluecolored.bluemap.api.markers.MarkerSet;
import de.bluecolored.bluemap.api.markers.POIMarker;
import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;

public final class LTBlueMap {
	private BlueMapAPI api = null;
	public LTBlueMap() {
		BlueMapAPI.onEnable(api -> {
			this.api = api;
			final MarkerSet loadedSet = loadFromFile();
			if(loadedSet != null) for(final BlueMapWorld world : api.getWorlds()) for(final BlueMapMap map : world.getMaps()) {
				final MarkerSet set = MarkerSet.builder().label("Mailboxes").build();
				for(final String id : loadedSet.getMarkers().keySet()) {
					final String[] args = id.split("_");
					if(args[1].equals(world.getId())) set.getMarkers().put(id, loadedSet.getMarkers().get(id));
				}
				map.getMarkerSets().put("ltitemmail_markers", set);
			}
		});
	}
	public final void createMarker(final Player player, final Location location) {
		if(api != null) {
			final BlueMapWorld world = api.getWorld(location.getWorld()).get();
			MarkerSet set = null;
			for(final BlueMapMap map : world.getMaps()) for(final String setId: map.getMarkerSets().keySet()) if(setId.equals("ltitemmail_markers")) {
				set = map.getMarkerSets().get(setId);
				break;
			}
			if(set == null) set = MarkerSet.builder().label("Mailboxes").build();
			final String worldId = world.getId();
			final Integer x = location.getBlockX();
			final Integer y = location.getBlockY();
			final Integer z = location.getBlockZ();
			final String id = player.getName() + "_" + worldId + "_" + x + "_" + y + "_" + z;
			final POIMarker marker = new POIMarker(player.getName() + "'s Mailbox (" + x + ", " + y + ", " + z + ")", new Vector3d(x, y, z));
			marker.setMaxDistance(1000);
			if(set.getMarkers().containsKey(id)) set.getMarkers().remove(id);
			set.getMarkers().put(id, marker);
			for(final BlueMapMap cachedMap : world.getMaps()) {
				if(cachedMap.getMarkerSets().containsKey("ltitemmail_markers")) cachedMap.getMarkerSets().remove("ltitemmail_markers");
				cachedMap.getMarkerSets().put("ltitemmail_markers", set);
			}
			saveToFile();
		}
	}
	public final void deleteMarker(final OfflinePlayer player, final Location location) {
		if(api != null) {
			final BlueMapWorld world = api.getWorld(location.getWorld()).get();
			MarkerSet set = null;
			for(final BlueMapMap map : world.getMaps()) for(final String setId: map.getMarkerSets().keySet()) if(setId.equals("ltitemmail_markers")) {
				set = map.getMarkerSets().get(setId);
				break;
			}
			if(set != null) {
				final String worldId = world.getId();
				final Integer x = location.getBlockX();
				final Integer y = location.getBlockY();
				final Integer z = location.getBlockZ();
				final String id = player.getName() + "_" + worldId + "_" + x + "_" + y + "_" + z;
				if(set.getMarkers().containsKey(id)) set.getMarkers().remove(id);
				for(final BlueMapMap cachedMap : world.getMaps()) if(cachedMap.getMarkerSets().containsKey("ltitemmail_markers")) cachedMap.getMarkerSets().remove("ltitemmail_markers");
				saveToFile();
			}
		}
	}
	private final void saveToFile() {
		final MarkerSet set = MarkerSet.builder().label("Mailboxes").build();
		for(final BlueMapWorld world : api.getWorlds()) for(final BlueMapMap map : world.getMaps()) for(final Map.Entry<String, MarkerSet> currentSet : map.getMarkerSets().entrySet()) if(currentSet.getKey().equals("ltitemmail_markers")) for(final Map.Entry<String, Marker> currentMarker : currentSet.getValue().getMarkers().entrySet()) set.getMarkers().put(currentMarker.getKey(), currentMarker.getValue());
		try {
			final FileWriter writer = new FileWriter(new File(LTItemMail.getInstance().getDataFolder(), "bluemap-markers.json"));
			MarkerGson.INSTANCE.toJson(set, writer);
			writer.close();
		} catch (final IOException e) {
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
		}
	}
	private final MarkerSet loadFromFile() {
		final File markers = new File(LTItemMail.getInstance().getDataFolder(), "bluemap-markers.json");
		MarkerSet set = null;
		if(markers.exists()) try {
			final FileReader reader = new FileReader(markers);
			set = MarkerGson.INSTANCE.fromJson(reader, MarkerSet.class);
			reader.close();
		} catch (final IOException e) {
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
		}
		return set;
	}
}