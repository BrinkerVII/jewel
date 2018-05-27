package net.brinkervii.jewel.core.work.workers;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;

import java.io.File;

@Slf4j
public final class OutputDirectoryStructureWorker extends JewelWorker {
	public OutputDirectoryStructureWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		log.info("Checking output directory structure...");

		final File outputDirectory = chain.getContext().getOutputDirectory();
		if (!outputDirectory.exists()) {
			log.info("Making output directory...");
			if (!outputDirectory.mkdirs()) {
				log.error("Could not create output directory!");
				return;
			}
		}
	}
}
