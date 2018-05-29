package net.brinkervii.jewel.core.document;

import net.brinkervii.jewel.core.exception.NotAComponentException;
import net.brinkervii.jewel.core.frontmatter.FrontMatter;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashSet;

public class HTMLDocument extends DocumentWithFrontMatter {
	private Document soup;

	private HTMLDocument() {

	}

	public Document getSoup() {
		return getSoup(false);
	}

	public Document getSoup(boolean forceRegenerate) {
		if (soup != null && !forceRegenerate) {
			return soup;
		}

		this.soup = Jsoup.parse(contentString);
		return soup;
	}

	public boolean isComponent() {
		return frontMatter.containsKey("selector");
	}

	public boolean isLayout() {
		return frontMatter.containsKey("layout");
	}

	public boolean shouldWrite() {
		return (!isLayout() && !isComponent()) || alwaysWrite;
	}

	public String getSelector() throws NotAComponentException {
		if (!frontMatter.containsKey("selector")) {
			throw new NotAComponentException(this);
		}

		return frontMatter.get("selector");
	}

	public static HTMLDocument fromFile(File origin, File file) throws IOException {
		HTMLDocument htmlDocument = new HTMLDocument();
		htmlDocument.origin = origin;

		try (FileInputStream inputStream = new FileInputStream(file)) {
			htmlDocument.sourceFile = file;
			htmlDocument.setContentString(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
		} catch (IOException e) {
			throw e;
		}

		return htmlDocument;
	}

	public void copyHEADTags(Document targetDOM) {
		for (Element element : getSoup().head().children()) {
			element.clone().appendTo(targetDOM.head());
		}

		// Strip duplicate tags from the target DOM
		LinkedHashSet<String> tracker = new LinkedHashSet<>();
		for (Element element : targetDOM.head().children()) {
			String elementString = element.toString();
			if (tracker.contains(elementString)) element.remove();
			tracker.add(elementString);
		}
	}

	public void copyHEADTags(HTMLDocument targetDocument) {
		copyHEADTags(targetDocument.getSoup());
		targetDocument.soupToContentString();
	}

	public static HTMLDocument fromString(File origin, File sourceFile, String s) {
		HTMLDocument document = new HTMLDocument();
		document.origin = origin;
		document.sourceFile = sourceFile;
		document.setContentString(s);

		return document;
	}

	public static HTMLDocument fromString(File origin, File sourceFile, String s, DocumentWithFrontMatter previous) {
		HTMLDocument document = new HTMLDocument();
		document.origin = origin;
		document.sourceFile = sourceFile;
		document.previous = previous;
		document.setContentString(s);

		document.frontMatter = new FrontMatter(previous.frontMatter, document.frontMatter);

		return document;
	}

	public void soupToContentString() {
		this.contentString = soup.outerHtml();
	}

	@Override
	public HTMLDocument clone() {
		HTMLDocument clone = new HTMLDocument();

		clone.origin = origin;
		clone.sourceFile = sourceFile;
		clone.contentString = contentString;

		return clone;
	}
}
