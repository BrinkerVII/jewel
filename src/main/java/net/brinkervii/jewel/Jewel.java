package net.brinkervii.jewel;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.JewelContext;
import net.brinkervii.jewel.core.work.driver.DefaultJewelWorkerChain;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;

@Slf4j
public class Jewel implements Runnable {
	public static void main(String[] arguments) {
		Jewel application = new Jewel();

		Thread main = new Thread(application);
		main.start();
	}

	private Jewel() {

	}

	@Override
	public void run() {
		log.info("Starting Jewel...");

		JewelContext context = new JewelContext();
		JewelWorkerChain workerChain = new DefaultJewelWorkerChain(context);
		workerChain.work();

		int bp = 0;
	}
}
