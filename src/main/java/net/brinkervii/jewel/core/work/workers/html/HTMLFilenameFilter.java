package net.brinkervii.jewel.core.work.workers.html;

import java.io.File;
import java.io.FilenameFilter;

public final class HTMLFilenameFilter implements FilenameFilter {
	private final static String FILENAME_PATTERN = ".*\\.html$";

	@Override
	public boolean accept(File file, String s) {
		return s.matches(FILENAME_PATTERN);
	}
}
