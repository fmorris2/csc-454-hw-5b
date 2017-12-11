package framework.model;

import framework.model.event.InputEvent;
import framework.model.event.Schedulers;

public class DevsSimulation {
	private NetworkModel network;
	
	public DevsSimulation(NetworkModel network, InputEvent... inputTrajectory) {
		this.network = network;
		Schedulers.GLOBAL.addAll(inputTrajectory);
		network.initializeTimeAdvance();
	}
	
	public void run() {
		while(shouldExecute()) {
			network.run();
			Schedulers.CURRENT.resetDiscreteTime();
		}
	}
	
	private boolean shouldExecute() {
		return !(Schedulers.CURRENT.hasAllInfTimeAdv()
					&& Schedulers.CURRENT.hasNoInput()
					&& Schedulers.GLOBAL.hasAllInfTimeAdv()
					&& Schedulers.GLOBAL.hasNoInput());
	}
}
