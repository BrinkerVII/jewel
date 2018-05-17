package net.brinkervii.whatever.stache.piping;

import net.brinkervii.jewel.core.pipeline.Pipeline;
import org.jsoup.nodes.Element;

import java.util.List;

public class StachePipeline extends Pipeline<Element> {
	protected final StachePipelineState state;

	public StachePipeline(List<String> bits, StachePipelineState state) {
		this.state = state;
		for (String bit : bits) {
			stage(StachePipelineStage.fromBit(this.state, bit));
		}
	}
}
