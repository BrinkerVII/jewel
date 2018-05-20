package net.brinkervii.jewel.core.work.workers.html;

import net.brinkervii.jewel.core.document.HTMLDocument;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;
import net.brinkervii.whatever.core.TemplateProcessor;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

public final class HTMLTemplateProcessorWorker extends JewelWorker {
	public HTMLTemplateProcessorWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		Map<HTMLDocument, HTMLDocument> replace = new HashMap<>();

		for (HTMLDocument document : chain.getContext().getHtmlDocuments()) {
			TemplateProcessor templateProcessor = new TemplateProcessor();
			if (document.getFrontMatter().containsKey("title")) {
				int bp = 999;
			}

			HashMap<String, Object> provider = new HashMap<>();
			provider.put("page", document.getFrontMatter());
			templateProcessor.provide(provider);

			final Document finalDocument = templateProcessor.process(document.getSoup());
			HTMLDocument processedDocument = HTMLDocument.fromString(document.getOrigin(), document.getSourceFile(), finalDocument.toString(), document);
			if (document.shouldWrite()) processedDocument.alwaysWrite();

			replace.put(document, processedDocument);
		}

		replace.forEach(chain.getContext()::replaceHTMLDocument);
	}
}
