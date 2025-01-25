package br.net.gmj.nobookie.LTItemMail.module.ext;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;

import br.net.gmj.nobookie.LTItemMail.module.ConsoleModule;

public final class LTTownyAdvanced {
	public final boolean canBuild(final Player player, final Location location) {
		final TownBlock block = TownyAPI.getInstance().getTownBlock(location);
		Boolean result = true;
		if(block != null) result = PlayerCacheUtil.getCachePermission(player, location, location.getBlock().getType(), TownyPermission.ActionType.BUILD);
		ConsoleModule.debug(getClass().getName() + "#canBuild: " + player.getName() + " " + result);
		return result;
	}
	public final boolean canBreak(final Player player, final Location location) {
		final TownBlock block = TownyAPI.getInstance().getTownBlock(location);
		Boolean result = true;
		if(block != null) result = PlayerCacheUtil.getCachePermission(player, location, location.getBlock().getType(), TownyPermission.ActionType.DESTROY);
		ConsoleModule.debug(getClass().getName() + "#canBreak: " + player.getName() + " " + result);
		return result;
	}
	public final boolean canInteract(final Player player, final Location location) {
		final TownBlock block = TownyAPI.getInstance().getTownBlock(location);
		Boolean result = true;
		if(block != null) result = PlayerCacheUtil.getCachePermission(player, location, location.getBlock().getType(), TownyPermission.ActionType.ITEM_USE);
		ConsoleModule.debug(getClass().getName() + "#canInteract: " + player.getName() + " " + result);
		return result;
	}
}