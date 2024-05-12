package io.github.leothawne.LTItemMail.module.integration;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.Region;

public final class LTRedProtect {
	public LTRedProtect() {}
	public final boolean canBuild(final Player player, final Location location) {
		final Region region = RedProtect.get().getAPI().getRegion(location);
		if(region != null) return (region.canBuild(player) && region.canBreak(location.getBlock().getType()));
		return true;
	}
}