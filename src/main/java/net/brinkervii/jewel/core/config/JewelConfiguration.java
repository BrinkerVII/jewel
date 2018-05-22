package net.brinkervii.jewel.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

@Data
@JsonSerialize
public class JewelConfiguration {
	@JsonProperty("theme_location")
	private String themeLocation = "theme";
	@JsonProperty("site_location")
	private String siteLocation = "www";
	@JsonProperty("source_location")
	private String sourceLocation = "src";
	@JsonProperty("site_properties")
	private SiteProperties siteProperties = new SiteProperties();

	public JewelConfiguration() {
	}
}
