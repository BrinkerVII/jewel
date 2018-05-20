package net.brinkervii.jewel.core.work.driver;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class JewelWorker implements Runnable {
	protected JewelWorkerChain chain;

	public JewelWorker(JewelWorkerChain chain) {
		this.chain = chain;
	}

	@Override
	public abstract void run();
}
