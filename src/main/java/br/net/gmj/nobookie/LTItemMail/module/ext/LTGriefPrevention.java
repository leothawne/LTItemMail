package br.net.gmj.nobookie.LTItemMail.module.ext;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import br.net.gmj.nobookie.LTItemMail.module.ConsoleModule;
import me.ryanhamshire.GriefPrevention.Claim;
import me.ryanhamshire.GriefPrevention.ClaimPermission;
import me.ryanhamshire.GriefPrevention.GriefPrevention;

public final class LTGriefPrevention implements LTExtension {
	private final Plugin plugin;
	public LTGriefPrevention(final Plugin plugin) {
		this.plugin = plugin;
	}
	@Override
	public final Plugin getBasePlugin() {
		return plugin;
	}
	public final boolean canBuildBreak(final Player player, final Location location) {
		final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false, false, null);
		Boolean result = true;
		if(claim != null) result = claim.hasExplicitPermission(player, ClaimPermission.Build);
		ConsoleModule.debug(getClass(), "#canBuildBreak: " + player.getName() + " " + result);
		return result;
	}
	public final boolean canInteract(final Player player, final Location location) {
		final Claim claim = GriefPrevention.instance.dataStore.getClaimAt(location, false, false, null);
		Boolean result = true;
		if(claim != null) result = claim.hasExplicitPermission(player, ClaimPermission.Inventory);
		ConsoleModule.debug(getClass(), "#canInteract: " + player.getName() + " " + result);
		return result;
	}
}