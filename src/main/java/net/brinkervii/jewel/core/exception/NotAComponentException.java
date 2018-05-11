package net.brinkervii.jewel.core.exception;

import net.brinkervii.jewel.core.document.HTMLDocument;

import java.io.File;

public class NotAComponentException extends Throwable {
	private final HTMLDocument document;

	public NotAComponentException(HTMLDocument document) {
		this.document = document;
	}

	@Override
	public String toString() {
		String documentName = document.getName();
		final File sourceFile = document.getSourceFile();
		if (sourceFile != null) {
			documentName = sourceFile.getName();
		}

		return String.format("Document '%s' is not an HTML component. Are you missing the selector property in the frontmatter?", documentName);
	}
}
