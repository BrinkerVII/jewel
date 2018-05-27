package net.brinkervii.jewel.core.work.workers.html;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.common.BucketOfShame;
import net.brinkervii.jewel.core.document.HTMLDocument;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;
import net.brinkervii.jewel.util.FileUtil;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Slf4j
public final class HTMLWriterWorker extends JewelWorker {
	public HTMLWriterWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		log.info("Running HTML Writer");

		for (HTMLDocument htmlDocument : chain.getContext().getHtmlDocuments()) {
			if (htmlDocument.shouldWrite()) {
				log.info(String.format("Writing HTML file %s", htmlDocument.getName()));

				final Path outputPath = FileUtil.relayPath(
					htmlDocument.getOrigin(),
					chain.getContext().getOutputDirectory(),
					htmlDocument.getSourceFile()
				);

				final File outfile = new File(outputPath.toString());
				if (!FileUtil.makeParents(outfile)) {
					log.error("Tried to make directories, but its all broken from here");
				}


				try (FileOutputStream outputStream = new FileOutputStream(outfile)) {
					IOUtils.write(htmlDocument.getContentString(), outputStream, StandardCharsets.UTF_8);
				} catch (IOException e) {
					BucketOfShame.accept(e);
				}
			}
		}
	}
}
