package io.github.leothawne.LTItemMail.module;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;

import io.github.leothawne.LTItemMail.LTItemMail;

public final class MailboxLogModule {
	private static MailboxLogModule instance;
	private Logger log;
	//private DateTimeFormatter time;
	private MailboxLogModule() {
		instance = this;
		log = Logger.getLogger(MailboxLogModule.class.getName());
		try {
			Files.createDirectories(Paths.get(LTItemMail.getInstance().getDataFolder() + File.separator + "logs"));
			final FileHandler fh = new FileHandler(LTItemMail.getInstance().getDataFolder() + File.separator + "logs" + File.separator + "mailboxes_" + DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").format(LocalDateTime.now()) + ".log");
			fh.setFormatter(new SimpleFormatter());
			log.addHandler(fh);
		} catch (final SecurityException | IOException e) {
			e.printStackTrace();
		}
	}
	private static final MailboxLogModule getInstance() {
		return instance;
	}
	private final Logger getLogger() {
		return log;
	}
	public static final void init() {
		new MailboxLogModule();
	}
	public static final boolean log(final UUID playerFrom, UUID playerTo, final Action action, final Integer mailboxID, final Location mailboxBlock) {
		if(playerFrom == null) return false;
		if(action == null) return false;
		String log = Bukkit.getOfflinePlayer(playerFrom).getName() + " ";
		switch(action) {
			case RECOVERED:
				log = log + "recovered lost items of Mailbox#" + mailboxID;
				break;
			case OPENED:
				log = log + "deleted Mailbox#" + mailboxID;
				break;
			case RECEIVED:
				log = log + "received Mailbox#" + mailboxID;
				break;
			case SENT:
				String offline = "";
				final OfflinePlayer offPlayerTo = Bukkit.getOfflinePlayer(playerTo);
				if(offPlayerTo.getPlayer() == null) offline = " (offline)";
				log = log + "sent to " + offPlayerTo.getName() + offline + ": Mailbox#" + mailboxID;
				break;
			case PLACED:
				log = log + "placed a mailbox at X: " + mailboxBlock.getBlockX() + ", Y: " + mailboxBlock.getBlockY() + ", Z: " + mailboxBlock.getBlockZ();
				break;
			case BROKE:
				log = log + "broke a mailbox at X: " + mailboxBlock.getBlockX() + ", Y: " + mailboxBlock.getBlockY() + ", Z: " + mailboxBlock.getBlockZ();
				break;
			case ADMIN_BROKE:
				log = log + "broke the mailbox of " + Bukkit.getOfflinePlayer(playerTo).getName() + " at X: " + mailboxBlock.getBlockX() + ", Y: " + mailboxBlock.getBlockY() + ", Z: " + mailboxBlock.getBlockZ();
				break;
		}
		MailboxLogModule.getInstance().getLogger().info(log);
		return true;
	}
	public enum Action {
		RECOVERED,
		OPENED,
		RECEIVED,
		SENT,
		PLACED,
		BROKE,
		ADMIN_BROKE
	}
}