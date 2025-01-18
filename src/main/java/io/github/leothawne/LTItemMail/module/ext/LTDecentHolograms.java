package io.github.leothawne.LTItemMail.module.ext;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import eu.decentsoftware.holograms.api.DHAPI;
import eu.decentsoftware.holograms.api.holograms.Hologram;
import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.block.MailboxBlock;
import io.github.leothawne.LTItemMail.module.ConsoleModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import net.md_5.bungee.api.ChatColor;

public final class LTDecentHolograms {
	private final List<Material> randomItems;
	public LTDecentHolograms() {
		randomItems = new ArrayList<>();
		for(final Material material : Material.values()) {
			final String name = material.toString();
			if(name.endsWith("_AXE") || name.endsWith("_PICKAXE") || name.endsWith("_SHOVEL") || name.endsWith("_HOE")) randomItems.add(material);
		}
		Collections.shuffle(randomItems);
	}
	public final void cleanup() {
		Bukkit.getScheduler().runTaskLater(LTItemMail.getInstance(), new Runnable() {
			@Override
			public final void run() {
				ConsoleModule.debug(getClass().getName() + "#cleanup: performing");
				for(final MailboxBlock block : DatabaseModule.Block.getMailboxBlocks()) deleteHolo(Bukkit.getOfflinePlayer(block.getOwner()), block.getLocation());
				for(final MailboxBlock block : DatabaseModule.Block.getMailboxBlocks()) createHolo(Bukkit.getOfflinePlayer(block.getOwner()), block.getLocation());
				ConsoleModule.debug(getClass().getName() + "#cleanup: done");
			}
		}, 1);
	}
	public final void createHolo(final OfflinePlayer player, final Location location) {
		final World world = location.getWorld();
		final Integer x = location.getBlockX();
		final Integer y = location.getBlockY() + 2;
		final Integer z = location.getBlockZ();
		final String id = player.getName() + "_" + world.getName() + "_" + x + "_" + y + "_" + z;
		final Location holoLocation = new Location(world, x + 0.5, y + 0.5, z + 0.5);
		if(DHAPI.getHologram(id) != null) deleteHolo(player, location);
		final Hologram holo = DHAPI.createHologram(id, holoLocation, true);
		DHAPI.addHologramLine(holo, randomItems.get(new Random().nextInt(randomItems.size() - 1)));
		DHAPI.addHologramLine(holo, "<#ANIM:wave:" + ChatColor.GREEN + "" + ChatColor.BOLD + "," + ChatColor.GOLD + "" + ChatColor.BOLD + ">" + LanguageModule.get(LanguageModule.Type.BLOCK_NAME) + "</#ANIM>");
		DHAPI.addHologramLine(holo, ChatColor.GOLD + LanguageModule.get(LanguageModule.Type.BLOCK_OWNER) + " " + ChatColor.AQUA + player.getName());
		ConsoleModule.debug(getClass().getName() + "#createHolo: " + id);
	}
	public final void deleteHolo(final OfflinePlayer player, final Location location) {
		final String world = location.getWorld().getName();
		final Integer x = location.getBlockX();
		final Integer y = location.getBlockY() + 2;
		final Integer z = location.getBlockZ();
		final String id = player.getName() + "_" + world + "_" + x + "_" + y + "_" + z;
		if(DHAPI.getHologram(id) != null) {
			DHAPI.removeHologram(id);
			ConsoleModule.debug(getClass().getName() + "#deleteHolo: " + id);
		}
	}
}