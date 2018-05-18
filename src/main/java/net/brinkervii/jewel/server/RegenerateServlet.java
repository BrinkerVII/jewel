package net.brinkervii.jewel.server;

import net.brinkervii.jewel.core.JewelContext;
import net.brinkervii.jewel.core.work.driver.JewelWorkerChain;
import net.brinkervii.quetzalcoatl.api.Request;
import net.brinkervii.quetzalcoatl.api.Response;
import net.brinkervii.quetzalcoatl.api.Servlet;

public class RegenerateServlet extends Servlet {
	private final JewelContext context;

	public RegenerateServlet(JewelContext context) {
		super();
		this.context = context;
	}

	@Override
	public boolean accepts(Request request) {
		return request.uri().toString().equals("/regenerate");
	}

	@Override
	public Response get(Request request) {
		Response response = new Response();

		final JewelWorkerChain activeChain = context.getActiveChain();
		if (activeChain == null) {
			response.status(500).writer(responseBody -> responseBody.string("No chain"));
		} else {
			activeChain.work();
			response.status(200).writer(responseBody -> responseBody.string("OK"));
		}

		return response;
	}
}
