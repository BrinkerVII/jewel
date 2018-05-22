package net.brinkervii.jewel.core.config;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

@Data
@JsonSerialize
public class SiteProperties {
	@JsonProperty
	private String title = "Untitled site";

	public Map<String, Object> map() {
		HashMap<String, Object> map = new HashMap<>();
		for (Field field : getClass().getDeclaredFields()) {
			try {
				map.put(field.getName(), field.get(this));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}

		return map;
	}
}
