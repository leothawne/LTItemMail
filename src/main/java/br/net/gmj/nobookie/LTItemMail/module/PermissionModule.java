package br.net.gmj.nobookie.LTItemMail.module;

import org.bukkit.command.CommandSender;

import br.net.gmj.nobookie.LTItemMail.module.ext.LTVault;

public class PermissionModule {
	private PermissionModule() {}
	public static final boolean hasPermission(final CommandSender sender, final Type permission) {
		final String node = permission.node();
		if(ExtensionModule.getInstance().isRegistered(ExtensionModule.Function.VAULT_PERMISSION)) return ((LTVault.Permission) ExtensionModule.getInstance().get(ExtensionModule.Function.VAULT_PERMISSION)).getAPI().has(sender, node);
		return sender.hasPermission(node);
	}
	public enum Type {
		CMD_PLAYER_MAIN("ltitemmail.player"),
		CMD_PLAYER_VERSION("ltitemmail.player.version"),
		CMD_PLAYER_LIST("ltitemmail.player.list"),
		CMD_PLAYER_COLOR("ltitemmail.player.color"),
		CMD_PLAYER_OPEN("ltitemmail.player.open"),
		CMD_PLAYER_DELETE("ltitemmail.player.delete"),
		CMD_PLAYER_SEND("ltitemmail.player.send"),
		CMD_PLAYER_PRICE("ltitemmail.player.price"),
		CMD_PLAYER_INFO("ltitemmail.player.info"),
		CMD_PLAYER_BLOCKS("ltitemmail.player.blocks"),
		CMD_PLAYER_NOTIFY("ltitemmail.player.notify"),
		CMD_ADMIN_MAIN("ltitemmail.admin"),
		CMD_ADMIN_UPDATE("ltitemmail.admin.update"),
		CMD_ADMIN_LIST("ltitemmail.admin.list"),
		CMD_ADMIN_RECOVER("ltitemmail.admin.recover"),
		CMD_ADMIN_BAN("ltitemmail.admin.ban"),
		CMD_ADMIN_UNBAN("ltitemmail.admin.unban"),
		CMD_ADMIN_BANLIST("ltitemmail.admin.banlist"),
		CMD_ADMIN_INFO("ltitemmail.admin.info"),
		CMD_ADMIN_RELOAD("ltitemmail.admin.reload"),
		CMD_ADMIN_BLOCKS("ltitemmail.admin.blocks"),
		CMD_ADMIN_NOTIFY("ltitemmail.admin.notify"),
		CMD_ADMIN_BYPASS("ltitemmail.admin.bypass"),
		BLOCK_PLAYER_PLACE("ltitemmail.block.place"),
		BLOCK_PLAYER_BREAK("ltitemmail.block.break"),
		BLOCK_PLAYER_USE("ltitemmail.block.use"),
		BLOCK_ADMIN_BREAK("ltitemmail.block.break.bypass");
		private final String node;
		Type(final String node){
			this.node = node;
		}
		public final String node() {
			return node;
		}
	}
}