package net.brinkervii.jewel.core.work.workers.html;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.document.HTMLDocument;
import net.brinkervii.jewel.core.exception.NotAComponentException;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedList;
import java.util.List;

@Slf4j
public final class HTMLComponentInjectorWorker extends JewelWorker {
	private List<HTMLDocument> components = new LinkedList<>();

	public HTMLComponentInjectorWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		this.components = chain.getContext().getComponents();
		log.info("Running component injector");
		log.info(String.format("We have %d components in stock", components.size()));

		for (HTMLDocument document : chain.getContext().getHtmlDocuments()) {
			if(document.getName().equals("index.html")) {
				int asd = 0;
			}

			final Document soup = document.getSoup();
			boolean changedSoup = false;

			// Merge components with main document
			for (Element element : soup.getAllElements()) {
				HTMLDocument component = findComponent(element.nodeName());
				if (component == null) continue;
				log.info(String.format("Injecting component %s into document %s", component.getName(), document.getName()));

				Element parent = element.parent();
				element.remove();

				for (Element componentChild : component.getSoup().body().children()) {
					parent.appendChild(componentChild);
				}
				changedSoup = true;
			}

			// Only update the content string if the DOM has changed
			if (changedSoup) {
				document.soupToContentString();
			}
		}
	}


	private HTMLDocument findComponent(String name) {
		for (HTMLDocument component : components) {
			try {
				if (component.getSelector().equals(name)) {
					return component;
				}
			} catch (NotAComponentException e) {
				log.warn("Could not get selector: " + e.toString());
			}
		}

		return null;
	}
}
