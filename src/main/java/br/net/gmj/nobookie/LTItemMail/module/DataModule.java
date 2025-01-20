package br.net.gmj.nobookie.LTItemMail.module;

import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import br.net.gmj.nobookie.LTItemMail.LTItemMail;

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
	private static final String DATE = "https://jenkins.gmj.net.br/job/LTItemMail/$build/buildTimestamp?format=dd/MM/yyyy%20HH:mm:ss%20z";
	private static final String UPDATE = "https://jenkins.gmj.net.br/job/LTItemMail/lastSuccessfulBuild/buildNumber";
	private static final String PLUGIN = "https://request.gmj.net.br/LTItemMail/$version/manifest.yml";
	private static final String RESOURCE = "https://jenkins.gmj.net.br/job/LTItemMail-ResourcePack/lastSuccessfulBuild/artifact/LTItemMail-ResourcePack.zip";
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
	public static final String getDateURL(final Integer build) {
		return DATE.replace("$build", String.valueOf(build));
	}
	public static final String getUpdateURL() {
		return UPDATE;
	}
	public static final String getPluginPath(final String version) {
		return PLUGIN.replace("$version", version);
	}
	public static final String getResourcePackURL() {
		return RESOURCE;
	}
	public static final void showVersion(final String version, final CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW + LTItemMail.getInstance().getDescription().getName() + ": " + ChatColor.GREEN + version);
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
