package framework.model;

import java.util.Arrays;

import framework.model.event.CurrentEventQueue;
import framework.model.event.DiscreteEvent;
import framework.model.event.EventQueue;
import framework.model.event.InputEvent;
import framework.model.event.TimeAdvanceEvent;

public class DevsSimulation {
	private NetworkModel network;
	
	public DevsSimulation(NetworkModel network, InputEvent... inputTrajectory) {
		this.network = network;
		EventQueue.get().addAll(inputTrajectory);
		network.initializeTimeAdvance();
	}
	
	public void run() {
		while(shouldExecute()) {
			network.run();
			CurrentEventQueue.discreteTime = 0;
		}
	}
	
	private boolean shouldExecute() {
		return !(hasAllInfTimeAdv(CurrentEventQueue.get().getElements()) && eventQueueHasNoInput()
					&& hasAllInfTimeAdv(EventQueue.get().getElements()));
	}
	
	private boolean eventQueueHasNoInput() {
		return Arrays.stream(EventQueue.get().getElements())
						.noneMatch(e -> e instanceof InputEvent);
	}
	
	private boolean hasAllInfTimeAdv(DiscreteEvent[] events) {
		return Arrays.stream(events)
				.filter(e -> e instanceof TimeAdvanceEvent)
				.allMatch(e -> ((TimeAdvanceEvent)e).REAL_TIME >= Integer.MAX_VALUE);		
	}
}
