package net.brinkervii.whatever.core;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jsoup.nodes.Element;

@Data
@AllArgsConstructor
public class CandidateEntry {
	private Element node;
	private CandidateType candidateType;
}
