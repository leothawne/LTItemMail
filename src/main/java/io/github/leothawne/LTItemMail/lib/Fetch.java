package io.github.leothawne.LTItemMail.lib;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URLConnection;
import java.nio.charset.Charset;

public final class Fetch {
	private Fetch() {}
	public static final String get(final String url) {
		try {
			final URLConnection connection = URI.create(url).toURL().openConnection();
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/72.0.3626.119 Safari/537.36");
			connection.setUseCaches(false);
			connection.connect();
			final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream(), Charset.forName("UTF-8")));
			final StringBuilder builder = new StringBuilder();
			String string;
			while((string = reader.readLine()) != null) builder.append(string);
			return builder.toString();
		} catch(final Exception exception) {
			return null;
		}
	}
}