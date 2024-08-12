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
		Bukkit.getScheduler().runTask(LTItemMail.getInstance(), new Runnable() {
			@Override
			public final void run() {
				if(requestDownload()) if(moveFile()) {
					ConsoleModule.info("The resource pack should be available inside the plugin folder.");
				} else ConsoleModule.warning("Could not move the resource pack file from the cache folder.");
			}
		});
	}
	private static final boolean requestDownload() {
		return FetchUtil.URL.Cache.download(DataModule.getResourcePack(), "LTItemMail-ResourcePack", false);
	}
	private static final boolean moveFile() {
		try {
			final File oldFile = FetchUtil.URL.Cache.get("LTItemMail-ResourcePack");
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