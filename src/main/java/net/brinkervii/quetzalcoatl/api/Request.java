package net.brinkervii.quetzalcoatl.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sun.net.httpserver.HttpExchange;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

public class Request {
	private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	private final HttpMethod method;
	private final String requestBody;
	private final HttpExchange exchange;

	public Request(HttpExchange exchange) throws IOException {
		this.exchange = exchange;
		this.method = HttpMethod.fromString(exchange.getRequestMethod());
		this.requestBody = IOUtils.toString(exchange.getRequestBody(), StandardCharsets.UTF_8);
	}

	public HttpMethod method() {
		return method;
	}

	public URI uri() {
		return exchange.getRequestURI();
	}

	public <T> T object(Class<T> clazz) throws IOException {
		return OBJECT_MAPPER.readValue(this.requestBody, clazz);
	}
}
