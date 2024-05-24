package io.github.leothawne.LTItemMail.module;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public final class DataModule {
	private DataModule() {}
	private static final String PROJECT_PAGES = "BUKKIT_DEV:dev.bukkit.org/projects/lt-item-mail,SPIGOT_MC:www.spigotmc.org/resources/62294";
	private static final String VERSIONS = "DATABASE:5,"
			+ "CONFIG_YML:11,"
			+ "ENGLISH_YML:9,"
			+ "PORTUGUESE_YML:9,"
			+ "VIETNAMESE_YML:3,"
			+ "MINECRAFT:1.14,"
			+ "JAVA:8";
	private static final String Plugin_Date = "21/03/2024 19:08 (BRT)";
	private static final String Minecraft_Build = "1.13-R0.1-SNAPSHOT";
	private static final String Update_URL = "https://leothawne.github.io/LTItemMail/api/latest.html";
	private static final String Plugin_URL = "https://leothawne.github.io/LTItemMail/api/$version/plugin.yml";
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
	public static final String getVersionDate() {
		return Plugin_Date;
	}
	public static final String getMinecraftBuild() {
		return Minecraft_Build;
	}
	public static final String getUpdateURL() {
		return Update_URL;
	}
	public static final String getPluginURL(final String version) {
		String Plugin_URL = DataModule.Plugin_URL;
		Plugin_URL = Plugin_URL.replace("$version", version);
		return Plugin_URL;
	}
	public static final void showVersion(final String version, final CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW + "Version " + ChatColor.GREEN + version + ChatColor.YELLOW + " (" + ChatColor.GREEN + Plugin_Date + ChatColor.YELLOW + "), Minecraft " + ChatColor.GREEN + DataModule.getVersion(VersionType.MINECRAFT) + ChatColor.YELLOW + " (Java " + ChatColor.GREEN + DataModule.getVersion(VersionType.JAVA) + ChatColor.YELLOW + ", build " + ChatColor.GREEN + Minecraft_Build + ChatColor.YELLOW + ").");
	}
	public enum VersionType {
		CONFIG_YML,
		ENGLISH_YML,
		PORTUGUESE_YML,
		VIETNAMESE_YML,
		MINECRAFT,
		JAVA,
		DATABASE
	}
	public enum ProjectType {
		BUKKIT_DEV,
		SPIGOT_MC
	}
}
