package io.github.leothawne.LTItemMail.module;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import io.github.leothawne.LTItemMail.type.ProjectPageType;
import io.github.leothawne.LTItemMail.type.VersionType;

public final class DataModule {
	private static final String PROJECT_PAGES = "BUKKIT_DEV:https://dev.bukkit.org/projects/lt-item-mail,SPIGOT_MC:https://www.spigotmc.org/resources/62294";
	private static final String VERSIONS = "CONFIG_YML:6,ENGLISH_YML:4,PORTUGUESE_YML:4,VIETNAMESE_YML:2,MINECRAFT:1.19,JAVA:8";
	private static final String Plugin_Date = "17/12/2023 02:45 (BRT)";
	private static final String Minecraft_Build = "1.19-R0.1-SNAPSHOT";
	private static final String Update_URL = "https://leothawne.github.io/LTItemMail/api/1.19.html";
	private static final String Plugin_URL = "https://leothawne.github.io/LTItemMail/api/$version/plugin.html";
	public static final String getProjectPage(final ProjectPageType type) {
		final String[] pageString = DataModule.PROJECT_PAGES.split(",");
		final HashMap<ProjectPageType, String> pageMap = new HashMap<ProjectPageType, String>();
		for(final String page : pageString) {
			final String[] string = page.split(":");
			pageMap.put(ProjectPageType.valueOf(string[0]), string[1]);
		}
		return pageMap.get(type);
	}
	public static final String getVersion(final VersionType type) {
		final String[] versionString = DataModule.VERSIONS.split(",");
		final HashMap<VersionType, String> versionMap = new HashMap<VersionType, String>();
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
	public static final void version(final String version, final CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW + "Version " + ChatColor.GREEN + version + ChatColor.YELLOW + " (" + ChatColor.GREEN + Plugin_Date + ChatColor.YELLOW + "), Minecraft " + ChatColor.GREEN + DataModule.getVersion(VersionType.MINECRAFT) + ChatColor.YELLOW + " (Java " + ChatColor.GREEN + DataModule.getVersion(VersionType.JAVA) + ChatColor.YELLOW + ", build " + ChatColor.GREEN + Minecraft_Build + ChatColor.YELLOW + ").");
	}
}