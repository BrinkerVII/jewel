package net.brinkervii.whatever.core;

import java.util.Map;

public class TemplateContext extends MultiMap {
	public TemplateContext(Map providers) {
		super(providers);
	}

	public Object access(String accessorString) {
		Map source = null;
		Object result = null;

		for (String accesor : accessorString.split("\\.")) {
			Object v;
			if (source == null) {
				v = super.get(accesor);
			} else {
				v = source.get(accesor);
			}

			if (v instanceof Map) {
				source = (Map) v;
			} else {
				result = v;
			}
		}

		return result;
	}
}
