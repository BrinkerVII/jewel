package net.brinkervii.jewel.core;

public class ExtensionFilenameFilter extends RegexFilenameFilter {
	public ExtensionFilenameFilter(String extension) {
		super(String.format(".*\\.%s$", extension));
	}
}
