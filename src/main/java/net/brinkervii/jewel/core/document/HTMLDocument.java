package net.brinkervii.jewel.core.document;

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

	public static HTMLDocument fromFile(File file) throws IOException {
		HTMLDocument htmlDocument = new HTMLDocument();

		try (FileInputStream inputStream = new FileInputStream(file)) {
			htmlDocument.setContentString(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
		} catch (IOException e) {
			throw e;
		}

		return htmlDocument;
	}
}
