package io.github.leothawne.LTItemMail;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.Charset;

import io.github.leothawne.LTItemMail.api.JSON.JSONObject;

public class AdLoader {
	private ConsoleLoader myLogger;
	public AdLoader(ConsoleLoader myLogger) {
		this.myLogger = myLogger;
	}
	public void show() {
		try {
			URLConnection allowedUrl = new URL("https://leothawne.github.io/AdCenter/adlist.json").openConnection();
			allowedUrl.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.11 (KHTML, like Gecko) Chrome/23.0.1271.95 Safari/537.11");
			allowedUrl.connect();
			BufferedReader allowedReader = new BufferedReader(new InputStreamReader(allowedUrl.getInputStream(), Charset.forName("UTF-8")));
			StringBuilder sb = new StringBuilder();
			String line;
			while((line = allowedReader.readLine()) != null) {
				sb.append(line);
			}
			if(sb.toString() != null) {
				JSONObject adlist = new JSONObject(sb.toString());
			}
		} catch(Exception e) {
			myLogger.severe("Ad server did not respond!");
		}
	}
}