package net.brinkervii.jewel.core.document;

import net.brinkervii.jewel.core.exception.NotATemplateException;
import org.apache.commons.io.IOUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class HTMLDocument extends DocumentWithFrontMatter {
	private Document soup;

	private HTMLDocument() {

	}

	public Document getSoup(boolean forceRegenerate) {
		if (soup != null && !forceRegenerate) {
			return soup;
		}

		this.soup = Jsoup.parse(contentString);
		return soup;
	}

	public boolean isTemplate() {
		return frontMatter.containsKey("selector");
	}

	public String getSelector() throws NotATemplateException {
		if (!frontMatter.containsKey("selector")) {
			throw new NotATemplateException(this);
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
}
