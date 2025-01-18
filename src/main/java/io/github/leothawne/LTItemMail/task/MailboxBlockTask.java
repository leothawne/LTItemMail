package io.github.leothawne.LTItemMail.task;

import io.github.leothawne.LTItemMail.block.MailboxBlock;
import io.github.leothawne.LTItemMail.entity.LTPlayer;
import io.github.leothawne.LTItemMail.module.DatabaseModule;
import io.github.leothawne.LTItemMail.module.ExtensionModule;
import io.github.leothawne.LTItemMail.module.ext.LTBlueMap;
import io.github.leothawne.LTItemMail.module.ext.LTDecentHolograms;
import io.github.leothawne.LTItemMail.module.ext.LTDynmap;

public final class MailboxBlockTask implements Runnable {
	private final LTBlueMap blueMap = (LTBlueMap) ExtensionModule.getInstance().get(ExtensionModule.Function.BLUEMAP);
	private final LTDecentHolograms decentHolograms = (LTDecentHolograms) ExtensionModule.getInstance().get(ExtensionModule.Function.DECENTHOLOGRAMS);
	private final LTDynmap dynmap = (LTDynmap) ExtensionModule.getInstance().get(ExtensionModule.Function.DYNMAP);
	@Override
	public final void run() {
		for(final MailboxBlock block : DatabaseModule.Block.getMailboxBlocks()) if(!block.getBukkitBlock().getType().toString().endsWith("_SHULKER_BOX")) {
			block.remove(true);
			if(blueMap != null) blueMap.deleteMarker(LTPlayer.fromUUID(block.getOwner()).getBukkitPlayer(), block.getLocation(), false);
			if(decentHolograms != null) decentHolograms.deleteHolo(LTPlayer.fromUUID(block.getOwner()).getBukkitPlayer(), block.getLocation());
			if(dynmap != null) dynmap.deleteMarker(LTPlayer.fromUUID(block.getOwner()).getBukkitPlayer(), block.getLocation());
		}
	}
}