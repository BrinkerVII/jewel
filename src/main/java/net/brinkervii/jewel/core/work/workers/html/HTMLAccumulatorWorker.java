package net.brinkervii.jewel.core.work.workers.html;

import net.brinkervii.jewel.core.FileAccumulator;
import net.brinkervii.jewel.core.document.HTMLDocument;
import net.brinkervii.jewel.core.exception.NotADirectoryException;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public final  class HTMLAccumulatorWorker extends JewelWorker {
	public HTMLAccumulatorWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		FileAccumulator accumulator = new FileAccumulator("theme");
		try {
			for (File file : accumulator.accumulate(new HTMLFilenameFilter()).getFiles()) {
				chain.getContext().htmlDocument(HTMLDocument.fromFile(file));
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (NotADirectoryException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
