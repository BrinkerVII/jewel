package net.brinkervii.jewel.core.exception;

import net.brinkervii.jewel.core.document.HTMLDocument;

import java.io.File;

public class NotATemplateException extends Throwable {
	private final HTMLDocument document;

	public NotATemplateException(HTMLDocument document) {
		this.document = document;
	}

	@Override
	public String toString() {
		String documentName = document.getName();
		final File sourceFile = document.getSourceFile();
		if (sourceFile != null) {
			documentName = sourceFile.getName();
		}

		return String.format("Document '%s' is not an HTML template. Are you missing the selector property in the frontmatter?", documentName);
	}
}
