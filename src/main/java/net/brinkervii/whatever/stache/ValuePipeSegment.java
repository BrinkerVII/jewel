package net.brinkervii.whatever.stache;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.common.BucketOfShame;
import net.brinkervii.whatever.stache.piping.StachePipelineStage;
import net.brinkervii.whatever.stache.piping.StachePipelineState;
import org.jsoup.nodes.Element;

import java.util.regex.PatternSyntaxException;

@Slf4j
public class ValuePipeSegment extends StachePipelineStage {
	public ValuePipeSegment(StachePipelineState state, String bit) {
		super(state, bit);
	}

	@Override
	public Element pump(Element work) {
		Object value = state.getTemplateScope().access(bit);

		if (value != null) {
			// String regex = String.format("\\{\\{\\s*%s\\s*}}", bit);
			String regex = "\\{\\{\\s*" + bit + "\\s*}}";

			try {
				work.text(work.ownText().replaceAll(regex, value.toString()));
			} catch (PatternSyntaxException e) {
				log.error("Failed to run regex " + regex);
				BucketOfShame.accept(e);
			}
		}

		return work;
	}
}
