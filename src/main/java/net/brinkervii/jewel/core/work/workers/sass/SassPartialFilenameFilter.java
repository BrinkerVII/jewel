package net.brinkervii.jewel.core.work.workers.sass;

import java.io.File;
import java.io.FilenameFilter;

public final class SassPartialFilenameFilter implements FilenameFilter {
	private final static String FILENAME_PATTERN = "^_.+\\.scss$";

	@Override
	public boolean accept(File file, String s) {
		return s.matches(FILENAME_PATTERN);
	}
}
