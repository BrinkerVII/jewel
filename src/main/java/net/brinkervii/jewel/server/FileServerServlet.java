package net.brinkervii.jewel.server;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.common.BucketOfShame;
import net.brinkervii.jewel.core.JewelContext;
import net.brinkervii.quetzalcoatl.api.Request;
import net.brinkervii.quetzalcoatl.api.Response;
import net.brinkervii.quetzalcoatl.api.Servlet;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Slf4j
public class FileServerServlet extends Servlet {
	private final static File cwd = new File("./").getAbsoluteFile();
	private final JewelContext context;
	private File root;

	public FileServerServlet(JewelContext context) {
		this(context, cwd);
	}

	public FileServerServlet(JewelContext context, String rootPath) {
		this(context, new File(rootPath));
	}

	public FileServerServlet(JewelContext context, File rootPath) {
		super();
		this.context = context;

		if (!rootPath.exists()) {
			log.warn(String.format("Root path %s does not exist", rootPath.getAbsolutePath()));
		}

		this.root = rootPath;
	}

	protected File getFile(Request request) {
		return new File(root, request.uri().toString());
	}

	@Override
	public boolean accepts(Request request) {
		final File file = getFile(request);
		log.info(file.toString());
		return file.exists();
	}

	@Override
	public Response get(Request request) {
		Response response = new Response();

		File file = getFile(request);
		if (file.isDirectory()) {
			File indexFile = new File(file, "index.html");
			if (indexFile.exists()) {
				log.info(String.format("Rewrite %s -> %s", file, indexFile));
				file = indexFile;
				indexFile = null;
			}
		}

		try (FileInputStream fileInputStream = new FileInputStream(file)) {
			byte[] bytes = new byte[(int) file.length()];
			IOUtils.readFully(fileInputStream, bytes);
			response.writer(responseBody -> {
				try {
					responseBody.bytes(bytes);
				} catch (IOException e) {
					BucketOfShame.accept(e);
					response.status(500).writer(responseBody1 -> responseBody1.string("IO Error"));
				}
			});
		} catch (IOException e) {
			BucketOfShame.accept(e);
			response.status(500).writer(responseBody -> responseBody.string("General failure on site " + context.config().getSiteProperties().getTitle()));
		}

		return response;
	}
}
