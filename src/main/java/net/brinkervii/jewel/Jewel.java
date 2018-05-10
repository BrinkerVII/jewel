package net.brinkervii.jewel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Jewel implements Runnable {
	public static void main(String[] arguments) {
		Jewel application = new Jewel();

		Thread main = new Thread(application);
		main.start();
	}

	private Jewel() {

	}

	@Override
	public void run() {
		log.info("Starting Jewel...");
	}
}
