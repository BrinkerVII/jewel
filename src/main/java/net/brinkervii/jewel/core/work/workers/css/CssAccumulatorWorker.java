package net.brinkervii.jewel.core.work.workers.css;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.FileAccumulator;
import net.brinkervii.jewel.core.config.JewelConfiguration;
import net.brinkervii.jewel.core.document.Stylesheet;
import net.brinkervii.jewel.core.exception.NotADirectoryException;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

@Slf4j
public final class CssAccumulatorWorker extends JewelWorker {
	public CssAccumulatorWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		log.info("Running CSS Accumulator");

		final JewelConfiguration config = chain.getContext().config();
		FileAccumulator accumulator = new FileAccumulator(
			config.getThemeLocation(),
			config.getSourceLocation()
		);

		try {
			accumulator.accumulate(new CssFilenameFilter()).getFiles().forEach((root, files) -> {
				for (File file : files) {
					FileInputStream inputStream = null;
					try {
						inputStream = new FileInputStream(file);
						final String s = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

						chain.getContext().stylesheet(Stylesheet.withContent(root, s));
						log.info(String.format("Added CSS file %s", file.getName()));
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						if (inputStream != null) {
							try {
								inputStream.close();
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					}
				}
			});
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NotADirectoryException e) {
			e.printStackTrace();
		}
	}
}
