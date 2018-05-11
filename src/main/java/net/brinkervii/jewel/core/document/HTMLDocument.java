package net.brinkervii.jewel.core.document;

import net.brinkervii.jewel.core.exception.NotAComponentException;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

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

	public String getSelector() throws NotAComponentException {
		if (!frontMatter.containsKey("selector")) {
			throw new NotAComponentException(this);
		}

		return frontMatter.get("selector");
	}

	public static HTMLDocument fromFile(File file) throws IOException {
		HTMLDocument htmlDocument = new HTMLDocument();

		try (FileInputStream inputStream = new FileInputStream(file)) {
			htmlDocument.sourceFile = file;
			htmlDocument.setContentString(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
		} catch (IOException e) {
			throw e;
		}

		return htmlDocument;
	}

	public void soupToContentString() {
		StringBuilder stringBuilder = new StringBuilder();
		for (Element element : soup.body().children()) {
			stringBuilder.append(element.outerHtml());
		}

		this.contentString = stringBuilder.toString();
	}
}
