package net.brinkervii.jewel.core.work.workers.md;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.FileAccumulator;
import net.brinkervii.jewel.core.config.JewelConfiguration;
import net.brinkervii.jewel.core.document.MarkdownDocument;
import net.brinkervii.jewel.core.exception.NotADirectoryException;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;

import java.io.File;
import java.io.FileNotFoundException;

@Slf4j
public final class MarkdownAccumulatorWorker extends JewelWorker {
	public MarkdownAccumulatorWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		log.info("Running markdown accumulator");

		final JewelConfiguration config = chain.getContext().config();
		FileAccumulator accumulator = new FileAccumulator(config.getThemeLocation(), config.getSourceLocation());
		try {
			accumulator.accumulate(new MarkdownFilenameFilter());

			accumulator.getFiles().forEach((root, files) -> {
				for (File file : files) {
					handleFile(root, file);
				}
			});
		} catch (FileNotFoundException | NotADirectoryException e) {
			e.printStackTrace();
		}
	}

	private void handleFile(File origin, File file) {
		log.info("Handling file " + file.getName());
		final MarkdownDocument document = MarkdownDocument.fromFile(origin, file);
		chain.getContext().markdown(document);
	}
}
