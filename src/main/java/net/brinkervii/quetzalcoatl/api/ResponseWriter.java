package net.brinkervii.quetzalcoatl.api;

import net.brinkervii.quetzalcoatl.core.ResponseBodyWrapper;

public interface ResponseWriter {
	void write(ResponseBodyWrapper responseBody);
}
