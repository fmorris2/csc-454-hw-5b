package machinery;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import framework.model.AtomicModel;
import framework.model.event.CurrentEventQueue;
import framework.model.event.InputEvent;
import framework.model.token.input.InputToken;
import framework.model.token.output.OutputToken;

public abstract class SimpleMachine extends AtomicModel {
	protected Queue<InputToken> partsBin = new LinkedList<>();
	protected double timeRemaining;
	
	protected abstract int processingTime();
	
	@Override
	protected void deltaInt() {
		partsBin.poll();		
		timeRemaining = processingTime();
		timeAdvance();
	}
	
	@Override
	protected OutputToken[] lambda() {
		return OUTPUT_SET.stream().toArray(OutputToken[]::new);
	}
	
	@Override
	protected void deltaExt() {
		if(partsBin.isEmpty()) {
			timeRemaining = processingTime();
		}
		else {
			System.out.println("deltaExt elapsedTime: " + CurrentEventQueue.get().getElapsedTime());
			timeRemaining = timeRemaining - CurrentEventQueue.get().getElapsedTime();
			System.out.println("deltaExt, timeRemaining: " + timeRemaining);
		}
		
		queuedEvents.stream()
			.filter(e -> e instanceof InputEvent)
			.flatMap(i -> Arrays.stream(((InputEvent)i).INPUT))
			.forEach(t -> partsBin.add(t));
		
		timeAdvance();
	}
	
	@Override
	protected void deltaConf() {
		deltaInt();
		deltaExt();
	}
	
	@Override
	protected String getStateString() {
		return "Parts Bin: " + partsBin.toString() + ", Time Remaining: " + timeRemaining;
	}
}
