package net.brinkervii.whatever.core;

import net.brinkervii.common.MultiMap;

import java.util.Map;

public class TemplateContext extends MultiMap {
	public TemplateContext(Map providers) {
		super(providers);
	}

	public Object access(String accessorString) {
		Map source = null;
		Object result = null;

		for (String accessor : accessorString.split("\\.")) {
			Object v;
			if (source == null) {
				v = super.get(accessor);
			} else {
				v = source.get(accessor);
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
