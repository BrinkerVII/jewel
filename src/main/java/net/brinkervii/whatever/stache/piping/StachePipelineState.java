package net.brinkervii.whatever.stache.piping;

import lombok.Getter;
import net.brinkervii.jewel.core.pipeline.PipelineState;
import net.brinkervii.whatever.core.TemplateContext;
import net.brinkervii.whatever.core.TemplateProcessor;
import org.jsoup.nodes.Element;

public class StachePipelineState extends PipelineState<Element> {
	@Getter
	private final TemplateProcessor templateProcessor;
	@Getter
	private final TemplateContext templateContext;

	public StachePipelineState(TemplateProcessor templateProcessor, TemplateContext templateContext) {
		super();
		this.templateProcessor = templateProcessor;
		this.templateContext = templateContext;
	}
}
