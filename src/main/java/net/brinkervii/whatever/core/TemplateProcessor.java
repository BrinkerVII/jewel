package net.brinkervii.whatever.core;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.whatever.stache.StacheElementProcessor;
import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class TemplateProcessor {
	private static final Pattern STACHE_PATTERN_OUTER = Pattern.compile("(\\{\\{\\s*.*?\\s*}})");
	private MultiMap<String, Object> providers = new MultiMap<>();
	private TemplateContext rootContext = null;

	public TemplateProcessor() {
		rootContext = new TemplateContext(providers);
	}

	public void provide(Map<String, Object> provider) {
		providers.appendSource(provider);
	}

	public Document process(Document input) {
		TemplateProcessorState state = new TemplateProcessorState();
		getCandidates(input, state);

		int n_candidates = state.getCandidates().size();
		if (n_candidates > 0) {
			log.info(String.format("Got some candidate(s) in template, %d", n_candidates));
		}

		processCandidates(state);

		return input;
	}

	private void processCandidates(TemplateProcessorState state) {
		List<Runnable> tasks = new LinkedList<>();

		for (CandidateEntry candidateEntry : state.getCandidates()) {
			switch (candidateEntry.getCandidateType()) {
				case STACHE:
					StacheElementProcessor stacheElementProcessor = new StacheElementProcessor(this, rootContext);
					tasks.add(() -> {
						stacheElementProcessor.process(candidateEntry.getNode());
					});
					break;
				case STAR_ATTRIB:
					break;
				default:
					break;
			}
		}

		tasks.forEach(Runnable::run);
	}

	private void getCandidates(Document input, TemplateProcessorState state) {
		List<CandidateEntry> candidates = state.getCandidates();

		for (Element element : input.getAllElements()) {
			String elementText = element.ownText();

			// Find 'mustaches'
			if (elementText != null && !elementText.isEmpty()) {
				final Matcher outerMatcher = STACHE_PATTERN_OUTER.matcher(elementText);
				while (outerMatcher.find()) {
					candidates.add(new CandidateEntry(element, CandidateType.STACHE));
				}
			}

			// Find for loops
			for (Attribute attribute : element.attributes()) {
				if (attribute.getKey().startsWith("*")) {
					candidates.add(new CandidateEntry(element, CandidateType.STAR_ATTRIB));
				}
			}
		}
	}
}
