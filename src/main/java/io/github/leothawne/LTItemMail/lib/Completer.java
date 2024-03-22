package io.github.leothawne.LTItemMail.lib;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.util.StringUtil;

public final class Completer {
	private Completer() {}
	public static final ArrayList<String> partial(final String token, final Collection<String> from) {
		final ArrayList<String> strings = new ArrayList<String>();
		for(final Object object : StringUtil.copyPartialMatches(token, from, new ArrayList<>(from.size()))) {
			strings.add(object.toString());
		}
        return strings;
    }
}