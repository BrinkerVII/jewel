package net.brinkervii.jewel.core;

import net.brinkervii.jewel.core.exception.NotADirectoryException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;

public class FileAccumulator {
	private LinkedList<File> roots = new LinkedList<>();
	private HashMap<File, LinkedList<File>> files = new HashMap<>();

	public FileAccumulator(String root) {
		this(new File(root));
	}

	public FileAccumulator(String... roots) {
		for (String root : roots) {
			this.roots.add(new File(root));
		}
	}

	public FileAccumulator(File... roots) {
		this.roots.addAll(Arrays.asList(roots));
	}

	public FileAccumulator(File file) {
		roots.add(file);
	}

	public FileAccumulator(LinkedList<File> roots) {
		this.roots.addAll(roots);
	}

	private void accumulate(File directory, FilenameFilter filter, File root) throws NotADirectoryException, FileNotFoundException {
		if (!directory.exists()) throw new FileNotFoundException();
		if (!directory.isDirectory()) throw new NotADirectoryException();

		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				accumulate(file, filter, root);
			} else if (file.isFile() && filter.accept(file, file.getAbsoluteFile().getName())) {
				addFile(root, file);
			}
		}
	}

	private void addFile(File root, File file) {
		if (!files.containsKey(root)) {
			files.put(root, new LinkedList<>());
		}

		files.get(root).add(file);
	}

	public FileAccumulator accumulate(FilenameFilter filter) throws FileNotFoundException, NotADirectoryException {
		for (File root : roots) {
			accumulate(root, filter, root);
		}
		return this;
	}

	public HashMap<File, LinkedList<File>> getFiles() {
		return files;
	}

	public FileAccumulator duplicate() {
		return new FileAccumulator(roots);
	}
}
