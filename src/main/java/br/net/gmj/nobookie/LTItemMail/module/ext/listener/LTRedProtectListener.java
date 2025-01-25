package br.net.gmj.nobookie.LTItemMail.module.ext.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import br.net.fabiozumbi12.RedProtect.Bukkit.API.events.DeleteRegionEvent;
import br.net.gmj.nobookie.LTItemMail.LTItemMail;
import br.net.gmj.nobookie.LTItemMail.item.MailboxItem;
import br.net.gmj.nobookie.LTItemMail.module.DatabaseModule;

public final class LTRedProtectListener implements Listener {
	public LTRedProtectListener() {
		Bukkit.getServer().getPluginManager().registerEvents(this, LTItemMail.getInstance());
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public final void onRegionDelete(final DeleteRegionEvent event) {
		final Location min = event.getRegion().getMinLocation();
		final Location max = event.getRegion().getMaxLocation();
		for(int x = min.getBlockX(); x < max.getBlockX(); x++)
			for(int z = min.getBlockZ(); z < max.getBlockZ(); z++)
				for(int y = min.getBlockY(); y < max.getBlockY(); y++) {
					final Block block = new Location(min.getWorld(), x, y, z).getBlock();
					if(block != null && block.getType().toString().endsWith("_SHULKER_BOX") && DatabaseModule.Block.isMailboxBlock(block.getLocation())) {
						final ItemStack mailbox = new MailboxItem().getItem(null);
						mailbox.setType(block.getType());
						block.setType(Material.AIR);
						block.getWorld().dropItem(block.getLocation(), mailbox);
					}
				}
	}
}