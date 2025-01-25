package br.net.gmj.nobookie.LTItemMail.module.ext.listener;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import br.net.gmj.nobookie.LTItemMail.LTItemMail;
import br.net.gmj.nobookie.LTItemMail.item.MailboxItem;
import br.net.gmj.nobookie.LTItemMail.module.DatabaseModule;
import me.ryanhamshire.GriefPrevention.events.ClaimDeletedEvent;

public class LTGriefPreventionListener implements Listener {
	public LTGriefPreventionListener() {
		Bukkit.getServer().getPluginManager().registerEvents(this, LTItemMail.getInstance());
	}
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public final void onClaimDelete(final ClaimDeletedEvent event) {
		for(final Chunk chunk : event.getClaim().getChunks()) {
			for(int x = 0; x < 15; x++)
				for(int z = 0; z < 15; z++)
					for(int y = 0; y < 255; y++) {
						final Block block = chunk.getBlock(x, y, z);
						if(block != null && block.getType().toString().endsWith("_SHULKER_BOX") && DatabaseModule.Block.isMailboxBlock(block.getLocation())) {
							final ItemStack mailbox = new MailboxItem().getItem(null);
							mailbox.setType(block.getType());
							block.setType(Material.AIR);
							block.getWorld().dropItem(block.getLocation(), mailbox);
						}
					}
		}
	}
}