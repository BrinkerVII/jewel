package net.brinkervii.jewel.core;

import net.brinkervii.jewel.core.exception.NotADirectoryException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;

public class FileAccumulator {
	private final File root;
	private ArrayList<File> files = new ArrayList<>();

	public FileAccumulator(String root) {
		this(new File(root));
	}

	public FileAccumulator(File file) {
		this.root = file;
	}

	private void accumulate(File directory, FilenameFilter filter) throws NotADirectoryException, FileNotFoundException {
		if (!directory.exists()) throw new FileNotFoundException();
		if (!directory.isDirectory()) throw new NotADirectoryException();

		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				accumulate(file, filter);
			} else if (file.isFile()) {
				if (filter.accept(file, file.getAbsoluteFile().getName())) {
					files.add(file);
				}
			}
		}
	}

	public FileAccumulator accumulate(FilenameFilter filter) throws FileNotFoundException, NotADirectoryException {
		accumulate(root, filter);
		return this;
	}

	public File[] getFiles() {
		File[] arrayFiles = new File[files.size()];
		for (int i = 0; i < files.size(); i++) {
			arrayFiles[i] = files.get(i);
		}

		return arrayFiles;
	}
}
