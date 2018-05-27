package net.brinkervii.jewel.core;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;
import net.brinkervii.common.BucketOfShame;
import net.brinkervii.jewel.core.config.JewelConfiguration;
import net.brinkervii.jewel.core.document.HTMLDocument;
import net.brinkervii.jewel.core.document.MarkdownDocument;
import net.brinkervii.jewel.core.document.Stylesheet;
import net.brinkervii.jewel.core.exception.NoChainConstructorException;
import net.brinkervii.jewel.core.exception.NoJewelChainException;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

@Data
public class JewelContext {
	private ArrayList<Stylesheet> stylesheets;
	private ArrayList<HTMLDocument> htmlDocuments;
	private ArrayList<MarkdownDocument> markdownDocuments;
	private File outputDirectory = null;
	private JewelWorkerChain currentChain;
	private ObjectMapper objectMapper = new ObjectMapper();
	private JewelConfiguration config = null;
	private HTMLDocument siteLayout;
	private JewelChainConstructor chainConstructor = null;

	public JewelContext() {
		this.reset();
	}

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
					BucketOfShame.accept(e);
				}
			} else {
				try {
					objectMapper.writeValue(configurationFile, this.config);
				} catch (IOException e) {
					BucketOfShame.accept(e);
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

	public void replaceHTMLDocument(HTMLDocument original, HTMLDocument newDocument) {
		List<HTMLDocument> rubbish = new LinkedList<>();
		htmlDocuments.forEach(d -> {
			if (d.equals(original)) rubbish.add(d);
		});
		htmlDocuments.removeAll(rubbish);

		htmlDocument(newDocument);
	}

	private void reset() {
		stylesheets = new ArrayList<>();
		htmlDocuments = new ArrayList<>();
		markdownDocuments = new ArrayList<>();
		siteLayout = null;
	}

	public void regenerate() throws NoJewelChainException, NoChainConstructorException {
		if (this.chainConstructor == null) throw new NoChainConstructorException();

		this.currentChain = this.chainConstructor.construct();
		if (this.currentChain == null) throw new NoJewelChainException();

		this.reset();
		this.currentChain.work();
	}

	@Override
	public JewelContext clone() {
		JewelContext clone = new JewelContext();

		for (Stylesheet stylesheet : stylesheets) {
			clone.stylesheets.add(stylesheet.clone());
		}
		for(HTMLDocument document : htmlDocuments) {
			clone.htmlDocuments.add(document.clone());
		}

		return clone;
	}
}
