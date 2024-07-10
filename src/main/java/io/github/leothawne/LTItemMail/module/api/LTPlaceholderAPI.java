package io.github.leothawne.LTItemMail.module.api;

import org.bukkit.OfflinePlayer;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.entity.LTPlayer;
import io.github.leothawne.LTItemMail.module.LanguageModule;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;

public final class LTPlaceholderAPI extends PlaceholderExpansion {
	public LTPlaceholderAPI() {
		register();
	}
	public final void unload() {
		unregister();
	}
	@Override
	public final String getIdentifier() {
		return LTItemMail.getInstance().getDescription().getName().toLowerCase();
	}
	@Override
	public final String getAuthor() {
		return LTItemMail.getInstance().getDescription().getAuthors().get(0);
	}
	@Override
	public final String getVersion() {
		return LTItemMail.getInstance().getDescription().getVersion();
	}
	@Override
	public final boolean persist() {
		return true;
	}
	@Override
	public final String onRequest(final OfflinePlayer player, final String placeholder) {
		LTPlayer ltPlayer = null;
		if(player != null) ltPlayer = LTPlayer.fromName(player.getName());
		if(ltPlayer != null && ltPlayer.isRegistered() && placeholder != null) switch(placeholder.toLowerCase()) {
			case "player_mailsent":
				return String.valueOf(ltPlayer.getMailSentCount());
			case "player_mailreceived":
				return String.valueOf(ltPlayer.getMailReceivedCount());
			case "player_banreason":
				return ltPlayer.getBanReason();
			case "player_registered":
				return ltPlayer.getRegistryDate();
			case "player_banned":
				if(ltPlayer.isBanned()) return LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_BANNED_YES);
				return LanguageModule.get(LanguageModule.Type.COMMAND_PLAYER_INFO_BANNED_NO);
		}
		return null;
	}
}