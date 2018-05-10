package net.brinkervii.jewel.core.work.workers;

import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;

import java.io.File;

public class OutputDirectoryStructureWorker extends JewelWorker {
	public OutputDirectoryStructureWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		final File outputDirectory = chain.getContext().getOutputDirectory();
		if (!outputDirectory.exists()) {
			if (!outputDirectory.mkdirs()) {
				// FAILED
				return;
			}
		}
	}
}
