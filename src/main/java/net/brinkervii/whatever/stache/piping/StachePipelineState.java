package net.brinkervii.whatever.stache.piping;

import lombok.Getter;
import net.brinkervii.jewel.core.pipeline.PipelineState;
import net.brinkervii.whatever.core.TemplateScope;
import net.brinkervii.whatever.core.TemplateProcessor;
import org.jsoup.nodes.Element;

public class StachePipelineState extends PipelineState<Element> {
	@Getter
	private final TemplateProcessor templateProcessor;
	@Getter
	private final TemplateScope templateScope;

	public StachePipelineState(TemplateProcessor templateProcessor, TemplateScope templateScope) {
		super();
		this.templateProcessor = templateProcessor;
		this.templateScope = templateScope;
	}
}
