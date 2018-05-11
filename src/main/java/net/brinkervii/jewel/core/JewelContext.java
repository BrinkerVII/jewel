package net.brinkervii.jewel.core;

import lombok.Data;
import net.brinkervii.jewel.core.document.HTMLDocument;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
public class JewelContext {
	private ArrayList<Stylesheet> stylesheets = new ArrayList<>();
	private ArrayList<HTMLDocument> htmlDocuments = new ArrayList<>();
	private File outputDirectory = new File("site");
	private JewelWorkerChain activeChain;

	public void stylesheet(Stylesheet stylesheet) {
		stylesheets.add(stylesheet);
	}

	public void htmlDocument(HTMLDocument document) {
		htmlDocuments.add(document);
	}

	public List<HTMLDocument> getComponents() {
		LinkedList<HTMLDocument> components = new LinkedList<>();

		for (HTMLDocument htmlDocument : htmlDocuments) {
			if (htmlDocument.isComponent()) {
				components.add(htmlDocument);
			}
		}

		return components;
	}
}
