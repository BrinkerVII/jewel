package net.brinkervii.jewel.core.work.driver;

import net.brinkervii.jewel.core.JewelContext;
import net.brinkervii.jewel.core.work.workers.SassCompilerWorker;

import java.util.ArrayList;

public final class DefaultJewelWorkerChain extends JewelWorkerChain {
	private ArrayList<JewelWorker> workers = new ArrayList<>();

	public DefaultJewelWorkerChain(JewelContext context) {
		super(context);
		add(new SassCompilerWorker(this));
	}

	@Override
	public JewelWorkerChain add(JewelWorker worker) {
		workers.add(worker);
		return this;
	}

	@Override
	public void work() {
		for (JewelWorker worker : workers) {
			worker.run();
		}
	}
}
