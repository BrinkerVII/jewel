package net.brinkervii.jewel.core.frontmatter;

import java.util.Properties;

public class FrontMatter extends Properties {
	public FrontMatter() {
	}

	public void load(String frontMatterString) {
		String withoutCarriageReturns = frontMatterString.replaceAll("\r", "");
		for (String line : withoutCarriageReturns.split("\n")) {
			int colonPosition = 0;
			final char[] chars = line.toCharArray();

			for (int i = 0; i < chars.length; i++) {
				if (chars[i] == ':') {
					colonPosition = i;
					break;
				}
			}

			String key = line.substring(0, colonPosition);
			String value = line.substring(colonPosition + 1);

			put(key.trim().toLowerCase(), value.trim());
		}
	}

	public String get(String key) {
		return String.valueOf(get(key));
	}

	public String get(String key, String defaultValue) {
		if (containsKey(key)) return get(key);
		else return defaultValue;
	}

	public String[] getStringArray(String key) {
		if (contains(key)) {
			return get(key).split(" ");
		}

		return new String[]{};
	}
}
