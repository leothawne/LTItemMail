package io.github.leothawne.LTItemMail.api;

import java.util.ArrayList;
import java.util.Collection;

import org.bukkit.util.StringUtil;

public final class TabCompleterAPI {
	private TabCompleterAPI() {}
	public static final ArrayList<String> partial(final String token, final Collection<String> from) {
		final ArrayList<String> strings = new ArrayList<String>();
		for(final Object object : StringUtil.copyPartialMatches(token, from, new ArrayList<>(from.size()))) {
			strings.add(object.toString());
		}
        return strings;
    }
}