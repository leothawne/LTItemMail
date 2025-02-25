package br.net.gmj.nobookie.LTItemMail.module.ext;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import br.net.gmj.nobookie.LTItemMail.LTItemMail;
import br.net.gmj.nobookie.LTItemMail.module.ConfigurationModule;
import br.net.gmj.nobookie.LTItemMail.module.ConsoleModule;
import br.net.gmj.nobookie.LTItemMail.module.ExtensionModule;
import br.net.gmj.nobookie.LTItemMail.module.ExtensionModule.Function;
import me.arcaniax.hdb.api.DatabaseLoadEvent;
import me.arcaniax.hdb.api.HeadDatabaseAPI;

public final class LTHeadDatabase implements Listener {
	private HeadDatabaseAPI api = null;
	public LTHeadDatabase() {
		Bukkit.getPluginManager().registerEvents(this, LTItemMail.getInstance());
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = false)
	public final void onDatabaseLoad(final DatabaseLoadEvent event) {
		api = new HeadDatabaseAPI();
	}
	public final ItemStack getHead(final String id) {
		try {
			return api.getItemHead(id);
		} catch(final NullPointerException e) {
			ConsoleModule.debug(getClass(), "Unable to retrieve head from HeadDatabase [" + id + "].");
			if((Boolean) ConfigurationModule.get(ConfigurationModule.Type.PLUGIN_DEBUG)) e.printStackTrace();
		}
		return null;
	}
	public final String getId(final ItemStack head) {
		return api.getItemID(head);
	}
	public enum Type {
		MAILBOX_BUTTON_COST("60568"),
		MAILBOX_BUTTON_LABEL("66505"),
		MAILBOX_BUTTON_DENY("106285"),
		MAILBOX_BUTTON_ACCEPT("106284");
		private final String id;
		Type(final String id){
			this.id = id;
		}
		public final String id() {
			return id;
		}
		public final ItemStack head() {
			final LTHeadDatabase headDB = (LTHeadDatabase) ExtensionModule.getInstance().get(Function.HEADDATABASE);
			if(headDB != null) return headDB.getHead(id);
			return null;
		}
	}
}