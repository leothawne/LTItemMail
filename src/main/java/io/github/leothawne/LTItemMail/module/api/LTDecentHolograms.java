package io.github.leothawne.LTItemMail.module.api;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;
import org.bukkit.entity.Player;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import net.md_5.bungee.api.ChatColor;

public final class LTDecentHolograms {
	private final List<Material> randomItems;
	private final Random random;
	public LTDecentHolograms() {
		randomItems = new ArrayList<>();
		random = new Random();
		for(final Material material : Material.values()) {
			final String name = material.toString();
			if(name.endsWith("_AXE") || name.endsWith("_PICKAXE") || name.endsWith("_SHOVEL") || name.endsWith("_HOE")) randomItems.add(material);
		}
	}
	public final void createHolo(final Player player, final Location location) {
		final World world = location.getWorld();
		final Integer x = location.getBlockX();
		final Integer y = location.getBlockY() + 2;
		final Integer z = location.getBlockZ();
		final String id = player.getName() + "_" + world.getName() + "_" + x + "_" + y + "_" + z;
		final Location holoLocation = new Location(world, x + 0.5, y + 0.5, z + 0.5);
		if(DHAPI.getHologram(id) != null) deleteHolo(player, location);
		final Hologram holo = DHAPI.createHologram(id, holoLocation, true);
		DHAPI.addHologramLine(holo, randomItems.get(random.nextInt(randomItems.size() - 1)));
		DHAPI.addHologramLine(holo, ChatColor.GOLD + player.getName() + "'s Mailbox");
	}
	public final void deleteHolo(final OfflinePlayer player, final Location location) {
		final String world = location.getWorld().getName();
		final Integer x = location.getBlockX();
		final Integer y = location.getBlockY() + 2;
		final Integer z = location.getBlockZ();
		final String id = player.getName() + "_" + world + "_" + x + "_" + y + "_" + z;
		if(DHAPI.getHologram(id) != null) DHAPI.removeHologram(id);
	}
}