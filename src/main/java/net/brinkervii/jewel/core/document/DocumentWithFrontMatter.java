package net.brinkervii.jewel.core.document;

import net.brinkervii.jewel.core.frontmatter.FrontMatter;
import net.brinkervii.jewel.core.frontmatter.FrontMatterParser;

import java.io.File;

public class DocumentWithFrontMatter {
	protected FrontMatter frontMatter = new FrontMatter();
	protected String contentString = "";
	protected File sourceFile;

	public FrontMatter getFrontMatter() {
		return frontMatter;
	}

	protected void setContentString(String contentString) {
		FrontMatterParser frontMatterParser = new FrontMatterParser(contentString);
		frontMatterParser.parse();

		if (!frontMatterParser.isEmpty()) {
			frontMatter = frontMatterParser.getFrontMatter();
		}

		this.contentString = frontMatterParser.getDocumentContentString();
	}

	public File getSourceFile() {
		return sourceFile;
	}

	public String getContentString() {
		return contentString;
	}
}
