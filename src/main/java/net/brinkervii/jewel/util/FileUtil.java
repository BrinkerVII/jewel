package net.brinkervii.jewel.util;

import java.io.File;

public class FileUtil {
	public static File changeExtension(File file, String extension) {
		final String newPath = file.getPath().replaceAll("\\..+$", "." + extension);
		return new File(newPath);
	}
}
