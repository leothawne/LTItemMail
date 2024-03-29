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

import org.bukkit.Location;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;

import io.github.leothawne.LTItemMail.LTItemMail;

public final class DatabaseModule {
	private DatabaseModule() {}
	private static final File databaseFile = new File(LTItemMail.getInstance().getDataFolder(), "mailboxes.db");
	public static final void check() {
		if(!databaseFile.exists()) {
			ConsoleModule.warning("Extracting mailboxes.db...");
			LTItemMail.getInstance().saveResource("mailboxes.db", false);
			ConsoleModule.info("Done.");
		}
	}
	public static final Connection load() {
		if(databaseFile.exists()) {
			try {
				final Connection con = DriverManager.getConnection("jdbc:sqlite:" + LTItemMail.getInstance().getDataFolder() + File.separator + "mailboxes.db");
				ConsoleModule.info("Loaded database.");
				return con;
			} catch (final SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
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
		}
		return 0;
	}
	public static final boolean updateDb(Integer dbVer) {
		LinkedList<String> sqlList = new LinkedList<>();
		switch(dbVer) {
			case 0:
				sqlList.add("CREATE TABLE config(version INTEGER NOT NULL);");
				sqlList.add("INSERT INTO config(version) VALUES('1');");
				sqlList.add("ALTER TABLE mailbox DROP COLUMN items_lost;");
				break;
			case 1:
				sqlList.add("CREATE TABLE mailbox_block ("
						+ "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
						+ "owner_uuid TEXT (36) NOT NULL,"
						+ "mailbox_x  INTEGER (8) NOT NULL,"
						+ "mailbox_y  INTEGER (8) NOT NULL,"
						+ "mailbox_z  INTEGER (8) NOT NULL"
						+ ");");
				sqlList.add("UPDATE config SET version = '2';");
				break;
			case 2:
				sqlList.add("ALTER TABLE mailbox ADD label TEXT;");
				sqlList.add("UPDATE config SET version = '3';");
				break;
		}
		if(sqlList.size() > 0) try {
			for(final String sql : sqlList) {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				statement.execute(sql);
				statement.closeOnCompletion();
			}
			return true;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return false;
	}
	public static final class Virtual {
		public static final int saveMailbox(final UUID playerFrom, UUID playerTo, final LinkedList<ItemStack> items, final String label) {
			final String time = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now());
			try {
				final PreparedStatement insert = LTItemMail.getInstance().getConnection().prepareStatement("INSERT INTO mailbox(uuid_from, uuid_to, sent_date, items, label) VALUES(?, ?, ?, ?, ?);");
				if(playerFrom != null) {
					insert.setString(1, playerFrom.toString());
				} else insert.setString(1, "");
				insert.setString(2, playerTo.toString());
				insert.setString(3, time);
				final YamlConfiguration itemString = new YamlConfiguration();
				for(Integer i = 0; i < items.size(); i++) itemString.set("i_" + String.valueOf(i), items.get(i));
				insert.setString(4, itemString.saveToString());
				insert.setString(5, label);
				insert.executeUpdate();
				insert.closeOnCompletion();
				final Statement get = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = get.executeQuery("SELECT id FROM mailbox WHERE uuid_to = '" + playerTo.toString() + "' AND sent_date = '" + time + "' ORDER BY id DESC LIMIT 1;");
				get.closeOnCompletion();
				if(results.next()) return results.getInt("id");
			} catch (final SQLException e) {
				e.printStackTrace();
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
			}
			return false;
		}
		public static final boolean isMaiboxOwner(final UUID owner, final Integer mailboxID) {
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = statement.executeQuery("SELECT uuid_to FROM mailbox WHERE id = '" + mailboxID + "';");
				statement.closeOnCompletion();
				if(results.next()) if(UUID.fromString(results.getString("uuid_to")).equals(owner)) return true;
			} catch (final SQLException e) {
				e.printStackTrace();
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
			}
			return null;
		}
		public static final UUID getMailboxFrom(final Integer mailboxID) {
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = statement.executeQuery("SELECT uuid_from FROM mailbox WHERE id = '" + mailboxID + "';");
				statement.closeOnCompletion();
				if(results.next()) {
					try {
						final UUID from = UUID.fromString(results.getString("uuid_from"));
						return from;
					} catch(final IllegalArgumentException exception) {}
					return null;
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		public static final String getMailboxLabel(final Integer mailboxID) {
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = statement.executeQuery("SELECT label FROM mailbox WHERE id = '" + mailboxID + "';");
				statement.closeOnCompletion();
				if(results.next()) {
					String label = results.getString("label");
					if(label == null) label = "";
					return label;
				}
			} catch (final SQLException e) {
				e.printStackTrace();
			}
			return "";
		}
		public static final boolean isMailboxOpened(final Integer mailboxID) {
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = statement.executeQuery("SELECT opened FROM mailbox WHERE id = '" + mailboxID + "';");
				if(results.next()) if(results.getInt("opened") == 1) return true;
				statement.closeOnCompletion();
			} catch (final SQLException e) {
				e.printStackTrace();
			}
			return false;
		}
		public static final HashMap<Integer, String> getMailboxesList(final UUID owner){
			final HashMap<Integer, String> mailboxes = new HashMap<>();
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = statement.executeQuery("SELECT * FROM mailbox WHERE uuid_to = '" + owner.toString() + "' AND opened = '0';");
				while(results.next()) mailboxes.putIfAbsent(results.getInt("id"), results.getString("sent_date"));
				statement.closeOnCompletion();
			} catch (final SQLException e) {
				e.printStackTrace();
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
			}
			return mailboxes;
		}
	}
	public static final class Block {
		public static final boolean isMailboxBlock(final Location block) {
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = statement.executeQuery("SELECT * FROM mailbox_block WHERE mailbox_x = '" + block.getBlockX() + "' AND mailbox_y = '" + block.getBlockY() + "' AND mailbox_z = '" + block.getBlockZ() + "';");
				statement.closeOnCompletion();
				return results.next();
			} catch (final SQLException e) {
				e.printStackTrace();
			}
			return false;
		}
		public static final boolean isMailboxOwner(final UUID owner, final Location block) {
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = statement.executeQuery("SELECT owner_uuid FROM mailbox_block WHERE mailbox_x = '" + block.getBlockX() + "' AND mailbox_y = '" + block.getBlockY() + "' AND mailbox_z = '" + block.getBlockZ() + "';");
				statement.closeOnCompletion();
				if(results.next()) if(UUID.fromString(results.getString("owner_uuid")).equals(owner)) return true;
			} catch (final SQLException e) {
				e.printStackTrace();
			}
			return false;
		}
		public static final UUID getMailboxOwner(final Location block) {
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = statement.executeQuery("SELECT owner_uuid FROM mailbox_block WHERE mailbox_x = '" + block.getBlockX() + "' AND mailbox_y = '" + block.getBlockY() + "' AND mailbox_z = '" + block.getBlockZ() + "';");
				statement.closeOnCompletion();
				if(results.next()) return UUID.fromString(results.getString("owner_uuid"));
			} catch (final SQLException e) {
				e.printStackTrace();
			}
			return null;
		}
		public static final boolean placeMailbox(final UUID owner, Location block) {
			try {
				final PreparedStatement statement = LTItemMail.getInstance().getConnection().prepareStatement("INSERT INTO mailbox_block(owner_uuid, mailbox_x, mailbox_y, mailbox_z) VALUES(?, ?, ?, ?);");
				statement.setString(1, owner.toString());
				statement.setInt(2, block.getBlockX());
				statement.setInt(3, block.getBlockY());
				statement.setInt(4, block.getBlockZ());
				statement.executeUpdate();
				statement.closeOnCompletion();
				return true;
			} catch (final SQLException e) {
				e.printStackTrace();
			}
			return false;
		}
		public static final boolean breakMailbox(final Location block) {
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				statement.executeUpdate("DELETE FROM mailbox_block WHERE mailbox_x = '" + block.getBlockX() + "' AND mailbox_y = '" + block.getBlockY() + "' AND mailbox_z = '" + block.getBlockZ() + "';");
				statement.closeOnCompletion();
				return true;
			} catch (final SQLException e) {
				e.printStackTrace();
			}
			return false;
		}
	}
}