package net.brinkervii.jewel.core.document;

import net.brinkervii.jewel.core.frontmatter.FrontMatter;
import net.brinkervii.jewel.core.frontmatter.FrontMatterParser;

public class DocumentWithFrontMatter {
	protected FrontMatter frontMatter = new FrontMatter();
	protected String contentString = "";

	public FrontMatter getFrontMatter() {
		return frontMatter;
	}

	protected void setContentString(String contentString) {
		FrontMatterParser frontMatterParser = new FrontMatterParser(contentString);
		frontMatterParser.parse();

		if (!frontMatterParser.isEmpty()) {
			frontMatter = frontMatterParser.getFrontMatter();
		}

		contentString = frontMatterParser.getDocumentContentString();
	}
}
