package net.brinkervii.jewel.server;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.JewelContext;
import net.brinkervii.quetzalcoatl.core.Lucoa;

import java.io.IOException;

@Slf4j
public class JewelServer {
	private final JewelContext context;
	private Lucoa lucoa;

	public JewelServer(JewelContext context) {
		this.context = context;
	}

	public void init() throws IOException {
		this.lucoa = new Lucoa(4000);
		lucoa.addServlet(new FileServerServlet(context, "www"));
		lucoa.addServlet(new RegenerateServlet(context));
	}
}
