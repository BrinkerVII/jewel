package net.brinkervii.jewel.core.work.workers.css;

import net.brinkervii.jewel.core.FileAccumulator;
import net.brinkervii.jewel.core.Stylesheet;
import net.brinkervii.jewel.core.exception.NotADirectoryException;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public final class CssAccumulatorWorker extends JewelWorker {
	public CssAccumulatorWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		FileAccumulator accumulator = new FileAccumulator("theme/style");
		try {
			for (File file : accumulator.accumulate(new CssFilenameFilter()).getFiles()) {
				FileInputStream inputStream = null;
				try {
					inputStream = new FileInputStream(file);
					final String s = IOUtils.toString(inputStream, StandardCharsets.UTF_8);

					chain.getContext().stylesheet(Stylesheet.withContent(s));
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
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NotADirectoryException e) {
			e.printStackTrace();
		}
	}
}
