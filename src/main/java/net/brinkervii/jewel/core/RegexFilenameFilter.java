package net.brinkervii.jewel.core;

import java.io.File;
import java.io.FilenameFilter;

public class RegexFilenameFilter implements FilenameFilter {
	protected final String pattern;

	public RegexFilenameFilter(String pattern) {
		this.pattern = pattern;
	}

	@Override
	public boolean accept(File file, String s) {
		return s.matches(pattern);
	}
}
