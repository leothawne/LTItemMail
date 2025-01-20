package io.github.leothawne.LTItemMail.module;

import java.io.File;
import java.io.IOException;

import org.bukkit.Bukkit;

import com.google.common.io.Files;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.util.FetchUtil;

public final class ResourcePackModule {
	private ResourcePackModule() {}
	public static final void begin() {
		Bukkit.getScheduler().runTaskAsynchronously(LTItemMail.getInstance(), new Runnable() {
			@Override
			public final void run() {
				if(requestDownload()) if(moveFile()) {
					ConsoleModule.info(LanguageModule.I.g(LanguageModule.I.i.RP_I) + ".");
				} else ConsoleModule.warning(LanguageModule.I.g(LanguageModule.I.i.RP_F) + ".");
			}
		});
	}
	private static final boolean requestDownload() {
		return FetchUtil.URL.Cache.download(DataModule.getResourcePackURL(), "LTItemMail-ResourcePack.zip", false);
	}
	private static final boolean moveFile() {
		try {
			final File oldFile = FetchUtil.URL.Cache.get("LTItemMail-ResourcePack.zip");
			final File newFile = new File(LTItemMail.getInstance().getDataFolder(), "LTItemMail-ResourcePack.zip");
			Files.copy(oldFile, newFile);
			oldFile.delete();
			return true;
		} catch (final IOException e) {
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
		}
		return false;
	}
}