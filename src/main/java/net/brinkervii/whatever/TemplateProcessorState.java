package net.brinkervii.whatever;

import lombok.Data;

import java.util.LinkedList;

@Data
public class TemplateProcessorState {
	private LinkedList<CandidateEntry> candidates = new LinkedList<>();
}
