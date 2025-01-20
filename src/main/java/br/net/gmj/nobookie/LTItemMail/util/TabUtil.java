package br.net.gmj.nobookie.LTItemMail.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.util.StringUtil;

public final class TabUtil {
	private TabUtil() {}
	public static final List<String> partial(final String token, final Collection<String> from) {
		final List<String> response = new ArrayList<String>();
		for(final Object object : StringUtil.copyPartialMatches(token, from, new ArrayList<>(from.size()))) response.add(object.toString());
        return response;
    }
}