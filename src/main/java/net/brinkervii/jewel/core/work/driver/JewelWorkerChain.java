package net.brinkervii.jewel.core.work.driver;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.JewelContext;

@Slf4j
public abstract class JewelWorkerChain {
	protected final JewelContext context;

	protected JewelWorkerChain(JewelContext context) {
		this.context = context;
	}

	public JewelWorkerChain add(JewelWorker worker) {
		return this;
	}

	public void work() {
		log.warn("A JewelWorkerChain is being ran without the work method being implemented...");
	}

	public JewelContext getContext() {
		return context;
	}
}
