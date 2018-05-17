package net.brinkervii.whatever.core;

import org.jsoup.nodes.Element;

public abstract class TemplateElementProcessor {
	protected final TemplateProcessor parent;

	protected TemplateElementProcessor(TemplateProcessor parent) {
		this.parent = parent;
	}

	public Element process(Element element) {
		return element;
	}
}
