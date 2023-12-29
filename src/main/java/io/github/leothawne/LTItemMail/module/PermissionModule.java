package io.github.leothawne.LTItemMail.module;

import org.bukkit.command.CommandSender;

public class PermissionModule {
	private PermissionModule() {}
	public static final boolean hasPermission(final CommandSender sender, final Type permission) {
		String node = null;
		switch(permission) {
			case CMD_ADMIN_LIST:
				node = "ltitemmail.admin.list";
				break;
			case CMD_ADMIN_MAIN:
				node = "ltitemmail.admin";
				break;
			case CMD_ADMIN_RECOVER:
				node = "ltitemmail.admin.recover";
				break;
			case CMD_ADMIN_UPDATE:
				node = "ltitemmail.admin.update";
				break;
			case CMD_PLAYER_DELETE:
				node = "ltitemmail.player.delete";
				break;
			case CMD_PLAYER_LIST:
				node = "ltitemmail.player.list";
				break;
			case CMD_PLAYER_MAIN:
				node = "ltitemmail.player";
				break;
			case CMD_PLAYER_OPEN:
				node = "ltitemmail.player.open";
				break;
			case CMD_PLAYER_SEND:
				node = "ltitemmail.player.send";
				break;
			case CMD_PLAYER_VERSION:
				node = "ltitemmail.player.version";
				break;
			case CMD_ADMIN_BYPASS:
				node = "ltitemmail.admin.bypass";
				break;
			case CMD_ADMIN_NOTIFY:
				node = "ltitemmail.admin.notify";
				break;
			case CMD_PLAYER_NOTIFY:
				node = "ltitemmail.player.notify";
				break;
			case BLOCK_PLAYER_BREAK:
				node = "ltitemmail.block.break";
				break;
			case BLOCK_PLAYER_PLACE:
				node = "ltitemmail.block.place";
				break;
			case BLOCK_PLAYER_USE:
				node = "ltitemmail.block.use";
				break;
			case BLOCK_ADMIN_BREAK:
				node = "ltitemmail.block.break-bypass";
				break;
			case CMD_ADMIN_RELOAD:
				node = "ltitemmail.admin.reload";
				break;
		}
		if(node != null) return sender.hasPermission(node);
		return false;
	}
	public enum Type {
		CMD_PLAYER_MAIN,
		CMD_PLAYER_VERSION,
		CMD_PLAYER_LIST,
		CMD_PLAYER_OPEN,
		CMD_PLAYER_DELETE,
		CMD_PLAYER_SEND,
		CMD_ADMIN_MAIN,
		CMD_ADMIN_UPDATE,
		CMD_ADMIN_LIST,
		CMD_ADMIN_RECOVER,
		CMD_ADMIN_BYPASS,
		CMD_PLAYER_NOTIFY,
		CMD_ADMIN_NOTIFY,
		BLOCK_PLAYER_PLACE,
		BLOCK_PLAYER_BREAK,
		BLOCK_PLAYER_USE,
		BLOCK_ADMIN_BREAK,
		CMD_ADMIN_RELOAD
	}
}