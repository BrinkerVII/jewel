package net.brinkervii.whatever;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jsoup.nodes.Node;

@Data
@AllArgsConstructor
public class CandidateEntry {
	private Node node;
	private CandidateType candidateType;
}
