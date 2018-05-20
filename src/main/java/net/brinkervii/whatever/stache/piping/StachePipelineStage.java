package net.brinkervii.whatever.stache.piping;

import net.brinkervii.jewel.core.pipeline.PipelineStage;
import net.brinkervii.whatever.stache.ValuePipeSegment;
import org.jsoup.nodes.Element;

import java.util.HashMap;

public class StachePipelineStage extends PipelineStage<Element> {
	private static HashMap<String, StachePipelineStage> cache = new HashMap<>();
	protected StachePipelineState state;
	protected final String bit;

	protected StachePipelineStage(StachePipelineState state, String bit) {
		super(state);
		this.state = state;
		this.bit = bit;
	}

	protected void setState(StachePipelineState state) {
		this.state = state;
		super.state = state;
	}

	public static PipelineStage<Element> fromBit(StachePipelineState state, String bit) {
		if(cache.containsKey(bit)) {
			final StachePipelineStage stachePipelineStage = cache.get(bit);
			stachePipelineStage.setState(state);
			return stachePipelineStage;
		}

		cache.put(bit, new ValuePipeSegment(state, bit));
		return fromBit(state, bit);
	}
}
