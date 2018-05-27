package net.brinkervii.jewel.core.work.workers.html;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.BucketOfShame;
import net.brinkervii.jewel.core.FileAccumulator;
import net.brinkervii.jewel.core.document.HTMLDocument;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;

import java.io.File;
import java.io.IOException;

@Slf4j
public final class HTMLAccumulatorWorker extends JewelWorker {
	public HTMLAccumulatorWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		log.info("Running HTML Accumulator");

		final File sourceDirectory = new File(chain.getContext().config().getSourceLocation());
		final File themeDirectory = new File(chain.getContext().config().getThemeLocation());

		FileAccumulator accumulator = new FileAccumulator(sourceDirectory, themeDirectory);
		handleFiles(accumulator);
	}

	private void handleFiles(FileAccumulator accumulator) {
		try {
			accumulator.accumulate(new HTMLFilenameFilter()).getFiles().forEach((origin, files) -> {
				for (File file : files) {
					try {
						chain.getContext().htmlDocument(HTMLDocument.fromFile(origin, file));
					} catch (IOException e) {
						BucketOfShame.accept(e);
					}

					log.info(String.format("Added HTML file %s", file.getName()));

				}
			});
		} catch (IOException e) {
			BucketOfShame.accept(e);
		}
	}
}
