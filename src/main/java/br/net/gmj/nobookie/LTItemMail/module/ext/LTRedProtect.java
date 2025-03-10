package br.net.gmj.nobookie.LTItemMail.module.ext;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import br.net.fabiozumbi12.RedProtect.Bukkit.RedProtect;
import br.net.fabiozumbi12.RedProtect.Bukkit.Region;
import br.net.gmj.nobookie.LTItemMail.module.ConsoleModule;

public final class LTRedProtect implements LTExtension {
	private final Plugin plugin;
	public LTRedProtect(final Plugin plugin) {
		this.plugin = plugin;
	}
	@Override
	public final Plugin getBasePlugin() {
		return plugin;
	}
	public final boolean canBuildBreak(final Player player, final Location location) {
		final Region region = RedProtect.get().getAPI().getRegion(location);
		Boolean result = true;
		if(region != null) result = (region.canBuild(player) && region.canBreak(location.getBlock().getType()));
		ConsoleModule.debug(getClass(), "#canBuildBreak: " + player.getName() + " " + result);
		return result;
	}
	public final boolean canInteract(final Player player, final Location location) {
		final Region region = RedProtect.get().getAPI().getRegion(location);
		Boolean result = true;
		if(region != null) result = region.canChest(player);
		ConsoleModule.debug(getClass(), "#canInteract: " + player.getName() + " " + result);
		return result;
	}
}