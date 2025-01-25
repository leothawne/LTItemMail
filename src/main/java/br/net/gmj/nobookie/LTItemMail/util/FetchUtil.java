package br.net.gmj.nobookie.LTItemMail.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;
import java.nio.charset.Charset;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import br.net.gmj.nobookie.LTItemMail.LTItemMail;
import br.net.gmj.nobookie.LTItemMail.module.ConfigurationModule;
import br.net.gmj.nobookie.LTItemMail.module.ConsoleModule;
import br.net.gmj.nobookie.LTItemMail.module.DatabaseModule;
import br.net.gmj.nobookie.LTItemMail.module.LanguageModule;
import javadl.Downloader;
import javadl.handler.CompleteDownloadHandler;
import javadl.model.Download;
import javadl.utils.SizeUtil;

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
	}
	public static final class FileManager {
		public static final void download(final String url, final String name, final Boolean silent) {
			final Downloader downloader = new Downloader();
			downloader.setDownloadHandler(new CompleteDownloadHandler(downloader) {
				@Override
				public final void onDownloadStart(final Download download) {
					super.onDownloadStart(download);
					if(!silent) ConsoleModule.info(LanguageModule.I.g(LanguageModule.I.i.R_S) + " [" + name + "]!");
				}
				@Override
				public final void onDownloadSpeedProgress(final Download download, final int downloadedSize, final int maxSize, final int downloadPercent, final int bytesPerSec) {
					if(!silent) ConsoleModule.info(LanguageModule.I.g(LanguageModule.I.i.R_D) + " [" + name + "]: " + downloadedSize + "/" + maxSize + " MB (" + downloadPercent + "%, " + SizeUtil.toMBFB(bytesPerSec) + " MB/s)");
				}
				@Override
				public final void onDownloadFinish(final Download download) {
					super.onDownloadFinish(download);
					if(!silent) ConsoleModule.info(LanguageModule.I.g(LanguageModule.I.i.R_C) + " [" + name + "]!");
				}
				@Override
				public final void onDownloadError(final Download download, final Exception exception) {
					super.onDownloadError(download, exception);
					if(!silent) {
						ConsoleModule.warning(LanguageModule.I.g(LanguageModule.I.i.R_F) + " [" + name + "]!");
					} else ConsoleModule.debug(LanguageModule.I.g(LanguageModule.I.i.R_F) + " [" + name + "]!");
				}
			});
			downloader.downloadFileToLocation(url, new File(LTItemMail.getInstance().getDataFolder() + File.separator + name).getAbsolutePath());
		}
		public static final File get(final String name) {
			final File file = new File(LTItemMail.getInstance().getDataFolder(), name);
			if(file.exists() && file.isFile()) return file;
			return null;
		}
		public static final void create(final String name) {
			final File file = new File(LTItemMail.getInstance().getDataFolder(), name);
			if(!file.exists()) try {
				file.createNewFile();
			} catch (final IOException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
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
	public static final class Build {
		public static final Integer get() {
			final InputStream internalPluginYaml = LTItemMail.getInstance().getResource("plugin.yml");
			final YamlConfiguration pluginYaml = new YamlConfiguration();
			try {
				pluginYaml.load(new InputStreamReader(internalPluginYaml));
				return pluginYaml.getInt("build");
			} catch (IOException | InvalidConfigurationException e) {
				ConsoleModule.debug("There was an error trying to retrieve build number from plugin.yml");
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return 0;
		}
	}
	public static final class Version {
		public static final String get() {
			final InputStream internalPluginYaml = LTItemMail.getInstance().getResource("plugin.yml");
			final YamlConfiguration pluginYaml = new YamlConfiguration();
			try {
				pluginYaml.load(new InputStreamReader(internalPluginYaml));
				return pluginYaml.getString("version");
			} catch (IOException | InvalidConfigurationException e) {
				ConsoleModule.debug("There was an error trying to retrieve version number from plugin.yml");
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return null;
		}
	}
}