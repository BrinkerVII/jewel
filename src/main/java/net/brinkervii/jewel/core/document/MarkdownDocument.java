package net.brinkervii.jewel.core.document;

import net.brinkervii.jewel.util.FileUtil;
import org.apache.commons.io.IOUtils;
import org.commonmark.node.Node;
import org.commonmark.parser.Parser;
import org.commonmark.renderer.html.HtmlRenderer;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class MarkdownDocument extends DocumentWithFrontMatter {
	private Document soup;

	private MarkdownDocument() {

	}

	public String render() {
		Parser parser = Parser.builder().build();
		Node document = parser.parse(this.contentString);
		HtmlRenderer renderer = HtmlRenderer.builder().build();
		return renderer.render(document);
	}

	public Document getSoup() {
		if (this.soup == null) {
			this.soup = Jsoup.parse(render());
		}

		return this.soup;
	}

	public static MarkdownDocument fromFile(File origin, File file) {
		MarkdownDocument document = new MarkdownDocument();
		document.origin = origin;

		try (FileInputStream inputStream = new FileInputStream(file)) {
			document.setContentString(IOUtils.toString(inputStream, StandardCharsets.UTF_8));
			document.sourceFile = FileUtil.changeExtension(file, "html");
			document.alwaysWrite = true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

		return document;
	}
}
