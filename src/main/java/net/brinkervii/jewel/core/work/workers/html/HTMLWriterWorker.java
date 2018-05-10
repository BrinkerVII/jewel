package net.brinkervii.jewel.core.work.workers.html;

import net.brinkervii.jewel.core.document.HTMLDocument;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class HTMLWriterWorker extends JewelWorker {
	public HTMLWriterWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		final File themeDirectory = new File("theme");
		final Path themePath = Paths.get(themeDirectory.getAbsolutePath());

		for (HTMLDocument htmlDocument : chain.getContext().getHtmlDocuments()) {
			if (!htmlDocument.isTemplate()) {
				final Path sourceFilePath = Paths.get(htmlDocument.getSourceFile().getAbsolutePath());
				final Path relativePath = themePath.relativize(sourceFilePath);
				final Path outputPath = Paths.get(chain.getContext().getOutputDirectory().getAbsolutePath().toString(), relativePath.toString());

				try (FileOutputStream outputStream = new FileOutputStream(new File(outputPath.toString()))) {
					IOUtils.write(htmlDocument.getContentString(), outputStream, StandardCharsets.UTF_8);
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
}
