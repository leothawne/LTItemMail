package io.github.leothawne.LTItemMail.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.lib.Fetch;
import io.github.leothawne.LTItemMail.module.ConfigurationModule;
import io.github.leothawne.LTItemMail.module.ConsoleModule;
import io.github.leothawne.LTItemMail.module.DataModule;

public final class VersionControlTask {
	private VersionControlTask() {}
	public static final void run() {
		new BukkitRunnable() {
			@Override
			public final void run() {
				if(Fetch.URL.Cache.download(DataModule.getPluginURL(LTItemMail.getInstance().getDescription().getVersion()), "cfg-" + LTItemMail.getInstance().getDescription().getVersion())) {
					try {
						final YamlConfiguration info = new YamlConfiguration();
						info.load(Fetch.URL.Cache.get("cfg-" + LTItemMail.getInstance().getDescription().getVersion()));
						if(!info.getBoolean("main.enabled")) {
							final List<String> warnings = info.getStringList("main.warnings");
							if(warnings.size() > 0) ConsoleModule.server(warnings);
							Bukkit.getPluginManager().disablePlugin(LTItemMail.getInstance());
						} else {
							final List<Integer> boards = new ArrayList<>();
							int rawID = 0;
							while(true) if(info.get("boards." + rawID) != null) {
								boards.add(rawID);
								rawID++;
							} else break;
							final Map<Integer, Map<String, Map<String, List<String>>>> messages = new HashMap<>();
							for(int id : boards) if(!ConfigurationModule.getBoardsRead().contains(id)) {
								final Map<String, Map<String, List<String>>> contents1 = new HashMap<>();
								final Map<String, List<String>> contents2 = new HashMap<>();
								contents2.put(info.getString("boards." + id + ".datetime"), info.getStringList("boards." + id + ".contents"));
								contents1.put(info.getString("boards." + id + ".title"), contents2);
								messages.put(id, contents1);
								ConfigurationModule.setBoardRead(id);
							}
							if(messages.size() > 0) ConsoleModule.server(messages);
						}
					} catch (final InvalidConfigurationException | IOException e) {
						if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
					}
				} else {
					ConsoleModule.warning("Unable to contact Version Control server. Are you offline?");
					this.cancel();
				}
				Runtime.getRuntime().gc();
			}
		}.runTaskTimerAsynchronously(LTItemMail.getInstance(), 0, 20 * 60);
	}
}