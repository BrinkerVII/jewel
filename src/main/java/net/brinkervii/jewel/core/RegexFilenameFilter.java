package net.brinkervii.jewel.core;

import java.io.File;
import java.io.FilenameFilter;

public class RegexFilenameFilter implements FilenameFilter {
	protected final String[] patterns;

	public RegexFilenameFilter(String pattern) {
		patterns = new String[]{pattern};
	}

	public RegexFilenameFilter(String[] patterns) {
		this.patterns = patterns;
	}

	@Override
	public boolean accept(File file, String s) {
		for (String pattern : patterns) {
			if (s.matches(pattern)) return true;
		}

		return false;
	}
}
