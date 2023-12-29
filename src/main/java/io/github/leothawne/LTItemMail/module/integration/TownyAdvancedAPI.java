package io.github.leothawne.LTItemMail.module.integration;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.palmergames.bukkit.towny.TownyAPI;
import com.palmergames.bukkit.towny.object.TownBlock;
import com.palmergames.bukkit.towny.object.TownyPermission;
import com.palmergames.bukkit.towny.utils.PlayerCacheUtil;

public final class TownyAdvancedAPI {
	protected TownyAdvancedAPI() {}
	public final boolean canBuild(final Player player, final Location location) {
		final TownBlock block = TownyAPI.getInstance().getTownBlock(location);
		if(block != null) return PlayerCacheUtil.getCachePermission(player, location, location.getBlock().getType(), TownyPermission.ActionType.BUILD);
		return true;
	}
	public final boolean canBreak(final Player player, final Location location) {
		final TownBlock block = TownyAPI.getInstance().getTownBlock(location);
		if(block != null) return PlayerCacheUtil.getCachePermission(player, location, location.getBlock().getType(), TownyPermission.ActionType.DESTROY);
		return true;
	}
}