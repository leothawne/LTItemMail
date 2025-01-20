package br.net.gmj.nobookie.LTItemMail.task;

import br.net.gmj.nobookie.LTItemMail.block.MailboxBlock;
import br.net.gmj.nobookie.LTItemMail.entity.LTPlayer;
import br.net.gmj.nobookie.LTItemMail.module.DatabaseModule;
import br.net.gmj.nobookie.LTItemMail.module.ExtensionModule;
import br.net.gmj.nobookie.LTItemMail.module.ext.LTBlueMap;
import br.net.gmj.nobookie.LTItemMail.module.ext.LTDecentHolograms;
import br.net.gmj.nobookie.LTItemMail.module.ext.LTDynmap;

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