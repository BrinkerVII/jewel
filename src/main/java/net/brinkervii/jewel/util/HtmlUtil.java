package net.brinkervii.jewel.util;

public class HtmlUtil {
	public static String escape(String name) {
		return name
			.replaceAll("<", "&lt;")
			.replaceAll(">", "&gt;");
	}
}
