package br.net.gmj.nobookie.LTItemMail.module;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import br.net.gmj.nobookie.LTItemMail.LTItemMail;
import br.net.gmj.nobookie.LTItemMail.util.FetchUtil;

public class ModelsModule {
	private ModelsModule() {}
	private static File file;
	public static final void check() {
		file = FetchUtil.FileManager.get("item-models.yml");
		if(file == null) {
			ConsoleModule.warning("item-models.yml not found!");
			ConsoleModule.warning("Generating a new one and all default models will be added.");
			FetchUtil.FileManager.create("item-models.yml");
		}
	}
	private static boolean update = false;
	public static final FileConfiguration load() {
		file = FetchUtil.FileManager.get("item-models.yml");
		if(file != null) {
			final FileConfiguration configuration = new YamlConfiguration();
			try {
				configuration.load(file);
				ConsoleModule.info("Item models loaded.");
				try {
					if(configuration.getInt("model-version") < Integer.valueOf(DataModule.getVersion(DataModule.VersionType.ITEM_MODELS_YML))) {
						update = true;
						ConsoleModule.warning("Item models outdated!");
						ConsoleModule.warning("New models will be added.");
						configuration.set("model-version", Integer.valueOf(DataModule.getVersion(DataModule.VersionType.ITEM_MODELS_YML)));
						configuration.save(file);
					}
				} catch(final IllegalArgumentException e) {
					configuration.set("model-version", 0);
					configuration.save(file);
				}
				return configuration;
			} catch (final IOException | InvalidConfigurationException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
		}
		return null;
	}
	public static final Integer get(final Type type) {
		Integer result = null;
		String path = null;
		switch(type) {
			case MAILBOX_LIMITER:
				result = 999999001;
				path = "mailbox.limiter";
				break;
			case MAILBOX_BUTTON_COST:
				result = 999999002;
				path = "mailbox.button.cost";
				break;
			case MAILBOX_BUTTON_LABEL:
				result = 999999006;
				path = "mailbox.button.label";
				break;
			case MAILBOX_BUTTON_DENY:
				result = 999999007;
				path = "mailbox.button.deny";
				break;
			case MAILBOX_BUTTON_ACCEPT:
				result = 999999008;
				path = "mailbox.button.accept";
				break;
			case MAILBOX_GUI_NORMAL:
				result = 999999003;
				path = "mailbox.gui.normal";
				break;
			case MAILBOX_GUI_PENDING:
				result = 999999004;
				path = "mailbox.gui.pending";
				break;
			case MAILBOX_GUI_ADMIN:
				result = 999999005;
				path = "mailbox.gui.admin";
				break;
		}
		if(path != null) if(LTItemMail.getInstance().models.isSet(path)) {
			result = LTItemMail.getInstance().models.getInt(path);
		} else if(result != null) {
			ConsoleModule.info("Item models fallback: [" + path + ":" + result + "]");
			LTItemMail.getInstance().models.set(path, result);
			try {
				LTItemMail.getInstance().models.save(file);
			} catch (final IOException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
		}
		return result;
	}
	public static final void addMissing() {
		if(update) {
			for(final Type type : Type.values()) get(type);
			update = false;
		}
	}
	public enum Type {
		MAILBOX_LIMITER,
		MAILBOX_BUTTON_COST,
		MAILBOX_BUTTON_LABEL,
		MAILBOX_BUTTON_DENY,
		MAILBOX_BUTTON_ACCEPT,
		MAILBOX_GUI_NORMAL,
		MAILBOX_GUI_PENDING,
		MAILBOX_GUI_ADMIN
	}
}