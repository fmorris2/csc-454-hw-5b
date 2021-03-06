package framework.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import framework.model.event.DiscreteEvent;
import framework.model.event.Schedulers;
import framework.model.token.input.InputToken;
import framework.model.token.output.OutputToken;

public abstract class Model {
	public final Set<InputToken<?>> INPUT_SET;
	public final Set<OutputToken> OUTPUT_SET;
	
	public static int numModels = 0;
	
	protected List<DiscreteEvent> queuedEvents = new ArrayList<>();
	protected List<OutputToken> output = new ArrayList<>();
	protected boolean isDebugMode;
	protected double elapsedTime = 0, lastEventTime = 0;
	
	private int id;
	
	protected Model() {
		INPUT_SET = inputSet();
		OUTPUT_SET = outputSet();
		numModels++;
		id = numModels;
	}

	protected abstract Set<InputToken<?>> inputSet();
	protected abstract Set<OutputToken> outputSet();
	protected abstract String getModelName();
	protected abstract String getStateString();
	protected abstract void executeFunctions();
	
	public void resetInputAndOutput() {
		output.clear();
		queuedEvents.clear();
	}
	
	public void queueEvents(DiscreteEvent... events) {
		queuedEvents.addAll(Arrays.asList(events));
		debug("queued up " + events.length + " events for " + this 
				+ "... model now has " + queuedEvents.size() + " queued events.");
		elapsedTime = Schedulers.CURRENT.getRealTime() - lastEventTime;
		lastEventTime = events[events.length - 1].REAL_TIME;
	}
	
	protected void log(String str) {
		System.out.println(this + " - " + str);
	}
	
	protected void debug(String str) {
		if(isDebugMode)
			log(str);
	}
	
	public void setDebugMode(boolean debug) {
		isDebugMode = debug;
	}
	
	public String toString() {
		return "["+getModelName()+ " " + id +"]";
	}
}
