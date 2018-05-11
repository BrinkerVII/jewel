package net.brinkervii.jewel.core.work.workers.sass;

import net.brinkervii.jewel.core.RegexFilenameFilter;

public final class SassPartialFilenameFilter extends RegexFilenameFilter {
	public SassPartialFilenameFilter() {
		super("^_.+\\.scss$");
	}
}
