package net.brinkervii.jewel.core;

import lombok.extern.slf4j.Slf4j;
import net.brinkervii.jewel.core.exception.NoChainConstructorException;
import net.brinkervii.jewel.core.exception.NoJewelChainException;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

import static java.nio.file.StandardWatchEventKinds.*;

@Slf4j
public class JewelChangeWatcher implements Runnable {
	private final JewelContext context;
	private WatchService watcher;
	private Thread thread = null;
	private boolean running = true;

	public JewelChangeWatcher(JewelContext context) {
		this.context = context;
	}

	public void init() throws IOException {
		final File src = new File(context.config().getSourceLocation());
		final File theme = new File(context.config().getThemeLocation());

		this.watcher = FileSystems.getDefault().newWatchService();
		registerRecursive(watcher, Paths.get(src.getAbsolutePath()));
		registerRecursive(watcher, Paths.get(theme.getAbsolutePath()));

		this.thread = new Thread(this);
		thread.start();

		Runtime.getRuntime().addShutdownHook(new Thread(this::shutdown));
	}

	private void registerRecursive(final WatchService watchService, final Path root) throws IOException {
		Files.walkFileTree(root, new SimpleFileVisitor<Path>() {
			@Override
			public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
				dir.register(watchService, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
				return FileVisitResult.CONTINUE;
			}
		});
	}

	private void shutdown() {
		log.info("Stopping file watcher");
		this.running = false;

		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (thread.isAlive()) {
			thread.interrupt();
		}
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
