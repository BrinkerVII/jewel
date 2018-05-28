package net.brinkervii.common;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class INIReader extends LinkedHashSet<INIReader.Section> {
	private Section activeSection = null;

	public void load(File file) throws IOException {
		try (FileInputStream inputStream = new FileInputStream(file)) {
			loadString(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
		}
	}

	public void loadString(String source) {
		for (String line : source.replace("\r", "").split("\n")) {
			parseLine(line);
		}
	}

	private void parseLine(String line) {
		final char[] chars = line.toCharArray();
		StringBuilder collector = new StringBuilder();

		int sectionStart = -1;
		for (int i = 0; i < chars.length; i++) {
			switch (chars[i]) {
				case '[':
					sectionStart = i;
					continue;
				case ']':
					section(line, sectionStart + 1, i);
					sectionStart = -1;
					continue;
				default:
					collector.append(chars[i]);
					break;
			}
		}

		pair(collector.toString());
	}

	private void pair(String line) {
		int index = line.indexOf('=');
		if (index < 0) return;
		if (activeSection == null) return;

		String key = line.substring(0, index - 1).trim();
		String value = line.substring(index + 1).trim();

		activeSection.put(key, value);
	}

	private void section(String line, int start, int end) {
		String parsedName = line.substring(start, end);

		if (activeSection == null) {
			forEach(section -> {
				if (section.name.equals(parsedName)) activeSection = section;
			});
		} else {
			if (!activeSection.name.equals(parsedName)) {
				activeSection = null;
			}
		}

		if (activeSection == null) {
			activeSection = new Section(parsedName);
			add(activeSection);
		}
	}

	private boolean hasSectionWithKey(String section, String key) {
		if (!containsSection(section)) return false;

		Section haystack = getSection(section);
		return haystack.containsKey(key);
	}

	public String get(String section, String key, String defaultValue) {
		if (!hasSectionWithKey(section, key)) return defaultValue;

		return getSection(section).get(key);
	}

	public int getInt(String section, String key, int defaultValue) {
		if (!hasSectionWithKey(section, key)) return defaultValue;

		return Integer.parseInt(getSection(section).get(key));
	}

	private Section getSection(String needle) {
		AtomicReference<Section> result = new AtomicReference<>();

		forEach(section -> {
			if (result.get() == null && section.name.equals(needle)) {
				result.set(section);
			}
		});

		return result.get();
	}

	public boolean containsSection(String needle) {
		AtomicBoolean b = new AtomicBoolean(false);

		forEach(section -> {
			if (section.name.equals(needle)) {
				b.set(true);
			}
		});

		return b.get();
	}

	public class Section extends HashMap<String, String> {
		private String name;

		public Section(String name) {
			this.name = name;
		}

		public String getName() {
			return name;
		}

		@Override
		public boolean equals(Object o) {
			if (o instanceof Section) {
				return ((Section) o).name.equals(name);
			}

			return super.equals(o);
		}
	}
}
