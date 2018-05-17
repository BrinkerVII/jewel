package net.brinkervii.jewel.core.pipeline;

public abstract class PipelineStage<T> {
	protected final PipelineState<T> state;

	protected PipelineStage(PipelineState<T> state) {
		this.state = state;
	}

	public T pump(T work) {
		return work;
	}
}
