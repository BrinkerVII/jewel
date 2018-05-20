package net.brinkervii.jewel.core.work.workers.html;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.document.HTMLDocument;
import net.brinkervii.jewel.core.exception.NotAComponentException;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;
import org.apache.commons.io.IOUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public final class HTMLWriterWorker extends JewelWorker {
	private List<HTMLDocument> components = new LinkedList<>();

	public HTMLWriterWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		log.info("Running HTML Writer");

		this.components = chain.getContext().getComponents();

		for (HTMLDocument htmlDocument : chain.getContext().getHtmlDocuments()) {
			if (htmlDocument.shouldWrite()) {
				log.info(String.format("Writing HTML file %s", htmlDocument.getName()));

				final Document soup = htmlDocument.getSoup();
				boolean changedSoup = false;

				// Merge components with main document
				for (Element element : soup.getAllElements()) {
					HTMLDocument component = findComponent(element.nodeName());
					if (component == null) continue;

					Element parent = element.parent();
					element.remove();

					for (Element componentChild : component.getSoup().body().children()) {
						parent.appendChild(componentChild);
					}
					changedSoup = true;
				}

				// Only update the content string if the DOM has changed
				if (changedSoup) {
					htmlDocument.soupToContentString();
				}

				final Path originPath = Paths.get(htmlDocument.getOrigin().getAbsolutePath());
				final Path sourceFilePath = Paths.get(htmlDocument.getSourceFile().getAbsolutePath());
				final Path relativePath = originPath.relativize(sourceFilePath);
				final Path outputPath = Paths.get(chain.getContext().getOutputDirectory().getAbsolutePath().toString(), relativePath.toString());

				final File outfile = new File(outputPath.toString());
				if (!outfile.exists()) {
					final File parentFile = outfile.getParentFile();
					if (!parentFile.exists()) {
						if (!parentFile.mkdirs()) {
							log.error("Tried to make directories, but its all broken from here");
						}
					}
				}

				try (FileOutputStream outputStream = new FileOutputStream(outfile)) {
					IOUtils.write(htmlDocument.getContentString(), outputStream, StandardCharsets.UTF_8);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
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
