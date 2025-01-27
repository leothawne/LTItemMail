package br.net.gmj.nobookie.LTItemMail.module;

public final class DataModule {
	private DataModule() {}
	private static final String DATE = "https://jenkins.gmj.net.br/job/LTItemMail/$build/buildTimestamp?format=dd/MM/yyyy%20HH:mm:ss%20z";
	private static final String UPDATE = "https://jenkins.gmj.net.br/job/LTItemMail/lastSuccessfulBuild/buildNumber";
	private static final String PLUGIN = "https://request.gmj.net.br/LTItemMail/$version/manifest.yml";
	private static final String RESOURCE = "https://jenkins.gmj.net.br/job/LTItemMail-ResourcePack/lastSuccessfulBuild/artifact/LTItemMail-ResourcePack.zip";
	private static final Integer STABLE = 61;
	public static final String getDateURL(final Integer build) {
		return DATE.replace("$build", String.valueOf(build));
	}
	public static final String getUpdateURL() {
		return UPDATE;
	}
	public static final String getManifestURL(final String version) {
		return PLUGIN.replace("$version", version);
	}
	public static final String getResourcePackURL() {
		return RESOURCE;
	}
	public static final Integer getLatestStable() {
		return STABLE;
	}
	public enum Version {
		CONFIG_YML(12),
		ITEM_MODELS_YML(1),
		ENGLISH_YML(10),
		PORTUGUESE_YML(10),
		VIETNAMESE_YML(3),
		DATABASE(4);
		private final Integer value;
		Version(final Integer value){
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
