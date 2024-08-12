package io.github.leothawne.LTItemMail.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.ConsoleModule;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import me.marnic.jdl.CombinedSpeedProgressDownloadHandler;
import me.marnic.jdl.Downloader;
import me.marnic.jdl.SizeUtil;

public final class FetchUtil {
	private FetchUtil() {}
	public static final class URL {
		private static final URLConnection connect(final String url) {
			URLConnection connection = null;
			try {
				connection = URI.create(url).toURL().openConnection();
				connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");
				connection.setUseCaches(false);
				connection.connect();
			} catch (final IOException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return connection;
		}
		public static final String get(final String url) {
			try {
				final InputStreamReader input = new InputStreamReader(connect(url).getInputStream(), Charset.forName("UTF-8"));
				final BufferedReader reader = new BufferedReader(input);
				final StringBuilder builder = new StringBuilder();
				String string;
				while((string = reader.readLine()) != null) builder.append(string);
				reader.close();
				input.close();
				return builder.toString();
			} catch(final IOException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return null;
		}
		public static final class Cache {
			/*public static final boolean download(final String url, final String name) {
				try {
					Files.createDirectories(Paths.get(LTItemMail.getInstance().getDataFolder() + File.separator + "cache"));
					final ReadableByteChannel byteChannel = Channels.newChannel(connect(url).getInputStream());
					final FileOutputStream output = new FileOutputStream(new File(LTItemMail.getInstance().getDataFolder() + File.separator + "cache", name + ".tmp"));
					final FileChannel fileChannel = output.getChannel();
					fileChannel.transferFrom(byteChannel, 0, Long.MAX_VALUE);
					fileChannel.close();
					output.close();
					byteChannel.close();
					return true;
				} catch (final IOException e) {
					if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
				}
				return false;
			}*/
			public static final boolean download(final String url, final String name, final Boolean silent) {
				try {
					Files.createDirectories(Paths.get(LTItemMail.getInstance().getDataFolder() + File.separator + "cache"));
					final Downloader artifact = new Downloader(false);
					artifact.setDownloadHandler(new CombinedSpeedProgressDownloadHandler(artifact) {
						@Override
						public final void onDownloadStart() {
							super.onDownloadStart();
							if(!silent) ConsoleModule.info("Resource download started! [" + name + "]");
						}
						@Override
						public final void onDownloadSpeedProgress(final int downloadedSize, final int maxSize, final int downloadPercent, final int bytesPerSec) {
							if(!silent) ConsoleModule.info("Downloading resource [" + name + "]: " + downloadedSize + "/" + maxSize + " MB (" + downloadPercent + "%, " + SizeUtil.toMBFB(bytesPerSec) + " MB/s)");
						}
						@Override
						public final void onDownloadFinish() {
							super.onDownloadFinish();
							if(!silent) ConsoleModule.info("Resource download completed! [" + name + "]");
						}
						@Override
						public final void onDownloadError() {
							super.onDownloadError();
							if(!silent) {
								ConsoleModule.warning("Resource download failed! [" + name + "]");
							} else ConsoleModule.debug("Resource download failed! [" + name + "]");
						}
					});
					artifact.downloadFileToLocation(url, new File(LTItemMail.getInstance().getDataFolder() + File.separator + "cache" + File.separator + name + ".tmp").getAbsolutePath());
					return true;
				} catch (final IOException e) {
					if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
				}
				return false;
			}
			public static final File get(final String name) {
				final File file = new File(LTItemMail.getInstance().getDataFolder() + File.separator + "cache", name + ".tmp");
				if(file.exists() && file.isFile()) return file;
				return null;
			}
		}
	}
	public static final class Player {
		private static final JSONArray array() {
			final File cache = new File(Bukkit.getWorldContainer().getAbsolutePath(), "usercache.json");
			try {
				return (JSONArray) new JSONParser().parse(new FileReader(cache));
			} catch (final IOException | ParseException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return null;
		}
		public static final UUID fromName(final String name) {
			final UUID uuid = DatabaseModule.User.Cache.getUUID(name);
			if(uuid != null) {
				return uuid;
			} else if(array() != null) for(int i = 0; i < array().size(); i++) {
				final JSONObject user = (JSONObject) array().get(i);
				if(name.equalsIgnoreCase((String) user.get("name"))) return UUID.fromString((String) user.get("uuid"));
			}
			return null;
		}
		public static final String fromUUID(final UUID uuid) {
			final String name = DatabaseModule.User.Cache.getName(uuid);
			if(name != null) {
				return name;
			} else if(array() != null) for(int i = 0; i < array().size(); i++) {
				final JSONObject user = (JSONObject) array().get(i);
				if(uuid.equals(UUID.fromString((String) user.get("uuid")))) return (String) user.get("name");
			}
			return null;
		}
	}
}