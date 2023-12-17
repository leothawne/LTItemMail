package io.github.leothawne.LTItemMail.module;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import io.github.leothawne.LTItemMail.LTItemMail;

public final class DatabaseModule {
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
				final Connection con = DriverManager.getConnection("jdbc:sqlite:" + databaseFile.getCanonicalPath());
				return con;
			} catch (final SQLException | IOException e) {
				e.printStackTrace();
			}
			LTItemMail.getInstance().getConsole().severe("A database error occurred.");
			return null;
		}
		LTItemMail.getInstance().getConsole().severe("Missing mailboxes.db file.");
		return null;
	}
}