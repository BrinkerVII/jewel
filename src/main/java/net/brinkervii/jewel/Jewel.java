package net.brinkervii.jewel;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.JewelChangeWatcher;
import net.brinkervii.jewel.core.JewelContext;
import net.brinkervii.jewel.core.exception.NoChainConstructorException;
import net.brinkervii.jewel.core.exception.NoJewelChainException;
import net.brinkervii.jewel.core.work.driver.DefaultJewelWorkerChain;
import net.brinkervii.jewel.server.JewelServer;

import java.io.IOException;

@Slf4j
public class Jewel implements Runnable {
	private static JewelContext context;

	public static void main(String[] arguments) {
		context = new JewelContext();

		Jewel application = new Jewel();

		Thread main = new Thread(application);
		main.start();

		JewelServer server = new JewelServer(context);
		try {
			server.init();
		} catch (IOException e) {
			e.printStackTrace();
		}

		final JewelChangeWatcher jewelChangeWatcher = new JewelChangeWatcher(context);
		try {
			jewelChangeWatcher.init();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private Jewel() {

	}

	@Override
	public void run() {
		log.info("Starting Jewel...");

		context.setChainConstructor(() -> new DefaultJewelWorkerChain(context));

		try {
			context.regenerate();
		} catch (NoJewelChainException | NoChainConstructorException e) {
			log.error("Press F to pay respect");
			e.printStackTrace();
		}
	}
}
