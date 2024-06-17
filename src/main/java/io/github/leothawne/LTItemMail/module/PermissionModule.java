package io.github.leothawne.LTItemMail.module;

import org.bukkit.command.CommandSender;

import io.github.leothawne.LTItemMail.module.api.IVault;

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
			case CMD_PLAYER_COLOR:
				node = "ltitemmail.player.color";
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
			case CMD_PLAYER_PRICE:
				node = "ltitemmail.player.price";
				break;
			case CMD_PLAYER_VERSION:
				node = "ltitemmail.player.version";
				break;
			case CMD_PLAYER_INFO:
				node = "ltitemmail.player.info";
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
			case CMD_ADMIN_BAN:
				node = "ltitemmail.admin.ban";
				break;
			case CMD_ADMIN_UNBAN:
				node = "ltitemmail.admin.unban";
				break;
			case CMD_ADMIN_BANLIST:
				node = "ltitemmail.admin.banlist";
				break;
			case CMD_ADMIN_INFO:
				node = "ltitemmail.admin.info";
				break;
		}
		if(node != null) {
			if(IntegrationModule.getInstance().isRegistered(IntegrationModule.Function.VAULT_PERMISSION)) return ((IVault.Permission) IntegrationModule.getInstance().get(IntegrationModule.Function.VAULT_PERMISSION)).getAPI().has(sender, node);
			return sender.hasPermission(node);
		}
		return false;
	}
	public enum Type {
		CMD_PLAYER_MAIN,
		CMD_PLAYER_VERSION,
		CMD_PLAYER_LIST,
		CMD_PLAYER_COLOR,
		CMD_PLAYER_OPEN,
		CMD_PLAYER_DELETE,
		CMD_PLAYER_SEND,
		CMD_PLAYER_PRICE,
		CMD_PLAYER_INFO,
		CMD_ADMIN_MAIN,
		CMD_ADMIN_UPDATE,
		CMD_ADMIN_LIST,
		CMD_ADMIN_RECOVER,
		CMD_ADMIN_BYPASS,
		CMD_PLAYER_NOTIFY,
		CMD_ADMIN_NOTIFY,
		CMD_ADMIN_BAN,
		CMD_ADMIN_UNBAN,
		CMD_ADMIN_BANLIST,
		CMD_ADMIN_INFO,
		CMD_ADMIN_RELOAD,
		BLOCK_PLAYER_PLACE,
		BLOCK_PLAYER_BREAK,
		BLOCK_PLAYER_USE,
		BLOCK_ADMIN_BREAK
	}
}