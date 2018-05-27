package net.brinkervii.jewel.core.work.workers.md;

import net.brinkervii.jewel.core.document.HTMLDocument;
import net.brinkervii.jewel.core.document.MarkdownDocument;
import net.brinkervii.jewel.core.frontmatter.FrontMatter;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;
import net.brinkervii.jewel.util.FileUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

public final class MarkdownRendererWorker extends JewelWorker {
	public MarkdownRendererWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		for (MarkdownDocument document : chain.getContext().getMarkdownDocuments()) {
			final FrontMatter frontMatter = document.getFrontMatter();

			Document resultDOM = null;
			if (frontMatter.containsKey("layout")) {
				final HTMLDocument layout = findLayout(frontMatter.get("layout"));

				if (layout != null) {
					resultDOM = layout.getSoup().clone();
				}

			}

			if (resultDOM == null) {
				resultDOM = Jsoup.parse("<jewel-content></jewel-content>");
			}

			for (Element contentTag : resultDOM.getElementsByTag("jewel-content")) {
				Element parent = contentTag.parent();

				for (Element documentElement : document.getSoup().body().children()) {
					parent.appendChild(documentElement.clone());
				}

				contentTag.remove();
			}

			HTMLDocument render = HTMLDocument.fromString(
				document.getOrigin(),
				FileUtil.changeExtension(document.getSourceFile(), "html"),
				resultDOM.outerHtml(), document
			);
			render.alwaysWrite();

			chain.getContext().htmlDocument(render);
		}
	}

	private HTMLDocument findLayout(String layout) {
		for (HTMLDocument htmlDocument : chain.getContext().getHtmlDocuments()) {
			if (htmlDocument.isLayout() && htmlDocument.getFrontMatter().get("layout").equals(layout)) {
				return htmlDocument;
			}
		}

		return null;
	}
}
