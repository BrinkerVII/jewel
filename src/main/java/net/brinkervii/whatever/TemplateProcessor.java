package net.brinkervii.whatever;

import lombok.extern.slf4j.Slf4j;
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
	private static final Pattern STACHE_PATTERN_INNER = Pattern.compile("\\{\\{\\s*(.*?)\\s*}}");
	private LinkedList<Map<String, Object>> providers = new LinkedList<>();

	public void provide(Map<String, Object> provider) {
		providers.add(provider);
	}

	public Document process(Document input) {
		TemplateProcessorState state = new TemplateProcessorState();
		getCandidates(input, state);

		int n_candidates = state.getCandidates().size();
		if (n_candidates > 0) {
			log.info(String.format("Got some candidate(s) in template, %d", n_candidates));
		}

		return input;
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
				if (attribute.getKey().equals("*for")) {
					candidates.add(new CandidateEntry(element, CandidateType.FORLOOP));
				}
			}
		}
	}
}
