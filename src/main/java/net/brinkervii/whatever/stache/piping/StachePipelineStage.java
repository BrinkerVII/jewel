package net.brinkervii.whatever.stache.piping;

import net.brinkervii.jewel.core.pipeline.PipelineStage;
import net.brinkervii.whatever.stache.ValuePipeSegment;
import org.jsoup.nodes.Element;

import java.util.HashMap;

public class StachePipelineStage extends PipelineStage<Element> {
	private static HashMap<String, StachePipelineStage> cache = new HashMap<>();
	protected final StachePipelineState state;
	protected final String bit;

	protected StachePipelineStage(StachePipelineState state, String bit) {
		super(state);
		this.state = state;
		this.bit = bit;
	}

	public static PipelineStage<Element> fromBit(StachePipelineState state, String bit) {
		if(cache.containsKey(bit)) return cache.get(bit);

		cache.put(bit, new ValuePipeSegment(state, bit));
		return fromBit(state, bit);
	}
}
