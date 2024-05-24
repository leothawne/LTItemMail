package io.github.leothawne.LTItemMail.module;

import java.io.File;
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
import org.bukkit.scheduler.BukkitRunnable;

import io.github.leothawne.LTItemMail.LTItemMail;
import io.github.leothawne.LTItemMail.LTPlayer;

public final class DatabaseModule {
	private DatabaseModule() {}
	private static final class FlatFile {
		private static final File file = new File(LTItemMail.getInstance().getDataFolder(), (String) ConfigurationModule.get(ConfigurationModule.Type.DATABASE_FLATFILE_FILE));
		private static final void check() {
			if(!file.exists()) {
				ConsoleModule.warning("Extracting " + (String) ConfigurationModule.get(ConfigurationModule.Type.DATABASE_FLATFILE_FILE) + "...");
				LTItemMail.getInstance().saveResource((String) ConfigurationModule.get(ConfigurationModule.Type.DATABASE_FLATFILE_FILE), false);
				ConsoleModule.info("Done.");
			}
		}
		private static final void connect() {
			if(file.exists()) {
				try {
					LTItemMail.getInstance().connection = DriverManager.getConnection("jdbc:sqlite:" + LTItemMail.getInstance().getDataFolder() + File.separator + (String) ConfigurationModule.get(ConfigurationModule.Type.DATABASE_FLATFILE_FILE));
					if(LTItemMail.getInstance().connection != null) ConsoleModule.info("Loaded FlatFile database.");
				} catch (final SQLException | NullPointerException e) {
					ConsoleModule.severe("Could not load FlatFile database.");
					if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
				}
			}
		}
	}
	private static final class MySQL {
		private static final void connect() {
			try {
				LTItemMail.getInstance().connection = DriverManager.getConnection("jdbc:mysql://" + (String) ConfigurationModule.get(ConfigurationModule.Type.DATABASE_MYSQL_HOST) + "/" + (String) ConfigurationModule.get(ConfigurationModule.Type.DATABASE_MYSQL_NAME), (String) ConfigurationModule.get(ConfigurationModule.Type.DATABASE_MYSQL_USER), (String) ConfigurationModule.get(ConfigurationModule.Type.DATABASE_MYSQL_PASSWORD));
				if(LTItemMail.getInstance().connection != null) ConsoleModule.info("Opened MySQL connection.");
			} catch (final SQLException | NullPointerException e) {
				ConsoleModule.severe("Could not open MySQL connection.");
				ConsoleModule.severe("Check the MySQL login information in config.yml and restart your Minecraft server.");
				ConsoleModule.severe("Is the MySQL server set up correctly?");
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			new BukkitRunnable() {
				@Override
				public final void run() {
					try {
						if(LTItemMail.getInstance().connection == null || (LTItemMail.getInstance().connection != null && !LTItemMail.getInstance().connection.isValid(15))) {
							if(LTItemMail.getInstance().connection != null) LTItemMail.getInstance().connection.close();
							LTItemMail.getInstance().connection = DriverManager.getConnection("jdbc:mysql://" + (String) ConfigurationModule.get(ConfigurationModule.Type.DATABASE_MYSQL_HOST) + "/" + (String) ConfigurationModule.get(ConfigurationModule.Type.DATABASE_MYSQL_NAME), (String) ConfigurationModule.get(ConfigurationModule.Type.DATABASE_MYSQL_USER), (String) ConfigurationModule.get(ConfigurationModule.Type.DATABASE_MYSQL_PASSWORD));
							ConsoleModule.info("Re-opened MySQL connection.");
						}
					} catch (final SQLException | NullPointerException e) {
						ConsoleModule.severe("MySQL connection was closed and could not be opened again.");
						ConsoleModule.severe("Is the MySQL server set up correctly?");
						if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
						this.cancel();
					}
				}
			}.runTaskTimer(LTItemMail.getInstance(), 20 * 60, 20 * 60);
		}
	}
	public static final void connect() {
		switch(((String) ConfigurationModule.get(ConfigurationModule.Type.DATABASE_TYPE)).toLowerCase()) {
			case "flatfile":
				FlatFile.check();
				FlatFile.connect();
				break;
			case "mysql":
				MySQL.connect();
				break;
		}
	}
	public static final void disconnect() {
		try {
			LTItemMail.getInstance().connection.close();
		} catch (final SQLException | NullPointerException e) {
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
		}
	}
	private static final int getCurrentVersion() {
		try {
			ResultSet results = null;
			Boolean exists = false;
			switch(((String) ConfigurationModule.get(ConfigurationModule.Type.DATABASE_TYPE)).toLowerCase()) {
				case "flatfile":
					final DatabaseMetaData meta = LTItemMail.getInstance().connection.getMetaData();
					results = meta.getTables(null, null, "config", new String[] {"TABLE"});
					exists = results.next();
					break;
				case "mysql":
					final Statement statement = LTItemMail.getInstance().connection.createStatement();
					results = statement.executeQuery("SHOW TABLES;");
					statement.closeOnCompletion();
					while(results.next()) if(results.getString(1).equals("config")) {
						exists = true;
						break;
					}
					break;
			}
			if(exists) {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				results = statement.executeQuery("SELECT version FROM config;");
				statement.closeOnCompletion();
				if(results.next()) return results.getInt("version");
			}
		} catch (final SQLException | NullPointerException e) {
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
		}
		return 0;
	}
	public static final void checkForUpdates() {
		final Integer dbVer = getCurrentVersion();
		if(dbVer < Integer.valueOf(DataModule.getVersion(DataModule.VersionType.DATABASE))) {
			for(Integer i = dbVer; i < Integer.valueOf(DataModule.getVersion(DataModule.VersionType.DATABASE)); i++) {
				if(((String) ConfigurationModule.get(ConfigurationModule.Type.DATABASE_TYPE)).toLowerCase().equals("mysql") && i > 0 && i < 5) continue;
				ConsoleModule.info("Updating database... (" + i + " -> " + (i + 1) + ")");
				if(DatabaseModule.runSQL(i)) {
					ConsoleModule.info("Database updated! (" + i + " -> " + (i + 1) + ")");
				} else ConsoleModule.severe("Database update failed! (" + i + " -> " + (i + 1) + ")");
			}
		} else ConsoleModule.info("Database is up to date! (" + dbVer + ")");
	}
	private static final boolean runSQL(final int version) {
		final LinkedList<String> sql = new LinkedList<>();
		switch(((String) ConfigurationModule.get(ConfigurationModule.Type.DATABASE_TYPE)).toLowerCase()) {
			case "flatfile":
				switch(version) {
					case 0:
						sql.add("CREATE TABLE config(version INTEGER NOT NULL);");
						sql.add("INSERT INTO config(version) VALUES('1');");
						sql.add("ALTER TABLE mailbox DROP COLUMN items_lost;");
						break;
					case 1:
						sql.add("CREATE TABLE mailbox_block ("
								+ "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,"
								+ "owner_uuid TEXT NOT NULL,"
								+ "mailbox_x  INTEGER NOT NULL,"
								+ "mailbox_y  INTEGER NOT NULL,"
								+ "mailbox_z  INTEGER NOT NULL"
								+ ");");
						sql.add("UPDATE config SET version = '2';");
						break;
					case 2:
						sql.add("ALTER TABLE mailbox ADD label TEXT;");
						sql.add("UPDATE config SET version = '3';");
						break;
					case 3:
						sql.add("CREATE TABLE users ("
							+ "uuid TEXT PRIMARY KEY NOT NULL,"
							+ "sent_count INTEGER NOT NULL,"
							+ "received_count INTEGER NOT NULL,"
							+ "ban INTEGER NOT NULL,"
							+ "ban_reason TEXT,"
							+ "registered_date TEXT NOT NULL"
							+ ");");
						sql.add("UPDATE config SET version = '4';");
						break;
					case 4:
						sql.add("ALTER TABLE mailbox ADD status TEXT NOT NULL DEFAULT 'PENDING';");
						sql.add("ALTER TABLE mailbox RENAME COLUMN opened TO deleted;");
						sql.add("UPDATE config SET version = '5';");
						break;
				}
				break;
			case "mysql":
				switch(version) {
					case 0:
						sql.add("DROP TABLE IF EXISTS config;");
						sql.add("CREATE TABLE config ("
								+ "version int NOT NULL"
								+ ") ENGINE=MyISAM DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;");
						sql.add("LOCK TABLES config WRITE;");
						sql.add("INSERT INTO config VALUES(5);");
						sql.add("UNLOCK TABLES;");
						
						sql.add("DROP TABLE IF EXISTS mailbox;");
						sql.add("CREATE TABLE mailbox ("
								+ "id int NOT NULL AUTO_INCREMENT,"
								+ "uuid_from longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci,"
								+ "uuid_to longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,"
								+ "sent_date longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,"
								+ "items longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,"
								+ "deleted int NOT NULL DEFAULT '0',"
								+ "label longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci,"
								+ "status tinytext CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci,"
								+ "PRIMARY KEY (id)"
								+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;");

						sql.add("DROP TABLE IF EXISTS mailbox_block;");
						sql.add("CREATE TABLE mailbox_block ("
								+ "id int NOT NULL AUTO_INCREMENT,"
								+ "owner_uuid longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,"
								+ "mailbox_x int NOT NULL,"
								+ "mailbox_y int NOT NULL,"
								+ "mailbox_z int NOT NULL,"
								+ "PRIMARY KEY (id)"
								+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;");

						sql.add("DROP TABLE IF EXISTS users;");
						sql.add("CREATE TABLE users ("
								+ "uuid varchar(36) CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,"
								+ "sent_count int NOT NULL DEFAULT '0',"
								+ "received_count int NOT NULL DEFAULT '0',"
								+ "ban int NOT NULL DEFAULT '0',"
								+ "ban_reason longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci,"
								+ "registered_date longtext CHARACTER SET utf8mb3 COLLATE utf8mb3_unicode_ci NOT NULL,"
								+ "PRIMARY KEY (uuid)"
								+ ") ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_unicode_ci;");
						break;
				}
				break;
		}
		if(sql.size() > 0) try {
			for(final String lines : sql) {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				statement.execute(lines);
				statement.closeOnCompletion();
			}
			return true;
		} catch (SQLException | NullPointerException e) {
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
		}
		return false;
	}
	public static final class Virtual {
		public static final int saveMailbox(final UUID playerFrom, UUID playerTo, final LinkedList<ItemStack> items, final String label) {
			final String time = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now());
			try {
				final PreparedStatement insert = LTItemMail.getInstance().connection.prepareStatement("INSERT INTO mailbox(uuid_from, uuid_to, sent_date, items, label, status) VALUES(?, ?, ?, ?, ?, ?);");
				if(playerFrom != null) {
					insert.setString(1, playerFrom.toString());
				} else insert.setString(1, "");
				insert.setString(2, playerTo.toString());
				insert.setString(3, time);
				final YamlConfiguration itemString = new YamlConfiguration();
				for(Integer i = 0; i < items.size(); i++) itemString.set("i_" + String.valueOf(i), items.get(i));
				insert.setString(4, itemString.saveToString());
				insert.setString(5, label);
				insert.setString(6, Status.PENDING.toString());
				insert.executeUpdate();
				insert.closeOnCompletion();
				final Statement get = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = get.executeQuery("SELECT id FROM mailbox WHERE uuid_to = '" + playerTo.toString() + "' AND sent_date = '" + time + "' ORDER BY id DESC LIMIT 1;");
				get.closeOnCompletion();
				if(results.next()) return results.getInt("id");
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return 0;
		}
		public static final boolean updateMailbox(final Integer mailboxID, final LinkedList<ItemStack> items) {
			try {
				final PreparedStatement statement = LTItemMail.getInstance().connection.prepareStatement("UPDATE mailbox SET items = ?, deleted = ? WHERE id = ?;");
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
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return false;
		}
		public static final LinkedList<ItemStack> getMailbox(final Integer mailboxID) {
			final LinkedList<ItemStack> items = new LinkedList<>();
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT items FROM mailbox WHERE id = '" + mailboxID + "';");
				final YamlConfiguration itemString = new YamlConfiguration();
				Boolean empty = false;
				if(results.next()) if(!results.getString("items").equals("")) {
					itemString.loadFromString(results.getString("items"));
				} else empty = true;
				statement.closeOnCompletion();
				if(!empty) for(Integer i = 0; i < 27; i++) items.add(i, itemString.getItemStack("i_" + String.valueOf(i)));
			} catch (final SQLException | NullPointerException | InvalidConfigurationException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return items;
		}
		public static final boolean setMailboxDeleted(final Integer mailboxID) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				statement.executeUpdate("UPDATE mailbox SET deleted = '1' WHERE id = '" + mailboxID + "';");
				statement.closeOnCompletion();
				return true;
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return false;
		}
		public static final boolean isMaiboxOwner(final UUID owner, final Integer mailboxID) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT uuid_to FROM mailbox WHERE id = '" + mailboxID + "';");
				statement.closeOnCompletion();
				if(results.next()) if(UUID.fromString(results.getString("uuid_to")).equals(owner)) return true;
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return false;
		}
		public static final UUID getMailboxOwner(final Integer mailboxID) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT uuid_to FROM mailbox WHERE id = '" + mailboxID + "';");
				statement.closeOnCompletion();
				if(results.next()) return UUID.fromString(results.getString("uuid_to"));
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return null;
		}
		public static final UUID getMailboxFrom(final Integer mailboxID) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT uuid_from FROM mailbox WHERE id = '" + mailboxID + "';");
				statement.closeOnCompletion();
				if(results.next()) {
					try {
						final UUID from = UUID.fromString(results.getString("uuid_from"));
						return from;
					} catch(final IllegalArgumentException exception) {}
					return null;
				}
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return null;
		}
		public static final String getMailboxLabel(final Integer mailboxID) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT label FROM mailbox WHERE id = '" + mailboxID + "';");
				statement.closeOnCompletion();
				if(results.next()) {
					String label = results.getString("label");
					if(label == null) label = "";
					return label;
				}
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return "";
		}
		public static final boolean isMailboxDeleted(final Integer mailboxID) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT deleted FROM mailbox WHERE id = '" + mailboxID + "';");
				if(results.next()) if(results.getInt("deleted") == 1) return true;
				statement.closeOnCompletion();
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return false;
		}
		public static final HashMap<Integer, String> getMailboxesList(final UUID owner){
			final HashMap<Integer, String> mailboxes = new HashMap<>();
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT * FROM mailbox WHERE uuid_to = '" + owner.toString() + "' AND deleted = '0';");
				while(results.next()) mailboxes.putIfAbsent(results.getInt("id"), results.getString("sent_date"));
				statement.closeOnCompletion();
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return mailboxes;
		}
		public static final HashMap<Integer, String> getDeletedMailboxesList(final UUID owner){
			final HashMap<Integer, String> mailboxes = new HashMap<>();
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT id,sent_date FROM mailbox WHERE uuid_to = '" + owner.toString() + "' AND deleted = '1';");
				while(results.next()) mailboxes.putIfAbsent(results.getInt("id"), results.getString("sent_date"));
				statement.closeOnCompletion();
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return mailboxes;
		}
		public static final void setStatus(final Integer mailboxID, final Status status) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				statement.executeUpdate("UPDATE mailbox SET status = '" + status.toString() + "' WHERE id = '" + mailboxID + "';");
				statement.closeOnCompletion();
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
		}
		public static final Status getStatus(final Integer mailboxID) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT status FROM mailbox WHERE id = '" + mailboxID + "';");
				if(results.next()) return Status.valueOf(results.getString("status"));
				statement.closeOnCompletion();
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return Status.UNDEFINED;
		}
		public enum Status {
			PENDING,
			DENIED,
			ACCEPTED,
			UNDEFINED
		}
	}
	public static final class Block {
		public static final boolean isMailboxBlock(final Location block) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT * FROM mailbox_block WHERE mailbox_x = '" + block.getBlockX() + "' AND mailbox_y = '" + block.getBlockY() + "' AND mailbox_z = '" + block.getBlockZ() + "';");
				statement.closeOnCompletion();
				return results.next();
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return false;
		}
		public static final boolean isMailboxOwner(final UUID owner, final Location block) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT owner_uuid FROM mailbox_block WHERE mailbox_x = '" + block.getBlockX() + "' AND mailbox_y = '" + block.getBlockY() + "' AND mailbox_z = '" + block.getBlockZ() + "';");
				statement.closeOnCompletion();
				if(results.next()) if(UUID.fromString(results.getString("owner_uuid")).equals(owner)) return true;
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return false;
		}
		public static final UUID getMailboxOwner(final Location block) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT owner_uuid FROM mailbox_block WHERE mailbox_x = '" + block.getBlockX() + "' AND mailbox_y = '" + block.getBlockY() + "' AND mailbox_z = '" + block.getBlockZ() + "';");
				statement.closeOnCompletion();
				if(results.next()) return UUID.fromString(results.getString("owner_uuid"));
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return null;
		}
		public static final boolean placeMailbox(final UUID owner, Location block) {
			try {
				final PreparedStatement statement = LTItemMail.getInstance().connection.prepareStatement("INSERT INTO mailbox_block(owner_uuid, mailbox_x, mailbox_y, mailbox_z) VALUES(?, ?, ?, ?);");
				statement.setString(1, owner.toString());
				statement.setInt(2, block.getBlockX());
				statement.setInt(3, block.getBlockY());
				statement.setInt(4, block.getBlockZ());
				statement.executeUpdate();
				statement.closeOnCompletion();
				return true;
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return false;
		}
		public static final boolean breakMailbox(final Location block) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				statement.executeUpdate("DELETE FROM mailbox_block WHERE mailbox_x = '" + block.getBlockX() + "' AND mailbox_y = '" + block.getBlockY() + "' AND mailbox_z = '" + block.getBlockZ() + "';");
				statement.closeOnCompletion();
				return true;
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return false;
		}
	}
	public static final class User {
		public static final boolean ban(final UUID user, String reason) {
			try {
				final PreparedStatement statement = LTItemMail.getInstance().connection.prepareStatement("UPDATE users SET ban = ?, ban_reason = ? WHERE uuid = ?;");
				statement.setInt(1, 1);
				statement.setString(2, reason);
				statement.setString(3, user.toString());
				statement.executeUpdate();
				statement.closeOnCompletion();
				return true;
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return false;
		}
		public static final boolean unban(final UUID user) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				statement.executeUpdate("UPDATE users SET ban = '0', ban_reason = '' WHERE uuid = '" + user + "';");
				statement.closeOnCompletion();
				return true;
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return false;
		}
		public static final boolean setSentCount(final UUID user, final Integer sent) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				statement.executeUpdate("UPDATE users SET sent_count = '" + sent + "' WHERE uuid = '" + user + "';");
				statement.closeOnCompletion();
				return true;
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return false;
		}
		public static final boolean setReceivedCount(final UUID user, final Integer received) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				statement.executeUpdate("UPDATE users SET received_count = '" + received + "' WHERE uuid = '" + user + "';");
				statement.closeOnCompletion();
				return true;
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return false;
		}
		public static final boolean isBanned(final UUID user) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT ban FROM users WHERE uuid = '" + user + "';");
				statement.closeOnCompletion();
				if(results.next()) return results.getInt("ban") == 1;
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return false;
		}
		public static final LinkedList<String> getBansList(){
			final LinkedList<String> banlist = new LinkedList<>();
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT uuid FROM users WHERE ban = '1';");
				while(results.next()) banlist.add(LTPlayer.fromUUID(UUID.fromString(results.getString("uuid"))).getName());
				statement.closeOnCompletion();
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return banlist;
		}
		public static final String getBanReason(final UUID user) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT ban_reason FROM users WHERE uuid = '" + user + "';");
				statement.closeOnCompletion();
				if(results.next()) return results.getString("ban_reason");
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return null;
		}
		public static final boolean register(final UUID user) {
			final String time = DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now());
			try {
				final PreparedStatement insert = LTItemMail.getInstance().connection.prepareStatement("INSERT INTO users(uuid, sent_count, received_count, ban, registered_date) VALUES(?, ?, ?, ?, ?);");
				insert.setString(1, user.toString());
				insert.setInt(2, 0);
				insert.setInt(3, 0);
				insert.setInt(4, 0);
				insert.setString(5, time);
				insert.executeUpdate();
				insert.closeOnCompletion();
				return true;
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return false;
		}
		public static final boolean isRegistered(final UUID user) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT * FROM users WHERE uuid = '" + user + "';");
				statement.closeOnCompletion();
				return results.next();
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return false;
		}
		public static final int getSentCount(final UUID user) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT sent_count FROM users WHERE uuid = '" + user + "';");
				statement.closeOnCompletion();
				if(results.next()) return results.getInt("sent_count");
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return 0;
		}
		public static final int getReceivedCount(final UUID user) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT received_count FROM users WHERE uuid = '" + user + "';");
				statement.closeOnCompletion();
				if(results.next()) return results.getInt("received_count");
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return 0;
		}
		public static final String getRegistryDate(final UUID user) {
			try {
				final Statement statement = LTItemMail.getInstance().connection.createStatement();
				final ResultSet results = statement.executeQuery("SELECT registered_date FROM users WHERE uuid = '" + user + "';");
				statement.closeOnCompletion();
				if(results.next()) return results.getString("registered_date");
			} catch (final SQLException | NullPointerException e) {
				if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
			}
			return null;
		}
	}
}