package net.brinkervii.jewel.core.server;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.JewelContext;
import net.brinkervii.jewel.util.HtmlUtil;

import java.io.*;
import java.net.URI;
import java.nio.file.Paths;

@Slf4j
public class FileServerHandler implements HttpHandler {
	private final static String BAD_REQUEST = "Bad Request";
	private final static String FOUR_O_FOUR = "404 rest in rip";
	private final static File HTTP_ROOT = new File("site");
	private final JewelContext context;

	public FileServerHandler(JewelContext context) {
		this.context = context;
	}

	@Override
	public void handle(HttpExchange httpExchange) throws IOException {
		if (!httpExchange.getRequestMethod().toUpperCase().equals("GET")) {
			serveBadRequest(httpExchange);
			return;
		}

		final URI requestURI = httpExchange.getRequestURI();
		File file = new File(Paths.get(HTTP_ROOT.getAbsolutePath(), requestURI.toString()).toAbsolutePath().toString());
		if (file.isDirectory()) {
			File indexHTML = new File(Paths.get(file.getPath(), "index.html").toString());
			if (indexHTML.exists() && indexHTML.isFile()) {
				file = indexHTML;
			}
		}

		if (file.exists()) {
			serveFile(httpExchange, file);
		} else {
			serve404(httpExchange, file);
		}
	}

	private void serveBadRequest(HttpExchange httpExchange) throws IOException {
		log.info("Serving 400 (BAD REQUEST)");

		httpExchange.sendResponseHeaders(400, BAD_REQUEST.length());
		try (OutputStream outputStream = httpExchange.getResponseBody()) {
			outputStream.write(BAD_REQUEST.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void serve404(HttpExchange httpExchange, File file) throws IOException {
		log.info("Serving 404");

		String response = String.format("File not found: %s\n\n%s", HtmlUtil.escape(file.getName()), FOUR_O_FOUR);
		httpExchange.sendResponseHeaders(404, response.length());

		try (OutputStream outputStream = httpExchange.getResponseBody()) {
			outputStream.write(response.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void serveFile(HttpExchange httpExchange, File file) throws IOException {
		log.info("Serving file");

		FileInputStream inputStream = new FileInputStream(file);
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		while (inputStream.available() > 0) {
			outputStream.write(inputStream.read());
		}

		httpExchange.sendResponseHeaders(200, outputStream.size());
		try (OutputStream responseBody = httpExchange.getResponseBody()) {
			outputStream.writeTo(responseBody);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
