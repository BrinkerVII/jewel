package net.brinkervii.jewel.core.work.workers.html;

import net.brinkervii.jewel.core.document.HTMLDocument;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;
import net.brinkervii.whatever.core.TemplateProcessor;
import org.jsoup.nodes.Document;

import java.util.HashMap;

public class HTMLTemplateProcessorWorker extends JewelWorker {
	public HTMLTemplateProcessorWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		for (HTMLDocument document : chain.getContext().getHtmlDocuments()) {
			TemplateProcessor templateProcessor = new TemplateProcessor();

			HashMap<String, Object> provider = new HashMap<>();
			provider.put("page", document.getFrontMatter());
			templateProcessor.provide(provider);

			final Document finalDocument = templateProcessor.process(document.getSoup());
		}
	}
}
