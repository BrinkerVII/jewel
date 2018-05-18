package net.brinkervii.quetzalcoatl.core;

import java.util.concurrent.Executor;

public class Fafnir implements Executor {
	@Override
	public void execute(Runnable runnable) {
		runnable.run();
	}
}
