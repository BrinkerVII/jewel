package net.brinkervii.jewel.core.work.workers.css;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.document.Stylesheet;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;
import net.brinkervii.jewel.util.FileUtil;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

@Slf4j
public final class CssWriterWorker extends JewelWorker {
	public CssWriterWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		log.info("Running CSS writer");

		for (Stylesheet stylesheet : chain.getContext().getStylesheets()) {
			final Path outputPath = FileUtil.relayPath(
				stylesheet.getOrigin(),
				chain.getContext().getOutputDirectory(),
				stylesheet.getSourceFile()
			);

			File outfile = new File(outputPath.toString());
			if (!FileUtil.makeParents(outfile)) {
				log.error("What");
			}

			log.info(String.format("Attempting to write to %s", outfile.getName()));

			try (FileOutputStream outputStream = new FileOutputStream(outfile)) {
				IOUtils.write(stylesheet.getContent(), outputStream, StandardCharsets.UTF_8);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
