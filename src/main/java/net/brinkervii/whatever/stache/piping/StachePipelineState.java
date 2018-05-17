package net.brinkervii.whatever.stache.piping;

import net.brinkervii.jewel.core.pipeline.PipelineState;
import net.brinkervii.whatever.core.TemplateProcessor;
import org.jsoup.nodes.Element;

public class StachePipelineState extends PipelineState<Element> {
	private final TemplateProcessor templateProcessor;

	public StachePipelineState(TemplateProcessor templateProcessor) {
		super();
		this.templateProcessor = templateProcessor;
	}

	public TemplateProcessor getTemplateProcessor() {
		return templateProcessor;
	}
}
