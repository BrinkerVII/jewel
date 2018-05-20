package net.brinkervii.whatever.stache;

import net.brinkervii.whatever.stache.piping.StachePipelineStage;
import net.brinkervii.whatever.stache.piping.StachePipelineState;
import org.jsoup.nodes.Element;

import java.util.Map;
import java.util.regex.Pattern;

public class ValuePipeSegment extends StachePipelineStage {
	public ValuePipeSegment(StachePipelineState state, String bit) {
		super(state, bit);
	}

	@Override
	public Element pump(Element work) {
		Map source = null;
		Object result = null;
		for (String accesor : bit.split("\\.")) {
			Object v;
			if (source == null) {
				v = state.getTemplateProcessor().getValue(accesor, null);
			} else {
				v = source.get(accesor);
			}

			if (v instanceof Map) {
				source = (Map) v;
			} else {
				result = v;
			}
		}

		if (result != null) {
			work.text(work.ownText().replaceAll(String.format("\\{\\{\\s*%s\\s*}}", bit), result.toString()));
		}

		return work;
	}
}
