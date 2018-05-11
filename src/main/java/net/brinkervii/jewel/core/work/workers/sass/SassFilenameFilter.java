package net.brinkervii.jewel.core.work.workers.sass;

import net.brinkervii.jewel.core.RegexFilenameFilter;

public final class SassFilenameFilter extends RegexFilenameFilter {
	public SassFilenameFilter() {
		super("^[^_].+\\.scss$");
	}
}
