package br.net.gmj.nobookie.LTItemMail.module;

import java.io.File;
import java.io.IOException;

import org.bukkit.scheduler.BukkitRunnable;

import com.google.common.io.Files;

import br.net.gmj.nobookie.LTItemMail.LTItemMail;
import br.net.gmj.nobookie.LTItemMail.util.FetchUtil;

public final class ResourcePackModule {
	private ResourcePackModule() {}
	public static final void begin() {
		new BukkitRunnable() {
			@Override
			public final void run() {
				if(requestDownload()) if(moveFile()) {
					ConsoleModule.info(LanguageModule.I.g(LanguageModule.I.i.RP_I) + ".");
				} else ConsoleModule.warning(LanguageModule.I.g(LanguageModule.I.i.RP_F) + ".");
			}
		}.runTask(LTItemMail.getInstance());
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