package net.brinkervii.jewel.core.work.workers;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.common.BucketOfShame;
import net.brinkervii.jewel.core.work.driver.JewelWorker;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;

@Slf4j
public final class CopyAssetsWorker extends JewelWorker {
	public CopyAssetsWorker(JewelWorkerChain chain) {
		super(chain);
	}

	@Override
	public void run() {
		log.info("Copying assets");

		File sourceAssetsDirectory = new File(chain.getContext().config().getSourceLocation(), "assets");
		if (sourceAssetsDirectory.exists()) {
			File outputAssetsDirectory = new File(chain.getContext().getOutputDirectory(), "assets");
			if (!outputAssetsDirectory.exists()) {
				outputAssetsDirectory.mkdirs();
			}

			try {
				FileUtils.copyDirectory(sourceAssetsDirectory, outputAssetsDirectory);
			} catch (IOException e) {
				BucketOfShame.accept(e);
			}
		}
	}
}
