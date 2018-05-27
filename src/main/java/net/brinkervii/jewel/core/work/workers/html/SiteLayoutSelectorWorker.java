package net.brinkervii.jewel.core.work.workers.html;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.document.HTMLDocument;
import net.brinkervii.jewel.core.frontmatter.FrontMatter;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;

@Slf4j
public final class SiteLayoutSelectorWorker extends JewelWorker {
	public SiteLayoutSelectorWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		for (HTMLDocument document : chain.getContext().getHtmlDocuments()) {
			final FrontMatter frontMatter = document.getFrontMatter();
			if (frontMatter.containsKey("layout") && String.valueOf(frontMatter.get("layout")).equals("site-layout")) {
				chain.getContext().setSiteLayout(document);
			}
		}

		log.info("Site layout is now " + chain.getContext().getSiteLayout().getName());
	}
}
