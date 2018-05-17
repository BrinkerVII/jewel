package net.brinkervii.jewel.core.pipeline;

import java.util.ArrayList;

public class Pipeline<T> {
	private ArrayList<PipelineStage<T>> stages = new ArrayList<>();
	protected PipelineState<T> state = null;

	public Pipeline<T> stage(PipelineStage<T> stage) {
		stages.add(stage);
		return this;
	}

	public T pump(T input) {
		T work = input;
		for (PipelineStage<T> stage : stages) {
			work = stage.pump(work);
		}

		return work;
	}

	public PipelineState<T> getState() {
		return state;
	}

	public void setState(PipelineState<T> state) {
		this.state = state;
	}
}
