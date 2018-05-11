package net.brinkervii.jewel.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import net.brinkervii.jewel.core.JewelContext;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;

import java.io.IOException;
import java.io.OutputStream;

public class RegenerateServerHandler implements HttpHandler {
	private final static String NO_CHAIN = "No Chain";
	private final static String OK = "OK";

	private final JewelContext context;

	public RegenerateServerHandler(JewelContext context) {
		this.context = context;
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		final JewelWorkerChain activeChain = context.getActiveChain();
		if (activeChain == null) {
			respond(httpExchange, 500, NO_CHAIN);
		} else {
			activeChain.work();
			respond(httpExchange, 200, OK);
		}
	}

	private void respond(HttpExchange httpExchange, int status, String response) throws IOException {
		httpExchange.sendResponseHeaders(status, response.length());
		try (OutputStream responseBody = httpExchange.getResponseBody()) {
			responseBody.write(response.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
