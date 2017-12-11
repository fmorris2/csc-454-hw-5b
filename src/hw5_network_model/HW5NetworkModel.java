package hw5_network_model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import framework.model.Model;
import framework.model.NetworkModel;
import framework.model.coupling.Coupling;
import framework.model.coupling.CouplingType;
import framework.model.token.input.InputToken;
import framework.model.token.output.OutputToken;
import machinery.drill_model.DrillModel;
import machinery.press_model.PressModel;

public class HW5NetworkModel extends NetworkModel {

	private final HW5NetworkModelInfo INFO = new HW5NetworkModelInfo();
	
	@Override
	protected List<Model> getSubModels() {
		return INFO.MODELS;
	}

	@Override
	protected List<Coupling> getCouplings() {
		return INFO.COUPLINGS;
	}

	@Override
	protected Set<InputToken<?>> inputSet() {
		return null;
	}

	@Override
	protected Set<OutputToken> outputSet() {
		return null;
	}

	@Override
	protected String getModelName() {
		return "HW5 Network Model";
	}

	@Override
	protected String getStateString() {
		return "N/A";
	}
	
	private class HW5NetworkModelInfo {
		private final Model PRESS = new PressModel();
		private final Model DRILL = new DrillModel();
		
		public final List<Model> MODELS = new ArrayList<>(Arrays.asList(
				PRESS, DRILL
		));
		
		public final List<Coupling> COUPLINGS = new ArrayList<>(Arrays.asList(
				//PRESS COUPLINGS
				new Coupling(null, PRESS, CouplingType.INPUT_TO_INPUT),
				new Coupling(PRESS, DRILL, CouplingType.OUTPUT_TO_INPUT),
				
				//DRILL COUPLINGS
				new Coupling(DRILL, null, CouplingType.OUTPUT_TO_OUTPUT)
		));
	}
}
