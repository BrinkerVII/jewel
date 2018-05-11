package net.brinkervii.jewel.core.server;

import com.sun.net.httpserver.HttpServer;
import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.JewelContext;

import java.io.IOException;
import java.net.InetSocketAddress;

@Slf4j
public class JewelServer {
	private final JewelContext context;
	private HttpServer server;

	public JewelServer(JewelContext context) {
		this.context = context;
	}

	public void init() throws IOException {
		InetSocketAddress address = new InetSocketAddress(6969);
		this.server = HttpServer.create(address, 0);
		server.createContext("/", new FileServerHandler(context));
		server.createContext("/regenerate", new RegenerateServerHandler(context));

		server.setExecutor(null);

		log.info("Starting HTTP Server");
		server.start();

		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
			log.info("Stopping HTTP Server");
			server.stop(0);
		}));
	}
}
