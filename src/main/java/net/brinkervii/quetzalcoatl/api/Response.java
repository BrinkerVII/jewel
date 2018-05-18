package net.brinkervii.quetzalcoatl.api;

import net.brinkervii.quetzalcoatl.core.ResponseBodyWrapper;

public class Response {
	private ResponseWriter writer;
	protected int status = 200;

	public Response() {
		this.writer = responseBody -> responseBody.string("Hello world");
	}

	public Response writeTo(ResponseBodyWrapper responseBody) {
		this.writer.write(responseBody);
		return this;
	}

	public int status() {
		return status;
	}

	public Response status(int status) {
		this.status = status;
		return this;
	}

	public Response writer(ResponseWriter writer) {
		this.writer = writer;
		return this;
	}
}
