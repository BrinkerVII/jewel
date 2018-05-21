package net.brinkervii.jewel.core.work.workers.html;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.document.HTMLDocument;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;
import net.brinkervii.whatever.core.TemplateProcessor;
import org.jsoup.nodes.Document;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public final class HTMLTemplateProcessorWorker extends JewelWorker {
	public HTMLTemplateProcessorWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		log.info("Running template processor");

		Map<HTMLDocument, HTMLDocument> replace = new HashMap<>();

		for (HTMLDocument document : chain.getContext().getHtmlDocuments()) {
			log.info(String.format("Processing templating in %s", document.getName()));

			TemplateProcessor templateProcessor = new TemplateProcessor();

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
