package net.brinkervii.jewel.core.document;

import java.io.File;

public class JewelDocument {
	protected File origin;
	protected boolean alwaysWrite = false;
	protected File sourceFile;

	public File getOrigin() {
		return origin;
	}

	public void alwaysWrite() {
		this.alwaysWrite = true;
	}

	public String getName() {
		if (sourceFile == null) {
			return "NO_SOURCE_FILE";
		}

		return sourceFile.getName();
	}

	public File getSourceFile() {
		return sourceFile;
	}

	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}
}
