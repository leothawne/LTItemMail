package io.github.leothawne.LTItemMail.module;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import io.github.leothawne.LTItemMail.LTItemMail;

public final class DataModule {
	private DataModule() {}
	private static final String PROJECT_PAGES = ""
			+ "BUKKIT_DEV:dev.bukkit.org/projects/lt-item-mail,"
			+ "SPIGOT_MC:www.spigotmc.org/resources/62294";
	private static final String VERSIONS = ""
			+ "DATABASE:4,"
			+ "CONFIG_YML:11,"
			+ "ENGLISH_YML:9,"
			+ "PORTUGUESE_YML:9,"
			+ "VIETNAMESE_YML:3";
	private static final String DATE = "21/03/2024 19:08 (BRT)";
	private static final String API = "1.14-R0.1-SNAPSHOT";
	private static final String JAVA = "1.8";
	private static final String UPDATE = "https://leothawne.github.io/LTItemMail/api/latest.html";
	private static final String PLUGIN = "https://leothawne.github.io/LTItemMail/api/$version/plugin.yml";
	private static final String RESOURCE = "https://github.com/leothawne/LTItemMail-ResourcePack/releases/latest/download/LTItemMail-ResourcePack.zip";
	public static final String getProjectPage(final ProjectType type) {
		final String[] pageString = PROJECT_PAGES.split(",");
		final HashMap<ProjectType, String> pageMap = new HashMap<>();
		for(final String page : pageString) {
			final String[] string = page.split(":");
			pageMap.put(ProjectType.valueOf(string[0]), string[1]);
		}
		return pageMap.get(type);
	}
	public static final String getVersion(final VersionType type) {
		final String[] versionString = VERSIONS.split(",");
		final HashMap<VersionType, String> versionMap = new HashMap<>();
		for(final String version : versionString) {
			final String[] string = version.split(":");
			versionMap.put(VersionType.valueOf(string[0]), string[1]);
		}
		return versionMap.get(type);
	}
	public static final String getDate() {
		return DATE;
	}
	public static final String getJava() {
		return JAVA;
	}
	public static final String getUpdatePath() {
		return UPDATE;
	}
	public static final String getPluginPath(final String version) {
		return PLUGIN.replace("$version", version);
	}
	public static final String getResourcePack() {
		return RESOURCE;
	}
	public static final void showVersion(final String version, final CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW + LTItemMail.getInstance().getDescription().getName() + ": " + ChatColor.GREEN + version + ChatColor.YELLOW + ", Spigot API: " + ChatColor.GREEN + API + ChatColor.YELLOW + ", Java: " + ChatColor.GREEN + JAVA);
	}
	public enum VersionType {
		CONFIG_YML,
		ENGLISH_YML,
		PORTUGUESE_YML,
		VIETNAMESE_YML,
		DATABASE
	}
	public enum ProjectType {
		BUKKIT_DEV,
		SPIGOT_MC
	}
}
