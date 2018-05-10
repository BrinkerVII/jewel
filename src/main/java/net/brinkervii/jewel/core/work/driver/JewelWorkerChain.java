package net.brinkervii.jewel.core.work.driver;

import net.brinkervii.jewel.core.JewelContext;

public abstract class JewelWorkerChain {
	protected final JewelContext context;

	protected JewelWorkerChain(JewelContext context) {
		this.context = context;
	}

	public JewelWorkerChain add(JewelWorker worker) {
		return this;
	}

	public void work() {
	}

	public JewelContext getContext() {
		return context;
	}
}
