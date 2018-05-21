package net.brinkervii.jewel.core.frontmatter;

public class FrontMatterParser {
	private String content;

	private String frontMatterString = "";
	private String documentContentString = "";

	private FrontMatter frontMatter = new FrontMatter();

	public FrontMatterParser(String content) {
		this.content = content;
	}

	public void parse() throws RuntimeException {
		if (content == null) return;
		if (!content.startsWith("---")) {
			// Document does not qualify
			this.documentContentString = content;
			return;
		}

		int dashCounter = 0;
		int idx = 0;
		int mode = 0;

		int fmStart = 0;
		int fmEnd = -1;

		for (char c : content.toCharArray()) {
			if (c == '-') {
				dashCounter++;
				if (dashCounter >= 3) {
					if (mode == 0) fmStart = idx + 1;
					mode++;
					dashCounter = 0;
				}

				if (mode >= 2) {
					fmEnd = idx - 2;
					break;
				}
			}

			idx++;
		}


		if (fmStart >= 0 && fmEnd >= 0 && fmEnd > fmStart) {
			int fmLength = fmEnd - fmStart;
			if (fmLength < 3) {
				throw new RuntimeException(); // TODO: Come up with something better
			}

			frontMatterString = content.substring(fmStart, fmEnd).trim();
			documentContentString = content.substring(fmEnd + 3).trim();
		} else {
			documentContentString = content;
		}
		content = null;

		frontMatter.load(frontMatterString);
	}

	public String getDocumentContentString() {
		return documentContentString;
	}

	public FrontMatter getFrontMatter() {
		return frontMatter;
	}

	public boolean isEmpty() {
		if (frontMatter == null) return true;
		return frontMatter.isEmpty();
	}
}
