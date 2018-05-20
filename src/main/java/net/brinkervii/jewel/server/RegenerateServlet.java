package net.brinkervii.jewel.server;

import net.brinkervii.jewel.core.JewelContext;
import net.brinkervii.jewel.core.exception.NoChainConstructorException;
import net.brinkervii.jewel.core.exception.NoJewelChainException;
import net.brinkervii.quetzalcoatl.api.HttpMethod;
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
		return request.uri().toString().equals("/regenerate") && request.method().equals(HttpMethod.POST);
	}

	@Override
	public Response post(Request request) {
		Response response = new Response();

		try {
			context.regenerate();
			response.status(200).writer(responseBody -> responseBody.string("OK"));
		} catch (NoJewelChainException | NoChainConstructorException e) {
			response.status(500).writer(responseBody -> responseBody.string("Regeneration failed, please refer to the logs"));
			e.printStackTrace();
		}

		return response;
	}
}
