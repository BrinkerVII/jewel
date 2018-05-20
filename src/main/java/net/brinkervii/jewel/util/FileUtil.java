package net.brinkervii.jewel.util;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileUtil {
	public static File changeExtension(File file, String extension) {
		final String newPath = file.getPath().replaceAll("\\..+$", "." + extension);
		return new File(newPath);
	}

	public static Path relativePath(File origin, File file) {
		final Path originPath = Paths.get(origin.getAbsolutePath());
		final Path sourceFilePath = Paths.get(file.getAbsolutePath());

		return originPath.relativize(sourceFilePath);
	}

	public static Path relayPath(File origin, File target, File file) {
		final Path relativePath = FileUtil.relativePath(origin, file);
		return Paths.get(target.getAbsolutePath().toString(), relativePath.toString());
	}

	public static boolean makeParents(File file) {
		if (file.exists()) return true;

		File parent = file.getParentFile();
		if (!parent.exists()) {
			return parent.mkdirs();
		}
		return false;
	}
}
