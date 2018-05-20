package net.brinkervii.jewel.core.document;

import java.io.File;

public class JewelDocument {
	protected File origin;
	protected boolean alwaysWrite = false;

	public File getOrigin() {
		return origin;
	}

	public void alwaysWrite() {
		this.alwaysWrite = true;
	}
}
