package net.brinkervii.quetzalcoatl.core;

import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;
import net.brinkervii.common.BucketOfShame;
import net.brinkervii.quetzalcoatl.api.Servlet;

import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
public class Lucoa {
	private HttpServer httpServer;
	private final int port;
	private Tohru httpContext;

	public Lucoa(int port) {
		this.port = port;

		try {
			init();
		} catch (IOException e) {
			BucketOfShame.accept(e);
		}
	}

	private void init() throws IOException {
		InetSocketAddress address = new InetSocketAddress(this.port);
		this.httpServer = HttpServer.create(address, 0);
		this.httpContext = new Tohru();
		httpServer.createContext("/", httpContext);

		httpServer.setExecutor(new Fafnir());

		log.info("Starting Lucoa");
		httpServer.start();

		Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
	}

	private void shutdown() {
		log.info("Stopping Lucoa");
		httpServer.stop(0);
	}

	public void addServlet(Servlet servlet) {
		this.httpContext.addServlet(servlet);
	}
}
