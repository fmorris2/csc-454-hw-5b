package framework.model;

import java.util.Arrays;

import framework.model.event.CurrentEventQueue;
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
			//System.out.println("[Simulation] Executing simulation");
			network.run();
		}
	}
	
	private boolean shouldExecute() {
		return !(CurrentEventQueue.get().size() <= 1 && eventQueueHasNoInput()
					&& eventQueueHasAllInfTimeAdv());
	}
	
	private boolean eventQueueHasNoInput() {
		//System.out.println("Event queue size: " + EventQueue.get().size());
		//System.out.println("Current events size: " + CurrentEventQueue.get().size());
		return Arrays.stream(EventQueue.get().getElements())
						.noneMatch(e -> e instanceof InputEvent);
	}
	
	private boolean eventQueueHasAllInfTimeAdv() {
		return Arrays.stream(EventQueue.get().getElements())
				.filter(e -> e instanceof TimeAdvanceEvent)
				.noneMatch(e -> ((TimeAdvanceEvent)e).REAL_TIME == Integer.MAX_VALUE);		
	}
}
