package io.github.leothawne.LTItemMail.module;

import java.io.File;
import java.sql.Connection;
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
	public static final class Function {
		private Function() {}
		public static final boolean saveMailbox(final UUID owner, final LinkedList<ItemStack> items) {
			try {
				final PreparedStatement statement = LTItemMail.getInstance().getConnection().prepareStatement("INSERT INTO mailbox(uuid_to, sent_date, items) VALUES(?, ?, ?);");
				statement.setString(1, owner.toString());
				statement.setString(2, DateTimeFormatter.ofPattern("dd/MM/yyyy").format(LocalDateTime.now()));
				final YamlConfiguration itemString = new YamlConfiguration();
				for(Integer i = 0; i < items.size(); i++) itemString.set("i_" + String.valueOf(i), items.get(i));
				statement.setString(3, itemString.saveToString());
				statement.executeUpdate();
				statement.closeOnCompletion();
				return true;
			} catch (final SQLException e) {
				e.printStackTrace();
				LTItemMail.getInstance().getConsole().severe("An error occurred while saving a mailbox.");
			}
			return false;
		}
		public static final boolean saveLostMailbox(final Integer mailboxID, final LinkedList<ItemStack> items) {
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				final YamlConfiguration itemString = new YamlConfiguration();
				if(items.size() > 0) {
					for(Integer i = 0; i < items.size(); i++) itemString.set("i_" + String.valueOf(i), items.get(i));
					statement.executeUpdate("UPDATE mailbox SET items_lost = '" + itemString.saveToString() + "' WHERE id = '" + mailboxID + "' AND opened = '1';");
				} else statement.executeUpdate("UPDATE mailbox SET items_lost = '' WHERE id = '" + mailboxID + "' AND opened = '1';");
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
				final ResultSet results = statement.executeQuery("SELECT items FROM mailbox WHERE id = '" + mailboxID + "' AND opened = '0';");
				final YamlConfiguration itemString = new YamlConfiguration();
				while(results.next()) itemString.loadFromString(results.getString("items"));
				statement.closeOnCompletion();
				for(Integer i = 0; i < 27; i++) items.add(i, itemString.getItemStack("i_" + String.valueOf(i)));
				return items;
			} catch (final SQLException | InvalidConfigurationException e) {
				e.printStackTrace();
				LTItemMail.getInstance().getConsole().severe("An error occurred while getting a mailbox items.");
			}
			return null;
		}
		public static final LinkedList<ItemStack> getLostMailbox(final Integer mailboxID) {
			final LinkedList<ItemStack> items = new LinkedList<>();
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = statement.executeQuery("SELECT items_lost FROM mailbox WHERE id = '" + mailboxID + "' AND opened = '1';");
				final YamlConfiguration itemString = new YamlConfiguration();
				Boolean isEmpty = true;
				while(results.next()) if(!results.getString("items_lost").isEmpty() && !results.getString("items_lost").isBlank()) {
					isEmpty = false;
					itemString.loadFromString(results.getString("items_lost"));
				}
				statement.closeOnCompletion();
				if(!isEmpty) for(Integer i = 0; i < 27; i++) items.add(i, itemString.getItemStack("i_" + String.valueOf(i)));
				return items;
			} catch (final SQLException | InvalidConfigurationException e) {
				e.printStackTrace();
				LTItemMail.getInstance().getConsole().severe("An error occurred while getting a mailbox items.");
			}
			return null;
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
				while(results.next()) if(results.getString("uuid_to").equals(owner.toString())) return true;
				statement.closeOnCompletion();
			} catch (final SQLException e) {
				e.printStackTrace();
				LTItemMail.getInstance().getConsole().severe("An error occurred while checking for a mailbox ownership.");
			}
			return false;
		}
		public static final boolean isMailboxOpened(final Integer mailboxID) {
			try {
				final Statement statement = LTItemMail.getInstance().getConnection().createStatement();
				final ResultSet results = statement.executeQuery("SELECT opened FROM mailbox WHERE id = '" + mailboxID + "';");
				while(results.next()) if(results.getInt("opened") == 1) return true;
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