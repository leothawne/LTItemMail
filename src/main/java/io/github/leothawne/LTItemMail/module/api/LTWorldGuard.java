package io.github.leothawne.LTItemMail.module.api;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;

public final class LTWorldGuard {
	public final boolean canBuild(final Player player, final Location location) {
		final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		final RegionQuery query = container.createQuery();
		return query.testState(BukkitAdapter.adapt(location), WorldGuardPlugin.inst().wrapPlayer(player), Flags.BLOCK_PLACE);
	}
	public final boolean canBreak(final Player player, final Location location) {
		final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		final RegionQuery query = container.createQuery();
		return query.testState(BukkitAdapter.adapt(location), WorldGuardPlugin.inst().wrapPlayer(player), Flags.BLOCK_BREAK);
	}
	public final boolean canInteract(final Player player, final Location location) {
		final RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
		final RegionQuery query = container.createQuery();
		return query.testState(BukkitAdapter.adapt(location), WorldGuardPlugin.inst().wrapPlayer(player), Flags.INTERACT);
	}
}