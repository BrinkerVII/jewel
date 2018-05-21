package net.brinkervii.jewel.core;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.exception.NoChainConstructorException;
import net.brinkervii.jewel.core.exception.NoJewelChainException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;

import static java.nio.file.StandardWatchEventKinds.*;

@Slf4j
public class JewelChangeWatcher implements Runnable {
	private final JewelContext context;
	private WatchService watcher;
	private Thread thread;
	private boolean running = true;

	public JewelChangeWatcher(JewelContext context) {
		this.context = context;
	}

	public void init() throws IOException {
		File src = new File(context.config().getSourceLocation());
		File theme = new File(context.config().getThemeLocation());

		this.watcher = FileSystems.getDefault().newWatchService();
		Paths.get(src.getAbsolutePath()).register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);
		Paths.get(theme.getAbsolutePath()).register(watcher, ENTRY_CREATE, ENTRY_MODIFY, ENTRY_DELETE);

		this.thread = new Thread(this);
		thread.start();

		Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
	}

	private void shutdown() {
		log.info("Stopping file watcher");
		this.running = false;
	}

	@Override
	public void run() {
		log.info("Starting file watcher...");

		while (running) {
			WatchKey key = null;
			try {
				key = watcher.take();
			} catch (InterruptedException e) {
				e.printStackTrace();
				continue;
			}


			for (WatchEvent<?> watchEvent : key.pollEvents()) {
				final WatchEvent.Kind<?> kind = watchEvent.kind();
				if (kind.equals(OVERFLOW)) continue;

				WatchEvent<Path> ev = (WatchEvent<Path>) watchEvent;
				Path filename = ev.context();

				log.info("File changed: " + filename.toString());
				try {
					context.regenerate();
				} catch (NoJewelChainException | NoChainConstructorException e) {
					e.printStackTrace();
				}
			}

			key.reset();
		}
	}
}
