package net.brinkervii.jewel.core.work.driver;

public abstract class JewelWorker implements Runnable {
	protected JewelWorkerChain chain;
	public JewelWorker(JewelWorkerChain chain) {
		this.chain = chain;
	}

	@Override
	public void run() {

	}
}
