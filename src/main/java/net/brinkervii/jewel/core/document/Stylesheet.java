package net.brinkervii.jewel.core.document;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class Stylesheet extends JewelDocument {
	private String content = null;
	private File file = null;
	private String sourceMap = "";

	public Stylesheet() {
	}

	public Stylesheet(File file) {
		this.file = file;
	}

	public Stylesheet(String file) {
		this(new File(file));
	}

	public String getContent() {
		if (content == null) {
			if (file != null) {
				InputStream inputStream = null;
				try {
					inputStream = new FileInputStream(file);
					content = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
				} catch (IOException e) {
					e.printStackTrace();
					content = "";
				} finally {
					if (inputStream != null) {
						try {
							inputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				}
			}

			if (content == null) {
				return "";
			}
		}

		return content;
	}

	public static Stylesheet withContent(File origin, String content) {
		Stylesheet stylesheet = new Stylesheet();
		stylesheet.content = content;
		stylesheet.origin = origin;

		return stylesheet;
	}

	public void setSourceMap(String sourceMap) {
		this.sourceMap = sourceMap;
	}

	public Stylesheet clone() {
		Stylesheet clone = new Stylesheet();
		clone.file = file;
		clone.content = content;
		clone.sourceMap = sourceMap;

		clone.origin = origin;
		clone.sourceFile = sourceFile;
		clone.alwaysWrite = alwaysWrite;


		return clone;
	}
}
