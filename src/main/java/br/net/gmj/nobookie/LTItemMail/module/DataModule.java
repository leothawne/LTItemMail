package br.net.gmj.nobookie.LTItemMail.module;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

import br.net.gmj.nobookie.LTItemMail.LTItemMail;

public final class DataModule {
	private DataModule() {}
	private static final String DATE = "https://jenkins.gmj.net.br/job/LTItemMail/$build/buildTimestamp?format=dd/MM/yyyy%20HH:mm:ss%20z";
	private static final String UPDATE = "https://jenkins.gmj.net.br/job/LTItemMail/lastSuccessfulBuild/buildNumber";
	private static final String PLUGIN = "https://request.gmj.net.br/LTItemMail/$version/manifest.yml";
	private static final String RESOURCE = "https://jenkins.gmj.net.br/job/LTItemMail-ResourcePack/lastSuccessfulBuild/artifact/LTItemMail-ResourcePack.zip";
	private static final Integer STABLE = 52;
	public static final Integer getVersion(final VersionType type) {
		return type.value();
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
	public static final Integer getLatestStable() {
		return STABLE;
	}
	public static final void showVersion(final String version, final CommandSender sender) {
		sender.sendMessage(ChatColor.YELLOW + LTItemMail.getInstance().getDescription().getName() + " " + version);
	}
	public enum VersionType {
		CONFIG_YML(11),
		ITEM_MODELS_YML(1),
		ENGLISH_YML(9),
		PORTUGUESE_YML(9),
		VIETNAMESE_YML(3),
		DATABASE(4);
		private final Integer value;
		VersionType(final Integer value){
			this.value = value;
		}
		public final Integer value() {
			return value;
		}
	}
	public enum ProjectType {
		BUKKIT_DEV,
		SPIGOT_MC
	}
}
