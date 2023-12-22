package io.github.leothawne.LTItemMail.module;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.type.VersionType;

public final class LanguageModule {
	public static final void check() {
		final File languageFile = new File(LTItemMail.getInstance().getDataFolder(), LTItemMail.getInstance().getConfiguration().getString("language") + ".yml");
		if(!languageFile.exists()) {
			LTItemMail.getInstance().getConsole().warning("Extracting " + LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file...");
			if(LTItemMail.getInstance().getConfiguration().getString("language").equalsIgnoreCase("vietnamese") || LTItemMail.getInstance().getConfiguration().getString("language").equalsIgnoreCase("english") || LTItemMail.getInstance().getConfiguration().getString("language").equalsIgnoreCase("portuguese")) {
				LTItemMail.getInstance().saveResource(LTItemMail.getInstance().getConfiguration().getString("language") + ".yml", false);
				LTItemMail.getInstance().getConsole().info("Done.");
			} else LTItemMail.getInstance().getConsole().warning(LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file is not supported yet. I suggest you to manually create the language file and do manually the translation.");
		} else LTItemMail.getInstance().getConsole().info("Found " + LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file.");
	}
	public static final FileConfiguration load() {
		final File languageFile = new File(LTItemMail.getInstance().getDataFolder(), LTItemMail.getInstance().getConfiguration().getString("language") + ".yml");
		if(languageFile.exists()) {
			final FileConfiguration languageConfig = new YamlConfiguration();
			try {
				languageConfig.load(languageFile);
				LTItemMail.getInstance().getConsole().info("Loaded " + LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file.");
				int languageVersion = 0;
				if(LTItemMail.getInstance().getConfiguration().getString("language").equalsIgnoreCase("english")) languageVersion = Integer.parseInt(DataModule.getVersion(VersionType.ENGLISH_YML));
				if(LTItemMail.getInstance().getConfiguration().getString("language").equalsIgnoreCase("portuguese")) languageVersion = Integer.parseInt(DataModule.getVersion(VersionType.PORTUGUESE_YML));
				if(LTItemMail.getInstance().getConfiguration().getString("language").equalsIgnoreCase("vietnamese")) languageVersion = Integer.parseInt(DataModule.getVersion(VersionType.VIETNAMESE_YML));
				if(languageVersion != 0) if(languageConfig.getInt("language-version") != languageVersion) {
					LTItemMail.getInstance().getConsole().severe(LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file outdated. Please restart the server.");
					languageFile.delete();
					return null;
				}
				return languageConfig;
			} catch(final IOException | InvalidConfigurationException exception) {
				exception.printStackTrace();
			}
			return null;
		}
		LTItemMail.getInstance().getConsole().severe("Missing " + LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file.");
		return null;
	}
	public static final String get(final String path) {
		String result = null;
		switch(path) {
			case "no-permission":
				result = "You do not have permission to do this.";
				break;
			case "mailbox-closed":
				result = "Box closed.";
				break;
			case "mailbox-paid":
				result = "You paid $% to the post office.";
				break;
			case "mailbox-sent":
				result = "Sending box to %...";
				break;
			case "mailbox-from":
				result = "New mailbox from";
				break;
			case "special-mailbox":
				result = "Special Mailbox!!!";
				break;
			case "mailbox-aborted":
				result = "Shipping canceled!";
				break;
			case "transaction-error":
				result = "Transaction not succeeded!";
				break;
			case "transaction-no-money":
				result = "You do not have $% to pay to the post office.";
				break;
			case "recipient-full":
				result = "% does not have % free slot(s).";
				break;
			case "recipient-empty":
				result = "You must specify a player!";
				break;
			case "recipient-offline":
				result = "The specified player is not online.";
				break;
			case "player-error":
				result = "You must be a player to do that!";
				break;
			case "player-tma":
				result = "Syntax error!";
				break;
			case "player-self":
				result = "You can not send a mailbox to yourself.";
				break;
			case "no-new-mailbox":
				result = "No new boxes.";
				break;
			case "mailbox-id-error":
				result = "Mailbox ID must be a number.";
				break;
			case "mailbox-deleted":
				result = "Deleted";
				break;
			case "opened-boxes":
				result = "Opened boxes of";
				break;
			case "empty":
				result = "Empty";
				break;
			case "mailbox-recover-empty":
				result = "There is no lost items in this box.";
				break;
		}
		if(LTItemMail.getInstance().getLanguage().isSet(path)) result = LTItemMail.getInstance().getLanguage().getString(path);
		return result;
	}
}