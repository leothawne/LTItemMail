package br.net.gmj.nobookie.LTItemMail.util;

import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import br.net.gmj.nobookie.LTItemMail.LTItemMail;
import br.net.gmj.nobookie.LTItemMail.entity.LTPlayer;

public final class Toasts {
	private Toasts() {}
	private static final String getIcon(final Type type) {
		switch(type) {
			case MAILBOX:
				return Material.WHITE_SHULKER_BOX.toString().toLowerCase();
			case DEBUG:
				return Material.BARRIER.toString().toLowerCase();
		}
		return null;
	}
	@SuppressWarnings("deprecation")
	private static final void createAdvancement(final String message, final Type icon, final Style style, final NamespacedKey key) {
		Bukkit.getUnsafe().loadAdvancement(key, "{\n" +
				"    \"criteria\": {\n" +
				"        \"trigger\": {\n" +
				"            \"trigger\": \"minecraft:impossible\"\n" +
				"        }\n" +
				"    },\n" +
				"    \"display\": {\n" +
				"        \"icon\": {\n" +
				"            \"item\": \"minecraft:" + getIcon(icon) + "\"\n" +
				"        },\n" +
				"        \"title\": {\n" +
				"            \"text\": \"" + message.replace("|", "\n") + "\"\n" +
				"        },\n" +
				"        \"description\": {\n" +
				"            \"text\": \"\"\n" +
				"        },\n" +
				"        \"background\": \"minecraft:textures/gui/advancements/backgrounds/adventure.png\",\n" +
				"        \"frame\": \"" + style.toString().toLowerCase() + "\",\n" +
				"        \"announce_to_chat\": false,\n" +
				"        \"show_toast\": true,\n" +
				"        \"hidden\": true\n" +
				"    },\n" +
				"    \"requirements\": [\n" +
				"        [\n" +
				"            \"trigger\"\n" +
				"        ]\n" +
				"    ]\n" +
				"}");
	}
	private static void grantAdvancement(final Player player, final NamespacedKey key) {
		player.getAdvancementProgress(Bukkit.getAdvancement(key)).awardCriteria("trigger");
	}
	private static void revokeAdvancement(final Player player, final NamespacedKey key) {
		player.getAdvancementProgress(Bukkit.getAdvancement(key)).revokeCriteria("trigger");
	}
	public static final void display(final LTPlayer player, final String message, final Type icon) {
		final Player bukkitPlayer = player.getBukkitPlayer().getPlayer();
		if(bukkitPlayer != null) {
			final NamespacedKey key = new NamespacedKey(LTItemMail.getInstance(), UUID.randomUUID().toString());
			createAdvancement(message, icon, Style.TASK, key);
			grantAdvancement(bukkitPlayer, key);
			new BukkitRunnable() {
				@Override
				public final void run() {
					revokeAdvancement(bukkitPlayer, key);
				}
			}.runTaskLater(LTItemMail.getInstance(), 10);
		}
	}
	public enum Type {
		MAILBOX,
		DEBUG
	}
	private enum Style {
		GOAL,
		TASK,
		CHALLENGE
	}
}