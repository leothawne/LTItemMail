package io.github.leothawne.LTItemMail.command.tabCompleter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.bukkit.util.StringUtil;

public class TabCompleterAPI {
	protected List<String> partial(String token, Collection<String> from) {
        return StringUtil.copyPartialMatches(token, from, new ArrayList<>(from.size()));
    }
}