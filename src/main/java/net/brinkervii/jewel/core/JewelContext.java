package net.brinkervii.jewel.core;

import lombok.Data;

import java.util.ArrayList;

@Data
public class JewelContext {
	ArrayList<Stylesheet> stylesheets = new ArrayList<>();

	public void stylesheet(Stylesheet stylesheet) {
		stylesheets.add(stylesheet);
	}
}
