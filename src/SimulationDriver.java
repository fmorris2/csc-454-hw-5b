import framework.model.DevsSimulation;
import framework.model.NetworkModel;
import framework.model.event.InputEvent;
import hw5_network_model.HW5NetworkModel;
import machinery.parts.MetalBall;


public class SimulationDriver {

	public static void main(String[] args) {
		NetworkModel model = new HW5NetworkModel().build();//new HW3NetworkModel().build(); //new VendingMachine().build();
		model.setDebugMode(false);
		
		InputEvent[] INPUT_TRAJECTORY = {
				new InputEvent(0.3,0,new MetalBall()), new InputEvent(0.7,0,new MetalBall()),
				new InputEvent(1.0,0,new MetalBall()), new InputEvent(1.5,0,new MetalBall()),
				new InputEvent(3.5,0,new MetalBall())
		};
		
		DevsSimulation sim = new DevsSimulation(model, INPUT_TRAJECTORY);
		sim.run();
	}

}
