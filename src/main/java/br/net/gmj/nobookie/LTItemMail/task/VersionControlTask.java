package br.net.gmj.nobookie.LTItemMail.task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import br.net.gmj.nobookie.LTItemMail.LTItemMail;
import br.net.gmj.nobookie.LTItemMail.module.ConfigurationModule;
import br.net.gmj.nobookie.LTItemMail.module.ConsoleModule;
import br.net.gmj.nobookie.LTItemMail.module.DataModule;
import br.net.gmj.nobookie.LTItemMail.util.FetchUtil;

public final class VersionControlTask {
	private VersionControlTask() {}
	public static final void run() {
		Bukkit.getScheduler().runTaskLater(LTItemMail.getInstance(), new Runnable() {
			@Override
			public final void run() {
				if(FetchUtil.URL.Cache.download(DataModule.getPluginPath(LTItemMail.getInstance().getDescription().getVersion()), LTItemMail.getInstance().getDescription().getVersion() + ".yml", true)) {
					try {
						final YamlConfiguration info = new YamlConfiguration();
						info.load(FetchUtil.URL.Cache.get(LTItemMail.getInstance().getDescription().getVersion() + ".yml"));
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
								LTItemMail.getInstance().boardsForPlayers.add(id);
								final Map<String, Map<String, List<String>>> contents1 = new HashMap<>();
								final Map<String, List<String>> contents2 = new HashMap<>();
								contents2.put(info.getString("boards." + id + ".datetime"), info.getStringList("boards." + id + ".contents"));
								contents1.put(info.getString("boards." + id + ".title"), contents2);
								messages.put(id, contents1);
								ConfigurationModule.setBoardRead(id);
							}
							if(messages.size() > 0) ConsoleModule.board(Bukkit.getConsoleSender(), messages);
						}
					} catch (final InvalidConfigurationException | IOException e) {
						if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
					}
				} else ConsoleModule.warning("Unable to contact Version Control server. Are you offline?");
			}
		}, 20);
	}
}