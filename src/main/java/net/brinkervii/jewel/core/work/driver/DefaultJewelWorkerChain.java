package net.brinkervii.jewel.core.work.driver;

import net.brinkervii.jewel.core.JewelContext;
import net.brinkervii.jewel.core.work.workers.OutputDirectoryStructureWorker;
import net.brinkervii.jewel.core.work.workers.css.CssAccumulatorWorker;
import net.brinkervii.jewel.core.work.workers.html.HTMLAccumulatorWorker;
import net.brinkervii.jewel.core.work.workers.html.HTMLTemplateProcessorWorker;
import net.brinkervii.jewel.core.work.workers.html.HTMLWriterWorker;
import net.brinkervii.jewel.core.work.workers.html.SiteLayoutSelectorWorker;
import net.brinkervii.jewel.core.work.workers.md.MarkdownAccumulatorWorker;
import net.brinkervii.jewel.core.work.workers.md.MarkdownRendererWorker;
import net.brinkervii.jewel.core.work.workers.sass.SassCompilerWorker;

import java.util.ArrayList;

public final class DefaultJewelWorkerChain extends JewelWorkerChain {
	private ArrayList<JewelWorker> workers = new ArrayList<>();

	public DefaultJewelWorkerChain(JewelContext context) {
		super(context);

		add(new SassCompilerWorker(this));
		add(new CssAccumulatorWorker(this));
		add(new HTMLAccumulatorWorker(this));
		add(new MarkdownAccumulatorWorker(this));
		add(new OutputDirectoryStructureWorker(this));
		add(new MarkdownRendererWorker(this));
		add(new SiteLayoutSelectorWorker(this));
		add(new HTMLTemplateProcessorWorker(this));
		add(new HTMLWriterWorker(this));
	}

	@Override
	public JewelWorkerChain add(JewelWorker worker) {
		workers.add(worker);
		return this;
	}

	@Override
	public void work() {
		super.work();
		for (JewelWorker worker : workers) {
			worker.run();
		}
	}
}
