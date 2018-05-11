package net.brinkervii.jewel.core;

public class ExtensionFilenameFilter extends RegexFilenameFilter {
	public ExtensionFilenameFilter(String extension) {
		super(String.format(".*\\.%s$", extension));
	}

	public ExtensionFilenameFilter(String... extensions) {
		super(formatExtensionArray(extensions));
	}

	private static String[] formatExtensionArray(String[] extensions) {
		String[] formattedArray = new String[extensions.length];
		for (int i = 0; i < extensions.length; i++) {
			formattedArray[i] = String.format(".*\\.%s$", extensions[i]);
		}

		return formattedArray;
	}
}
