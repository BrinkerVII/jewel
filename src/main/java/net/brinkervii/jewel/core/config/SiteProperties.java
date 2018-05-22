package net.brinkervii.jewel.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import net.brinkervii.jewel.core.ObjectMap;

import java.util.Map;

@Data
@JsonSerialize
public class SiteProperties {
	@JsonProperty
	private String title = "Untitled site";

	public Map<String, Object> map() {
		return new ObjectMap(this);
	}
}
