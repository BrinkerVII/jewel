package net.brinkervii.jewel.core.work.workers.html;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.BucketOfShame;
import net.brinkervii.jewel.core.document.HTMLDocument;
import net.brinkervii.jewel.core.exception.NoTargetElementException;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedHashMap;

@Slf4j
public final class SiteLayoutWrapper extends JewelWorker {
	public SiteLayoutWrapper(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		log.info("Wrapping html documents in site layout");

		final HTMLDocument siteLayout = chain.getContext().getSiteLayout();
		if (siteLayout == null) {
			log.warn("No site layout present");
			return;
		}

		LinkedHashMap<HTMLDocument, HTMLDocument> changes = new LinkedHashMap<>();
		for (HTMLDocument document : chain.getContext().getHtmlDocuments()) {
			if (!document.equals(siteLayout)) {
				log.info("Wrapping " + document.getName());
				try {
					HTMLDocument newDocument = this.wrapIt(document, siteLayout);
					if (newDocument == null) {
						log.warn("Could not wrap document " + document.getName());
						continue;
					}
					changes.put(document, newDocument);
				} catch (NoTargetElementException e) {
					BucketOfShame.accept(e);
				}
			}
		}

		changes.forEach(chain.getContext()::replaceHTMLDocument);
	}

	private HTMLDocument wrapIt(HTMLDocument document, HTMLDocument siteLayout) throws NoTargetElementException {
		final Document result = Jsoup.parse(siteLayout.getSoup().toString());
		final Document soup = document.getSoup();

		Element targetElement = result.body().getElementsByTag("jewel-content").first();
		if (targetElement == null) throw new NoTargetElementException();
		Element jcontent = targetElement;
		targetElement = targetElement.parent();
		jcontent.remove();

		for (Element element : soup.body().children()) {
			targetElement.appendChild(element.clone());
		}

		final HTMLDocument wrappedDocument = HTMLDocument.fromString(document.getOrigin(), document.getSourceFile(), result.toString(), document);
		if (document.shouldWrite()) wrappedDocument.alwaysWrite();

		return wrappedDocument;
	}
}
