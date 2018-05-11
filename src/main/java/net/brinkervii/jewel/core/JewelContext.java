package net.brinkervii.jewel.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import net.brinkervii.jewel.core.config.JewelConfiguration;
import net.brinkervii.jewel.core.document.HTMLDocument;
import net.brinkervii.jewel.core.document.MarkdownDocument;
import net.brinkervii.jewel.core.document.Stylesheet;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
public class JewelContext {
	private ArrayList<Stylesheet> stylesheets = new ArrayList<>();
	private ArrayList<HTMLDocument> htmlDocuments = new ArrayList<>();
	private ArrayList<MarkdownDocument> markdownDocuments = new ArrayList<>();
	private File outputDirectory = null;
	private JewelWorkerChain activeChain;
	private ObjectMapper objectMapper = new ObjectMapper();
	private JewelConfiguration config = null;

	public void stylesheet(Stylesheet stylesheet) {
		stylesheets.add(stylesheet);
	}

	public void htmlDocument(HTMLDocument document) {
		LinkedList<HTMLDocument> rubbish = new LinkedList<>();
		for (HTMLDocument htmlDocument : htmlDocuments) {
			if (htmlDocument.getSourceFile().toString().equals(document.getSourceFile().toString())) {
				rubbish.add(htmlDocument);
			}
		}

		htmlDocuments.removeAll(rubbish);
		htmlDocuments.add(document);
	}

	public void markdown(MarkdownDocument document) {
		LinkedList<MarkdownDocument> rubbish = new LinkedList<>();
		for (MarkdownDocument markdownDocument : markdownDocuments) {
			if (markdownDocument.getSourceFile().toString().equals(document.getSourceFile().toString())) {
				rubbish.add(markdownDocument);
			}
		}

		markdownDocuments.removeAll(rubbish);
		markdownDocuments.add(document);
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

	public JewelConfiguration config() {
		if (this.config == null) {
			this.config = new JewelConfiguration();

			File configurationFile = new File("jewel_config.json");
			if (configurationFile.exists()) {
				try {
					this.config = objectMapper.readValue(configurationFile, JewelConfiguration.class);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				try {
					objectMapper.writeValue(configurationFile, this.config);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return this.config;
	}

	public File getOutputDirectory() {
		if (this.outputDirectory == null) {
			this.outputDirectory = new File(config().getSiteLocation());
		}

		return this.outputDirectory;
	}
}
