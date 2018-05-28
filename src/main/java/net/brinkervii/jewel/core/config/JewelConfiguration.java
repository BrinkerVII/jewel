package net.brinkervii.jewel.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.util.HashMap;

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
	@JsonProperty("sass_variables")
	private HashMap<String, String> sassVariables = new HashMap<>();

	public String compileSassVariables() {
		StringBuilder stringBuilder = new StringBuilder();
		sassVariables.forEach((key, value) -> {
			stringBuilder.append(String.format("$%s: %s;", key, value)).append('\n');
		});

		return stringBuilder.toString();
	}
}
