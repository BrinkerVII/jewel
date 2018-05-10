package net.brinkervii.jewel.core;

import lombok.Data;
import net.brinkervii.jewel.core.document.HTMLDocument;

import java.io.File;
import java.util.ArrayList;

@Data
public class JewelContext {
	ArrayList<Stylesheet> stylesheets = new ArrayList<>();
	ArrayList<HTMLDocument> htmlDocuments = new ArrayList<>();
	File outputDirectory = new File("site");

	public void stylesheet(Stylesheet stylesheet) {
		stylesheets.add(stylesheet);
	}

	public void htmlDocument(HTMLDocument document) {
		htmlDocuments.add(document);
	}
}
