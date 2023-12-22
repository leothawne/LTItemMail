package io.github.leothawne.LTItemMail.module;

import java.io.File;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.UUID;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.LTItemMail;

public final class DatabaseModule {
	private DatabaseModule() {}
	public static final void check() {
		final File databaseFile = new File(LTItemMail.getInstance().getDataFolder(), "mailboxes.db");
		if(!databaseFile.exists()) {
			LTItemMail.getInstance().getConsole().warning("Extracting mailboxes.db file...");
			LTItemMail.getInstance().saveResource("mailboxes.db", false);
			LTItemMail.getInstance().getConsole().info("Done.");
		} else LTItemMail.getInstance().getConsole().info("Found mailboxes.db file.");
	}
	public static final Connection load() {
		final File databaseFile = new File(LTItemMail.getInstance().getDataFolder(), "mailboxes.db");
		if(databaseFile.exists()) {
			try {
				final Connection con = DriverManager.getConnection("jdbc:sqlite:" + LTItemMail.getInstance().getDataFolder() + File.separator + "mailboxes.db");
				LTItemMail.getInstance().getConsole().info("Database loaded.");
				return con;
			} catch (final SQLException e) {
				e.printStackTrace();
			}
			LTItemMail.getInstance().getConsole().severe("An error occurred while loading the database.");
			return null;
		}
		LTItemMail.getInstance().getConsole().severe("Missing mailboxes.db file.");
		return null;
	}
	public static final int checkDbVer() {
		try {
			DatabaseMetaData meta = LTItemMail.getInstance().getConnection().getMetaData();
			ResultSet results = meta.getTables(null, null, "config", new String[] {"TABLE"});
			if(results.next()) {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				results = statement.executeQuery("SELECT version FROM config;");
				statement.closeOnCompletion();
				if(results.next()) return results.getInt("version");
			}
		} catch (final SQLException e) {
			e.printStackTrace();
			LTItemMail.getInstance().getConsole().severe("An error occurred while checking database version.");
		}
		return 0;
	}
	public static final boolean updateDb(Integer dbVer) {
		LinkedList<String> sqlList = new LinkedList<>();
		switch(dbVer) {
			case 0:
				sqlList.add(0, "CREATE TABLE config(version INTEGER NOT NULL);");
				sqlList.add(1, "INSERT INTO config(version) VALUES('1');");
				sqlList.add(2, "ALTER TABLE mailbox DROP COLUMN items_lost;");
				break;
		}
		if(sqlList.size() > 0) try {
			for(Integer i = 0; i < sqlList.size(); i++) {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				statement.execute(sqlList.get(i));
				statement.closeOnCompletion();
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static final class Function {
		public static final int saveMailbox(final UUID playerFrom, UUID playerTo, final LinkedList<ItemStack> items) {
			final String time = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now());
			try {
				final PreparedStatement insert = LTItemMail.getInstance().getConnection().prepareStatement("INSERT INTO mailbox(uuid_from, uuid_to, sent_date, items) VALUES(?, ?, ?, ?);");
				if(playerFrom != null) {
					insert.setString(1, playerFrom.toString());
				} else insert.setString(1, "");
				insert.setString(2, playerTo.toString());
				insert.setString(3, time);
				final YamlConfiguration itemString = new YamlConfiguration();
				for(Integer i = 0; i < items.size(); i++) itemString.set("i_" + String.valueOf(i), items.get(i));
				insert.setString(4, itemString.saveToString());
				insert.executeUpdate();
				insert.closeOnCompletion();
				final Statement get = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = get.executeQuery("SELECT id FROM mailbox WHERE uuid_to = '" + playerTo.toString() + "' AND sent_date = '" + time + "' ORDER BY id DESC LIMIT 1;");
				get.closeOnCompletion();
				if(results.next()) return results.getInt("id");
			} catch (final SQLException e) {
				e.printStackTrace();
				LTItemMail.getInstance().getConsole().severe("An error occurred while saving a mailbox.");
			}
			return 0;
		}
		public static final boolean updateMailbox(final Integer mailboxID, final LinkedList<ItemStack> items) {
			try {
				final PreparedStatement statement = LTItemMail.getInstance().getConnection().prepareStatement("UPDATE mailbox SET items = ?, opened = ? WHERE id = ?;");
				if(items.size() > 0) {
					final YamlConfiguration itemString = new YamlConfiguration();
					for(Integer i = 0; i < items.size(); i++) itemString.set("i_" + String.valueOf(i), items.get(i));
					statement.setString(1, itemString.saveToString());
					statement.setInt(2, 0);
				} else {
					statement.setString(1, "");
					statement.setInt(2, 1);
				}
				statement.setInt(3, mailboxID);
				statement.executeUpdate();
				statement.closeOnCompletion();
				return true;
			} catch (final SQLException e) {
				e.printStackTrace();
				LTItemMail.getInstance().getConsole().severe("An error occurred while updating a mailbox status.");
			}
			return false;
		}
		public static final LinkedList<ItemStack> getMailbox(final Integer mailboxID) {
			final LinkedList<ItemStack> items = new LinkedList<>();
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = statement.executeQuery("SELECT items FROM mailbox WHERE id = '" + mailboxID + "';");
				final YamlConfiguration itemString = new YamlConfiguration();
				Boolean empty = false;
				if(results.next()) if(!results.getString("items").equals("")) {
					itemString.loadFromString(results.getString("items"));
				} else empty = true;
				statement.closeOnCompletion();
				if(!empty) for(Integer i = 0; i < 27; i++) items.add(i, itemString.getItemStack("i_" + String.valueOf(i)));
				return items;
			} catch (final SQLException | InvalidConfigurationException e) {
				e.printStackTrace();
				LTItemMail.getInstance().getConsole().severe("An error occurred while getting a mailbox items.");
			}
			return items;
		}
		public static final boolean setMailboxOpened(final Integer mailboxID) {
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				statement.executeUpdate("UPDATE mailbox SET opened = '1' WHERE id = '" + mailboxID + "';");
				statement.closeOnCompletion();
				return true;
			} catch (final SQLException e) {
				e.printStackTrace();
				LTItemMail.getInstance().getConsole().severe("An error occurred while updating a mailbox status.");
			}
			return false;
		}
		public static final boolean isMaiboxOwner(final UUID owner, final Integer mailboxID) {
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = statement.executeQuery("SELECT uuid_to FROM mailbox WHERE id = '" + mailboxID + "';");
				statement.closeOnCompletion();
				if(results.next()) if(results.getString("uuid_to").equals(owner.toString())) return true;
			} catch (final SQLException e) {
				e.printStackTrace();
				LTItemMail.getInstance().getConsole().severe("An error occurred while checking for a mailbox ownership.");
			}
			return false;
		}
		public static final UUID getMailboxOwner(final Integer mailboxID) {
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = statement.executeQuery("SELECT uuid_to FROM mailbox WHERE id = '" + mailboxID + "';");
				statement.closeOnCompletion();
				if(results.next()) return UUID.fromString(results.getString("uuid_to"));
			} catch (final SQLException e) {
				e.printStackTrace();
				LTItemMail.getInstance().getConsole().severe("An error occurred while checking for a mailbox ownership.");
			}
			return null;
		}
		public static final boolean isMailboxOpened(final Integer mailboxID) {
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = statement.executeQuery("SELECT opened FROM mailbox WHERE id = '" + mailboxID + "';");
				if(results.next()) if(results.getInt("opened") == 1) return true;
				statement.closeOnCompletion();
			} catch (final SQLException e) {
				e.printStackTrace();
				LTItemMail.getInstance().getConsole().severe("An error occurred while checking for a mailbox status.");
			}
			return false;
		}
		public static final HashMap<Integer, String> getMailboxesList(final UUID owner){
			final HashMap<Integer, String> mailboxes = new HashMap<>();
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = statement.executeQuery("SELECT id,sent_date FROM mailbox WHERE uuid_to = '" + owner.toString() + "' AND opened = '0';");
				while(results.next()) mailboxes.putIfAbsent(results.getInt("id"), results.getString("sent_date"));
				statement.closeOnCompletion();
			} catch (final SQLException e) {
				e.printStackTrace();
				LTItemMail.getInstance().getConsole().severe("An error occurred getting a mailboxes list.");
			}
			return mailboxes;
		}
		public static final HashMap<Integer, String> getOpenedMailboxesList(final UUID owner){
			final HashMap<Integer, String> mailboxes = new HashMap<>();
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = statement.executeQuery("SELECT id,sent_date FROM mailbox WHERE uuid_to = '" + owner.toString() + "' AND opened = '1';");
				while(results.next()) mailboxes.putIfAbsent(results.getInt("id"), results.getString("sent_date"));
				statement.closeOnCompletion();
			} catch (final SQLException e) {
				e.printStackTrace();
				LTItemMail.getInstance().getConsole().severe("An error occurred getting a mailboxes list.");
			}
			return mailboxes;
		}
	}
}