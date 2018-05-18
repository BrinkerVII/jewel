package net.brinkervii.whatever.stache;

import net.brinkervii.whatever.stache.piping.StachePipelineStage;
import net.brinkervii.whatever.stache.piping.StachePipelineState;
import org.jsoup.nodes.Element;

import java.util.Map;

public class ValuePipeSegment extends StachePipelineStage {
	public ValuePipeSegment(StachePipelineState state, String bit) {
		super(state, bit);
	}

	@Override
	public Element pump(Element work) {
		Map<String, Object> source = null;
		Object result = null;
		for (String accesor : bit.split("\\.")) {
			Object v;
			if (source == null) {
				v = state.getTemplateProcessor().getValue(accesor, null);

			} else {
				v = source.get(bit);
			}

			if (v instanceof Map) {
				source = (Map<String, Object>) v;
			} else {
				result = v;
			}
		}

		if (result != null) {
			work.text(result.toString());
		}

		return work;
	}
}