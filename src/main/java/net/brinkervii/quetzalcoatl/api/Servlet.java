package net.brinkervii.quetzalcoatl.api;

public class Servlet {
	public boolean accepts(Request request) {
		return true;
	}

	public Response get(Request request) {
		return new Response();
	}

	public Response post(Request request) {
		return new Response();
	}

	public Response put(Request request) {
		return new Response();
	}

	public Response options(Request request) {
		return new Response();
	}

	public Response connect(Request request) {
		return new Response();
	}

	public Response delete(Request request) {
		return new Response();
	}

	public Response head(Request request) {
		return new Response();
	}

	public Response trace(Request request) {
		return new Response();
	}

	public Response patch(Request request) {
		return new Response();
	}
}
