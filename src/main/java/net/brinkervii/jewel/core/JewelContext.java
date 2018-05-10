package net.brinkervii.jewel.core;

import lombok.Data;

import java.io.File;
import java.util.ArrayList;

@Data
public class JewelContext {
	ArrayList<Stylesheet> stylesheets = new ArrayList<>();
	File outputDirectory = new File("site");

	public void stylesheet(Stylesheet stylesheet) {
		stylesheets.add(stylesheet);
	}
}
