package io.github.leothawne.LTItemMail.module;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import io.github.leothawne.LTItemMail.LTItemMail;

public final class LanguageModule {
	private LanguageModule() {}
	private static File languageFile = new File(LTItemMail.getInstance().getDataFolder(), LTItemMail.getInstance().getConfiguration().getString("language") + ".yml");
	public static final void check() {
		if(!languageFile.exists()) {
			ConsoleModule.warning("Extracting " + LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file...");
			if(LTItemMail.getInstance().getConfiguration().getString("language").equalsIgnoreCase("vietnamese") || LTItemMail.getInstance().getConfiguration().getString("language").equalsIgnoreCase("english") || LTItemMail.getInstance().getConfiguration().getString("language").equalsIgnoreCase("portuguese")) {
				LTItemMail.getInstance().saveResource(LTItemMail.getInstance().getConfiguration().getString("language") + ".yml", false);
				ConsoleModule.info("Done.");
			} else ConsoleModule.warning(LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file is not supported yet. I suggest you to manually create the language file and do manually the translation.");
		} else ConsoleModule.info("Found " + LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file.");
	}
	public static final FileConfiguration load() {
		if(languageFile.exists()) {
			final FileConfiguration languageConfig = new YamlConfiguration();
			try {
				languageConfig.load(languageFile);
				ConsoleModule.info("Loaded " + LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file.");
				int languageVersion = 0;
				switch(LTItemMail.getInstance().getConfiguration().getString("language").toLowerCase()) {
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
				if(languageVersion != 0) if(languageConfig.getInt("language-version") != languageVersion) {
					ConsoleModule.severe(LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file outdated. New lines will be added to your current language file. Or you can manually delete the current language file and let the plugin extract the new one.");
					languageConfig.set("language-version", languageVersion);
					languageConfig.save(languageFile);
				}
				return languageConfig;
			} catch(final IOException | InvalidConfigurationException exception) {
				exception.printStackTrace();
			}
		}
		ConsoleModule.severe("Missing " + LTItemMail.getInstance().getConfiguration().getString("language") + ".yml file.");
		return null;
	}
	public static final String get(final Type type) {
		String result = null;
		String path = null;
		switch(type) {
			case PLAYER_PERMISSIONERROR:
				result = "You do not have permission to do this.";
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
			case PLAYER_OFFLINEERROR:
				result = "The specified player is not online.";
				path = "player.offlineerror";
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
				result = "You can not send a mailbox to yourself.";
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
		}
		if(path != null && result != null) if(LTItemMail.getInstance().getLanguage().isSet(path)) {
			result = LTItemMail.getInstance().getLanguage().getString(path);
		} else {
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
		PLAYER_OFFLINEERROR,
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
		TRANSACTION_NOMONEY
	}
}