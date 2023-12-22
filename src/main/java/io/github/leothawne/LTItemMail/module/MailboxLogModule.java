package io.github.leothawne.LTItemMail.module;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.bukkit.Bukkit;

import io.github.leothawne.LTItemMail.LTItemMail;

public final class MailboxLogModule {
	private static MailboxLogModule instance;
	private Logger log;
	//private DateTimeFormatter time;
	private MailboxLogModule() {
		instance = this;
		log = Logger.getLogger(MailboxLogModule.class.getName());
		try {
			final FileHandler fh = new FileHandler(LTItemMail.getInstance().getDataFolder() + File.separator + "logs" + File.separator + "mailboxes_" + DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss").format(LocalDateTime.now()) + ".log");
			fh.setFormatter(new SimpleFormatter());
			log.addHandler(fh);
		} catch (final SecurityException | IOException e) {
			e.printStackTrace();
		}
		//log.setUseParentHandlers(false);
		//time = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
	}
	private static final MailboxLogModule getInstance() {
		return instance;
	}
	private final Logger getLogger() {
		return log;
	}
	/*private final String getTime() {
		return time.format(LocalDateTime.now());
	}*/
	public static final void init() {
		new MailboxLogModule();
	}
	public static final boolean log(final UUID playerFrom, UUID playerTo, final ActionType action, final Integer mailboxID) {
		if(playerFrom == null) return false;
		if(action == null) return false;
		if(mailboxID == null) return false;
		String log = /*"[" + MailboxLogModule.getInstance().getTime() + "] " + */Bukkit.getPlayer(playerFrom).getName() + " ";
		switch(action) {
			case RECOVERED:
				log = log + "recovered lost items of ";
				break;
			case OPENED:
				log = log + "deleted ";
				break;
			case RECEIVED:
				log = log + "received ";
				break;
			case SENT:
				log = log + "sent to " + Bukkit.getPlayer(playerTo).getName() + ": ";
				break;
		}
		log = log + "Mailbox#" + mailboxID;
		MailboxLogModule.getInstance().getLogger().info(log);
		return true;
	}
	public enum ActionType {
		RECOVERED,
		OPENED,
		RECEIVED,
		SENT
	}
}