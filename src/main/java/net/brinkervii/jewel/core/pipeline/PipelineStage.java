package net.brinkervii.jewel.core.pipeline;

public abstract class PipelineStage<T> {
	public T pump(T work) {
		return null;
	}
}
