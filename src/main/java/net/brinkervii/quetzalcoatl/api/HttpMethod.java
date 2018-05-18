package net.brinkervii.quetzalcoatl.api;

public enum HttpMethod {
	NOPE,
	GET,
	HEAD,
	POST,
	PUT,
	DELETE,
	CONNECT,
	OPTIONS,
	TRACE,
	PATCH;

	public static HttpMethod fromString(String method) {
		for (HttpMethod httpMethod : HttpMethod.class.getEnumConstants()) {
			if (httpMethod.toString().toLowerCase().equals(method.toLowerCase().trim())) {
				return httpMethod;
			}
		}

		return NOPE;
	}
}
