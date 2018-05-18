package net.brinkervii.quetzalcoatl.core;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.brinkervii.quetzalcoatl.api.BadRequestResponse;
import net.brinkervii.quetzalcoatl.api.Request;
import net.brinkervii.quetzalcoatl.api.Response;
import net.brinkervii.quetzalcoatl.api.Servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.LinkedList;

public class Tohru implements HttpHandler {
	private LinkedList<Servlet> servlets = new LinkedList<>();
	private final static Servlet DEFAULT_SERVLET = new Servlet();

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		Request request = new Request(httpExchange);

		boolean served = false;
		for (Servlet servlet : servlets) {
			if (!servlet.accepts(request)) continue;

			Response response = onServlet(servlet, request);
			processResponse(response, httpExchange);

			served = true;
		}

		if (!served) {
			processResponse(onServlet(DEFAULT_SERVLET, request), httpExchange);
		}
	}

	private void processResponse(Response response, HttpExchange httpExchange) throws IOException {
		final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
		final ResponseBodyWrapper responseBodyWrapper = new ResponseBodyWrapper(byteArrayOutputStream);
		response.writeTo(responseBodyWrapper);

		httpExchange.sendResponseHeaders(response.status(), responseBodyWrapper.getSize());
		byteArrayOutputStream.writeTo(httpExchange.getResponseBody());
		httpExchange.getResponseBody().close();
	}

	private Response onServlet(Servlet servlet, Request request) {
		Response response = null;

		switch (request.method()) {
			case GET:
				response = servlet.get(request);
				break;
			case HEAD:
				response = servlet.head(request);
				break;
			case POST:
				response = servlet.post(request);
				break;
			case PUT:
				response = servlet.put(request);
				break;
			case DELETE:
				response = servlet.delete(request);
				break;
			case CONNECT:
				response = servlet.connect(request);
				break;
			case OPTIONS:
				response = servlet.options(request);
				break;
			case TRACE:
				response = servlet.trace(request);
				break;
			case PATCH:
				response = servlet.patch(request);
				break;
			default:
				response = new BadRequestResponse();
				break;
		}

		return response;
	}

	public void addServlet(Servlet servlet) {
		servlets.add(servlet);
	}
}
