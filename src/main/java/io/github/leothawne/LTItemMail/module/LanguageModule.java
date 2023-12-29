package io.github.leothawne.LTItemMail.module;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.leothawne.LTItemMail.LTItemMail;

public final class LanguageModule {
	private LanguageModule() {}
	private static File languageFile;
	public static final void check() {
		languageFile = new File(LTItemMail.getInstance().getDataFolder(), (String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE) + ".yml");
		if(!languageFile.exists()) {
			ConsoleModule.warning("Extracting " + (String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE) + ".yml file...");
			if(((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE)).equalsIgnoreCase("vietnamese") || ((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE)).equalsIgnoreCase("english") || ((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE)).equalsIgnoreCase("portuguese")) {
				LTItemMail.getInstance().saveResource((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE) + ".yml", false);
				ConsoleModule.info("Done.");
			} else try {
				languageFile.createNewFile();
				ConsoleModule.warning((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE) + " is not integrated with internal language support. The yml file was created and new lines will be added while you play.");
			} catch (final IOException e) {
				e.printStackTrace();
			}
		} else ConsoleModule.info("Found " + (String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE) + ".yml file.");
	}
	public static final FileConfiguration load() {
		if(languageFile.exists()) {
			final FileConfiguration language = new YamlConfiguration();
			try {
				language.load(languageFile);
				ConsoleModule.info("Loaded " + (String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE) + ".yml file.");
				int languageVersion = 0;
				switch(((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE)).toLowerCase()) {
					case "english":
						languageVersion = Integer.parseInt(DataModule.getVersion(DataModule.VersionType.ENGLISH_YML));
						break;
					case "portuguese":
						languageVersion = Integer.parseInt(DataModule.getVersion(DataModule.VersionType.PORTUGUESE_YML));
						break;
					case "vietnamese":
						languageVersion = Integer.parseInt(DataModule.getVersion(DataModule.VersionType.VIETNAMESE_YML));
						break;
				}
				if(language.getInt("language-version") != languageVersion) ConsoleModule.severe((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE) + ".yml file outdated. New lines will be added to your current language file. Or you can manually delete the current language file and let the plugin extract the new one.");
				language.set("language-version", languageVersion);
				language.save(languageFile);
				return language;
			} catch(final IOException | InvalidConfigurationException e) {
				e.printStackTrace();
			}
		}
		ConsoleModule.severe("Missing " + (String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE) + ".yml file.");
		return null;
	}
	public static final String get(final Type type) {
		String result = null;
		String path = null;
		switch(type) {
			case PLAYER_PERMISSIONERROR:
				result = "You do not have permission to do that.";
				path = "player.permissionerror";
				break;
			case MAILBOX_CLOSED:
				result = "Box closed.";
				path = "mailbox.closed";
				break;
			case TRANSACTION_PAID:
				result = "You paid $% to the post office.";
				path = "transaction.paid";
				break;
			case MAILBOX_SENT:
				result = "Sending box to %...";
				path = "mailbox.sent";
				break;
			case MAILBOX_FROM:
				result = "New mailbox from";
				path = "mailbox.from";
				break;
			case MAILBOX_SPECIAL:
				result = "Special Mailbox!!!";
				path = "mailbox.special";
				break;
			case MAILBOX_ABORTED:
				result = "Shipping canceled!";
				path = "mailbox.aborted";
				break;
			case TRANSACTION_ERROR:
				result = "Transaction not succeeded!";
				path = "transaction.error";
				break;
			case TRANSACTION_NOMONEY:
				result = "You do not have $% to pay to the post office.";
				path = "transaction.nomoney";
				break;
			case PLAYER_INVENTORYFULL:
				result = "% does not have % free slot(s).";
				path = "player.inventoryfull";
				break;
			case PLAYER_MISSINGERROR:
				result = "You must specify a player!";
				path = "player.missingerror";
				break;
			case PLAYER_NEVERPLAYEDERROR:
				result = "The specified player never joined this server.";
				path = "player.neverplayederror";
				break;
			case PLAYER_ERROR:
				result = "You must be a player to do that!";
				path = "player.error";
				break;
			case PLAYER_SYNTAXERROR:
				result = "Syntax error!";
				path = "player.syntaxerror";
				break;
			case PLAYER_SELFERROR:
				result = "You can not send items to yourself.";
				path = "player.selferror";
				break;
			case MAILBOX_NONEW:
				result = "No new boxes.";
				path = "mailbox.nonew";
				break;
			case MAILBOX_IDERROR:
				result = "Mailbox ID must be a number.";
				path = "mailbox.iderror";
				break;
			case MAILBOX_DELETED:
				result = "Deleted";
				path = "mailbox.deleted";
				break;
			case PLAYER_OPENEDBOXES:
				result = "Opened boxes of";
				path = "player.openedboxes";
				break;
			case MAILBOX_EMPTY:
				result = "Empty";
				path = "mailbox.empty";
				break;
			case MAILBOX_NOLOST:
				result = "There is no lost items in this box.";
				path = "mailbox.nolost";
				break;
			case MAILBOX_EMPTYLIST:
				result = "No opened mailboxes for player";
				path = "mailbox.emptylist";
				break;
			case COMMAND_PLAYER_ITEMMAIL:
				result = "List of player commands.";
				path = "command.player.itemmail";
				break;
			case COMMAND_PLAYER_VERSION:
				result = "Show the current plugin version.";
				path = "command.player.version";
				break;
			case COMMAND_PLAYER_LIST:
				result = "List all pending mailboxes received.";
				path = "command.player.list";
				break;
			case COMMAND_PLAYER_OPEN:
				result = "Open a pending mailbox.";
				path = "command.player.open";
				break;
			case COMMAND_PLAYER_DELETE:
				result = "Delete a pending mailbox.";
				path = "command.player.delete";
				break;
			case COMMAND_PLAYER_MAILITEM:
				result = "Open a new mailbox to put items inside and send it to another player.";
				path = "command.player.mailitem";
				break;
			case COMMAND_ADMIN_ITEMMAILADMIN:
				result = "List of admin commands.";
				path = "command.admin.itemmailadmin";
				break;
			case COMMAND_ADMIN_UPDATE:
				result = "Check for new updates.";
				path = "command.admin.update";
				break;
			case COMMAND_ADMIN_LIST:
				result = "List deleted mailboxes of a player.";
				path = "command.admin.list";
				break;
			case COMMAND_ADMIN_RECOVER:
				result = "Recover lost items from a deleted mailbox (if there is any).";
				path = "command.admin.recover";
				break;
			case COMMAND_INVALID:
				result = "Invalid command. Type % to see the command list.";
				path = "command.invalid";
				break;
			case BLOCK_BREAKERROR:
				result = "You do not have permission to break that mailbox.";
				path = "block.breakerror";
				break;
			case BLOCK_PLACEERROR:
				result = "You do not have permission to place a mailbox.";
				path = "block.placeerror";
				break;
			case BLOCK_USEERROR:
				result = "You do not have permission to use a mailbox.";
				path = "block.useerror";
				break;
			case BLOCK_OWNERERROR:
				result = "You are not the mailbox owner to do that.";
				path = "block.ownererror";
				break;
			case BLOCK_BELOWERROR:
				result = "Below block must be a fence.";
				path = "block.belowerror";
				break;
			case BLOCK_ADMINBROKE:
				result = "You broke the mailbox of";
				path = "block.adminbroke";
				break;
		}
		if(path != null && result != null) if(LTItemMail.getInstance().getLanguage().isSet(path)) {
			result = LTItemMail.getInstance().getLanguage().getString(path);
		} else {
			ConsoleModule.warning("Language fallback: [" + path + ":" + result + "]");
			LTItemMail.getInstance().getLanguage().set(path, result);
			try {
				LTItemMail.getInstance().getLanguage().save(languageFile);
			} catch (final IOException e) {
				e.printStackTrace();
			}
		}
		return result;
	}
	public enum Type {
		COMMAND_INVALID,
		COMMAND_PLAYER_ITEMMAIL,
		COMMAND_PLAYER_VERSION,
		COMMAND_PLAYER_LIST,
		COMMAND_PLAYER_OPEN,
		COMMAND_PLAYER_DELETE,
		COMMAND_PLAYER_MAILITEM,
		COMMAND_ADMIN_ITEMMAILADMIN,
		COMMAND_ADMIN_UPDATE,
		COMMAND_ADMIN_LIST,
		COMMAND_ADMIN_RECOVER,
		PLAYER_PERMISSIONERROR,
		PLAYER_INVENTORYFULL,
		PLAYER_MISSINGERROR,
		PLAYER_NEVERPLAYEDERROR,
		PLAYER_ERROR,
		PLAYER_SYNTAXERROR,
		PLAYER_SELFERROR,
		PLAYER_OPENEDBOXES,
		MAILBOX_CLOSED,
		MAILBOX_SENT,
		MAILBOX_FROM,
		MAILBOX_SPECIAL,
		MAILBOX_ABORTED,
		MAILBOX_IDERROR,
		MAILBOX_DELETED,
		MAILBOX_EMPTY,
		MAILBOX_NOLOST,
		MAILBOX_EMPTYLIST,
		MAILBOX_NONEW,
		TRANSACTION_PAID,
		TRANSACTION_ERROR,
		TRANSACTION_NOMONEY,
		BLOCK_PLACEERROR,
		BLOCK_BREAKERROR,
		BLOCK_USEERROR,
		BLOCK_OWNERERROR,
		BLOCK_BELOWERROR,
		BLOCK_ADMINBROKE
	}
}