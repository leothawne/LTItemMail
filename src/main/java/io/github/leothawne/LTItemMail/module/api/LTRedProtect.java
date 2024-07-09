package io.github.leothawne.LTItemMail.module.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.Region;
import io.github.leothawne.LTItemMail.module.ConsoleModule;

public final class LTRedProtect {
	public final boolean canBuildBreak(final Player player, final Location location) {
		final Region region = RedProtect.get().getAPI().getRegion(location);
		Boolean result = true;
		if(region != null) result = (region.canBuild(player) && region.canBreak(location.getBlock().getType()));
		ConsoleModule.debug(getClass().getName() + "#canBuildBreak: " + player.getName() + " " + result);
		return result;
	}
	public final boolean canInteract(final Player player, final Location location) {
		final Region region = RedProtect.get().getAPI().getRegion(location);
		Boolean result = true;
		if(region != null) result = region.canChest(player);
		ConsoleModule.debug(getClass().getName() + "#canInteract: " + player.getName() + " " + result);
		return result;
	}
}