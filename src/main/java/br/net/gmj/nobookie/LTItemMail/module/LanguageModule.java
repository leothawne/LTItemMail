package br.net.gmj.nobookie.LTItemMail.module;

import java.io.File;
import java.io.IOException;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import br.net.gmj.nobookie.LTItemMail.LTItemMail;
import br.net.gmj.nobookie.LTItemMail.module.DataModule.VersionType;
import br.net.gmj.nobookie.LTItemMail.util.BukkitUtil;

public final class LanguageModule {
	private LanguageModule() {}
	private static File languageFile;
	public static final void check() {
		languageFile = new File(LTItemMail.getInstance().getDataFolder(), (String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE) + ".yml");
		if(!languageFile.exists()) {
			ConsoleModule.info("Extracting " + (String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE) + ".yml...");
			if(LTItemMail.getInstance().getResource((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE) + ".yml") != null) {
				LTItemMail.getInstance().saveResource((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE) + ".yml", false);
				ConsoleModule.info("Done.");
			} else try {
				languageFile.createNewFile();
				ConsoleModule.warning("Language " + (String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE) + " not found!");
				ConsoleModule.warning("A new yml file was created and all translations will be added with default value for you to modify/translate.");
			} catch (final IOException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
		}
	}
	public static final FileConfiguration load() {
		if(languageFile.exists()) {
			final FileConfiguration language = new YamlConfiguration();
			try {
				language.load(languageFile);
				ConsoleModule.info((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE) + ".yml loaded.");
				if(((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE)).equalsIgnoreCase("portuguese")) ConsoleModule.br();
				try {
					final VersionType type = DataModule.VersionType.valueOf(((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE)).toUpperCase() + "_YML");
					final int languageVersion = Integer.parseInt(DataModule.getVersion(type));
					if(language.getInt("language-version") < languageVersion) {
						ConsoleModule.warning("Language " + (String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE) + ".yml outdated!");
						ConsoleModule.warning("Missing translations will be added with default value.");
						language.set("language-version", languageVersion);
						language.save(languageFile);
					}
				} catch(final IllegalArgumentException e) {
					language.set("language-version", 0);
					language.save(languageFile);
				}
				return language;
			} catch(final IOException | InvalidConfigurationException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
		}
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
				result = "Mail closed.";
				path = "mailbox.closed";
				break;
			case TRANSACTION_PAID:
				result = "You paid $% to the post office.";
				path = "transaction.paid";
				break;
			case MAILBOX_SENT:
				result = "Sending mail to %...";
				path = "mailbox.sent";
				break;
			case MAILBOX_FROM:
				result = "New mail from";
				path = "mailbox.from";
				break;
			case MAILBOX_SPECIAL:
				result = "Special Mail!!!";
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
			case PLAYER_BANNED:
				result = "You are banned! Ban reason available in /itemmail info";
				path = "player.banned";
				break;
			case MAILBOX_NONEW:
				result = "No new mails.";
				path = "mailbox.nonew";
				break;
			case MAILBOX_IDERROR:
				result = "Mail ID must be a number.";
				path = "mailbox.iderror";
				break;
			case MAILBOX_DELETED:
				result = "Deleted";
				path = "mailbox.deleted";
				break;
			case PLAYER_OPENEDBOXES:
				result = "Deleted mails of";
				path = "player.openedboxes";
				break;
			case MAILBOX_EMPTY:
				result = "Empty";
				path = "mailbox.empty";
				break;
			case MAILBOX_NOLOST:
				result = "There is no lost items in this mail.";
				path = "mailbox.nolost";
				break;
			case MAILBOX_EMPTYLIST:
				result = "No opened mails for player";
				path = "mailbox.emptylist";
				break;
			case MAILBOX_BLOCKED:
				result = "Something blocked this mail. Delivery cancelled.";
				path = "mailbox.blocked";
				break;
			case MAILBOX_LABEL:
				result = "&6Label:";
				path = "mailbox.label";
				break;
			case MAILBOX_COST:
				result = "&aCost:";
				path = "mailbox.cost";
				break;
			case MAILBOX_COSTERROR:
				result = "Economy plugin not found.";
				path = "mailbox.costerror";
				break;
			case MAILBOX_COSTUPDATE:
				result = "Click to update the current mail price.";
				path = "mailbox.costupdate";
				break;
			case MAILBOX_RETURNED:
				result = "sent it back to you.";
				path = "mailbox.returned";
				break;
			case MAILBOX_ACCEPT:
				result = "Accept";
				path = "mailbox.accept";
				break;
			case MAILBOX_DENY:
				result = "Deny";
				path = "mailbox.deny";
				break;
			case COMMAND_PLAYER_ITEMMAIL:
				result = "Lists player commands.";
				path = "command.player.itemmail";
				break;
			case COMMAND_PLAYER_VERSION:
				result = "Shows the current plugin version.";
				path = "command.player.version";
				break;
			case COMMAND_PLAYER_LIST:
				result = "Lists all pending mailboxes received.";
				path = "command.player.list";
				break;
			case COMMAND_PLAYER_OPEN:
				result = "Opens a mail.";
				path = "command.player.open";
				break;
			case COMMAND_PLAYER_DELETE:
				result = "Deletes a mail.";
				path = "command.player.delete";
				break;
			case COMMAND_PLAYER_INFO_MAIN:
				result = "Shows relevant informations about the player.";
				path = "command.player.info.info";
				break;
			case COMMAND_PLAYER_INFO_REGISTRY:
				result = "Registry date:";
				path = "command.player.info.registry";
				break;
			case COMMAND_PLAYER_INFO_BANNED_MAIN:
				result = "Banned:";
				path = "command.player.info.banned.banned";
				break;
			case COMMAND_PLAYER_INFO_BANNED_NO:
				result = "No";
				path = "command.player.info.banned.nop";
				break;
			case COMMAND_PLAYER_INFO_BANNED_YES:
				result = "Yes";
				path = "command.player.info.banned.yep";
				break;
			case COMMAND_PLAYER_INFO_BANNED_REASON:
				result = "Ban reason:";
				path = "command.player.info.banned.reason";
				break;
			case COMMAND_PLAYER_INFO_SENT:
				result = "Mails sent:";
				path = "command.player.info.sent";
				break;
			case COMMAND_PLAYER_INFO_RECEIVED:
				result = "Mails received:";
				path = "command.player.info.received";
				break;
			case COMMAND_PLAYER_MAILITEM:
				result = "Opens a new mail to put items inside and send it to another player.";
				path = "command.player.mailitem";
				break;
			case COMMAND_ADMIN_ITEMMAILADMIN:
				result = "Lists admin commands.";
				path = "command.admin.itemmailadmin";
				break;
			case COMMAND_ADMIN_UPDATE_MAIN:
				result = "Checks for new updates.";
				path = "command.admin.update.update";
				break;
			case COMMAND_ADMIN_UPDATE_FOUND:
				result = "New update available. You are % build(s) out of date. Download it now:";
				path = "command.admin.update.found";
				break;
			case COMMAND_ADMIN_UPDATE_NONEW:
				result = "There is no new updates!";
				path = "command.admin.update.nonew";
				break;
			case COMMAND_ADMIN_LIST:
				result = "Lists deleted mails of a player.";
				path = "command.admin.list";
				break;
			case COMMAND_ADMIN_RECOVER:
				result = "Recovers lost items from a deleted mail (if there is any).";
				path = "command.admin.recover";
				break;
			case COMMAND_ADMIN_BAN_MAIN:
				result = "Bans a specific player.";
				path = "command.admin.ban.ban";
				break;
			case COMMAND_ADMIN_BAN_BANNED:
				result = "banned!";
				path = "command.admin.ban.banned";
				break;
			case COMMAND_ADMIN_BAN_ALREADY:
				result = "is banned already!";
				path = "command.admin.ban.already";
				break;
			case COMMAND_ADMIN_UNBAN_MAIN:
				result = "Unbans a specific player.";
				path = "command.admin.unban.unban";
				break;
			case COMMAND_ADMIN_UNBAN_UNBANNED:
				result = "unbanned!";
				path = "command.admin.unban.unbanned";
				break;
			case COMMAND_ADMIN_UNBAN_ALREADY:
				result = "is unbanned already!";
				path = "command.admin.unban.already";
				break;
			case COMMAND_ADMIN_BANLIST_MAIN:
				result = "Gets the banned list.";
				path = "command.admin.banlist.banlist";
				break;
			case COMMAND_ADMIN_BANLIST_LIST:
				result = "Players banned:";
				path = "command.admin.banlist.list";
				break;
			case COMMAND_ADMIN_BANLIST_EMPTY:
				result = "No players banned.";
				path = "command.admin.banlist.empty";
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
			case BLOCK_NAME:
				result = "Mailbox";
				path = "block.name";
				break;
			case BLOCK_OWNER:
				result = "Owner:";
				path = "block.owner";
				break;
			case BLOCK_LIST:
				result = "Mailboxes you placed:";
				path = "block.list.placed";
				break;
			case BLOCK_LIST_WORLD:
				result = "World";
				path = "block.list.world";
				break;
			case BLOCK_ADMIN_LIST:
				result = "Mailboxes of";
				path = "block.adminlist.placed";
				break;
			case TRANSACTION_NOTINSTALLED:
				result = "Vault is not installed!";
				path = "transaction.notinstalled";
				break;
			case TRANSACTION_COSTS:
				result = "Price:";
				path = "transaction.costs";
				break;
			case COMMAND_ADMIN_RELOAD:
				result = "Reloads plugin config and language settings.";
				path = "command.admin.reload";
				break;
			case COMMAND_PLAYER_COSTS:
				result = "Shows mail price.";
				path = "command.player.costs";
				break;
			case COMMAND_PLAYER_BLOCKS:
				result = "Shows your placed mailboxes list.";
				path = "command.player.blocks";
				break;
			case COMMAND_ADMIN_BLOCKS:
				result = "Shows the list of placed mailboxes of a player.";
				path = "command.admin.blocks";
				break;
			case COMMAND_PLAYER_COLOR:
				result = "Changes the color of the mailbox block in your main hand.";
				path = "command.player.color";
				break;
		}
		if(path != null) if(LTItemMail.getInstance().language.isSet(path)) {
			result = LTItemMail.getInstance().language.getString(path);
			if(type.equals(Type.MAILBOX_COST) || type.equals(Type.MAILBOX_LABEL)) result = BukkitUtil.format((String) result);
		} else if(result != null) {
			ConsoleModule.info("Language fallback: [" + path + ":" + result + "]");
			LTItemMail.getInstance().language.set(path, result);
			try {
				LTItemMail.getInstance().language.save(languageFile);
			} catch (final IOException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
		}
		return result;
	}
	public static final void addMissing() {
		for(final Type type : Type.values()) get(type);
	}
	public enum Type {
		COMMAND_INVALID,
		COMMAND_PLAYER_ITEMMAIL,
		COMMAND_PLAYER_VERSION,
		COMMAND_PLAYER_LIST,
		COMMAND_PLAYER_OPEN,
		COMMAND_PLAYER_DELETE,
		COMMAND_PLAYER_MAILITEM,
		COMMAND_PLAYER_INFO_MAIN,
		COMMAND_PLAYER_INFO_REGISTRY,
		COMMAND_PLAYER_INFO_BANNED_MAIN,
		COMMAND_PLAYER_INFO_BANNED_NO,
		COMMAND_PLAYER_INFO_BANNED_YES,
		COMMAND_PLAYER_INFO_BANNED_REASON,
		COMMAND_PLAYER_INFO_SENT,
		COMMAND_PLAYER_INFO_RECEIVED,
		COMMAND_PLAYER_BLOCKS,
		COMMAND_PLAYER_COLOR,
		COMMAND_ADMIN_ITEMMAILADMIN,
		COMMAND_ADMIN_UPDATE_MAIN,
		COMMAND_ADMIN_UPDATE_FOUND,
		COMMAND_ADMIN_UPDATE_NONEW,
		COMMAND_ADMIN_LIST,
		COMMAND_ADMIN_RECOVER,
		COMMAND_ADMIN_BAN_MAIN,
		COMMAND_ADMIN_BAN_BANNED,
		COMMAND_ADMIN_BAN_ALREADY,
		COMMAND_ADMIN_UNBAN_MAIN,
		COMMAND_ADMIN_UNBAN_UNBANNED,
		COMMAND_ADMIN_UNBAN_ALREADY,
		COMMAND_ADMIN_BANLIST_MAIN,
		COMMAND_ADMIN_BANLIST_LIST,
		COMMAND_ADMIN_BANLIST_EMPTY,
		COMMAND_ADMIN_BLOCKS,
		PLAYER_PERMISSIONERROR,
		PLAYER_INVENTORYFULL,
		PLAYER_MISSINGERROR,
		PLAYER_NEVERPLAYEDERROR,
		PLAYER_ERROR,
		PLAYER_SYNTAXERROR,
		PLAYER_SELFERROR,
		PLAYER_OPENEDBOXES,
		PLAYER_BANNED,
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
		MAILBOX_BLOCKED,
		MAILBOX_LABEL,
		MAILBOX_COST,
		MAILBOX_COSTERROR,
		MAILBOX_COSTUPDATE,
		MAILBOX_RETURNED,
		MAILBOX_ACCEPT,
		MAILBOX_DENY,
		TRANSACTION_PAID,
		TRANSACTION_ERROR,
		TRANSACTION_NOMONEY,
		BLOCK_PLACEERROR,
		BLOCK_BREAKERROR,
		BLOCK_USEERROR,
		BLOCK_OWNERERROR,
		BLOCK_BELOWERROR,
		BLOCK_ADMINBROKE,
		BLOCK_NAME,
		BLOCK_OWNER,
		BLOCK_LIST,
		BLOCK_LIST_WORLD,
		BLOCK_ADMIN_LIST,
		TRANSACTION_NOTINSTALLED,
		TRANSACTION_COSTS,
		COMMAND_ADMIN_RELOAD,
		COMMAND_PLAYER_COSTS
	}
	public static final class I {
		public static final String g(final i i) {
			if(((String) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_TYPE_LANGUAGE)).equals("portuguese")) switch(i) {
				case R_S:
					return "Download de recurso iniciado";
				case R_D:
					return "Baixando recurso";
				case R_C:
					return "Download de recurso finalizado";
				case R_F:
					return "Download de recurso falhou";
				case RP_I:
					return "O pacote de recursos deve estar localizado dentro da pasta do plugin";
				case RP_F:
					return "Não foi possível mover o arquivo do pacote de recursos da pasta cache";
			}
			switch(i) {
				case R_S:
					return "Resource download started";
				case R_D:
					return "Downloading resource";
				case R_C:
					return "Resource download completed";
				case R_F:
					return "Resource download failed";
				case RP_I:
					return "The resource pack should be available inside the plugin folder";
				case RP_F:
					return "Could not move the resource pack file from the cache folder";
			}
			return null;
		}
		public enum i {
			R_S,
			R_D,
			R_C,
			R_F,
			RP_I,
			RP_F
		}
	}
}