package io.github.leothawne.LTItemMail.module.integration;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public final class LTGriefPrevention {
	public final boolean canBuildBreak(final Player player, final Location location) {
		final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false, false, null);
		if(claim != null) return claim.hasExplicitPermission(player, ClaimPermission.Build);
		return true;
	}
	public final boolean canInteract(final Player player, final Location location) {
		final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false, false, null);
		if(claim != null) return claim.hasExplicitPermission(player, ClaimPermission.Inventory);
		return true;
	}
}