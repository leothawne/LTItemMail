package io.github.leothawne.LTItemMail.module.integration;

import org.bukkit.entity.Player;

import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import net.luckperms.api.util.Tristate;

public final class LTLuckPerms {
	private final LuckPerms api;
	public LTLuckPerms(final LuckPerms api) {
		this.api = api;
	}
	public final boolean hasPermission(final Player player, final String node) {
		final User user = api.getPlayerAdapter(Player.class).getUser(player);
		final Tristate state = user.getCachedData().getPermissionData().checkPermission(node);
		return state.asBoolean();
	}
}