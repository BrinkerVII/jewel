package net.brinkervii.whatever.core;

import lombok.Data;

import java.util.LinkedList;

@Data
public class TemplateProcessorState {
	private LinkedList<CandidateEntry> candidates = new LinkedList<>();
}
