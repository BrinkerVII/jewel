package net.brinkervii.whatever.stache;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.pipeline.Pipeline;
import net.brinkervii.whatever.core.TemplateContext;
import net.brinkervii.whatever.core.TemplateElementProcessor;
import net.brinkervii.whatever.core.TemplateProcessor;
import net.brinkervii.whatever.stache.piping.StachePipeline;
import net.brinkervii.whatever.stache.piping.StachePipelineState;
import org.jsoup.nodes.Element;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class StacheElementProcessor extends TemplateElementProcessor {
	private static final Pattern STACHE_PATTERN_INNER = Pattern.compile("\\{\\{\\s*(.*?)\\s*}}");
	private final TemplateContext templateContext;

	public StacheElementProcessor(TemplateProcessor parent, TemplateContext templateContext) {
		super(parent);
		this.templateContext = templateContext;
	}

	@Override
	public Element process(Element element) {
		final Matcher matcher = STACHE_PATTERN_INNER.matcher(element.ownText());
		while (matcher.find()) {
			String stacheContent = matcher.group(1);
			log.info(String.format("Got stache content %s", stacheContent));

			Pipeline<Element> pipeline = buildPipeline(stacheContent);
			pipeline.pump(element);
		}

		return element;
	}

	private Pipeline<Element> buildPipeline(String stacheContent) {
		StachePipeline pipeline;
		List<String> bits = new LinkedList<>();

		final StringBuilder[] stringBuilder = {new StringBuilder()};
		boolean escaping = false;

		Runnable complete = () -> {
			bits.add(stringBuilder[0].toString());
			stringBuilder[0] = new StringBuilder();
		};

		for (char c : stacheContent.toCharArray()) {
			if (escaping) {
				stringBuilder[0].append(c);
				continue;
			}

			if (c == '\\') {
				escaping = true;
				continue;
			}

			if (c == '|') {
				complete.run();
				continue;
			}

			stringBuilder[0].append(c);
		}
		complete.run();

		pipeline = new StachePipeline(bits, new StachePipelineState(parent, templateContext));

		return pipeline;
	}
}
