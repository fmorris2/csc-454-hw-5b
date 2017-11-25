package framework.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

import framework.model.event.DiscreteEvent;
import framework.model.token.Token;
import framework.model.token.input.InputToken;
import framework.model.token.output.OutputToken;

public abstract class Model {
	public final Set<InputToken<?>> INPUT_SET;
	public final Set<OutputToken> OUTPUT_SET;
	
	public static int numModels = 0;
	
	protected List<DiscreteEvent> queuedEvents = new ArrayList<>();
	protected OutputToken[] output;
	protected boolean isDebugMode;
	
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
	
	
	/*
	public OutputToken[] executeLambda() {
		debug("lambda is being executed for " + getModelName());
		output = lambda.execute();
		if(tick % internalTicks == 0) {
			debug("State at internal tick #"+tick+": "+getStateString());
			debug("Output at internal tick #"+tick+": ");
			Arrays.stream(output).forEach(o -> debug("\t"+o.getName()));
		}
		return output;
	}
	*/
	
	public void resetInputAndOutput() {
		output = null;
		queuedEvents.clear();
	}
	
	public void queueEvents(DiscreteEvent... events) {
		queuedEvents.addAll(Arrays.asList(events));
		debug("queued up " + events.length + " events for " + this 
				+ "... model now has " + queuedEvents.size() + " queued events.");
	}
	
	protected void log(String str) {
		System.out.println("["+getModelName()+ " " + id +"] - " + str);
	}
	
	protected void debug(String str) {
		if(isDebugMode)
			log(str);
	}
	
	public void setDebugMode(boolean debug) {
		isDebugMode = debug;
	}
}
