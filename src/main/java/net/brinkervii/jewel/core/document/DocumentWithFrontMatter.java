package net.brinkervii.jewel.core.document;

import net.brinkervii.jewel.core.frontmatter.FrontMatter;
import net.brinkervii.jewel.core.frontmatter.FrontMatterParser;

import java.io.File;

public class DocumentWithFrontMatter extends JewelDocument {
	protected FrontMatter frontMatter = new FrontMatter();
	protected String contentString = "";
	protected File sourceFile;
	protected DocumentWithFrontMatter previous;

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

	public String getName() {
		return "DOCUMENT_WITH_FRONTMATTER";
	}

	public DocumentWithFrontMatter getPrevious() {
		return previous;
	}
}
