package machinery.drill_model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import framework.model.event.Schedulers;
import framework.model.event.TimeAdvanceEvent;
import framework.model.token.input.InputToken;
import framework.model.token.output.OutputToken;
import machinery.SimpleMachine;
import machinery.parts.DrilledMetalDisk;
import machinery.parts.UndrilledMetalDisk;

public class DrillModel extends SimpleMachine {

	@Override
	protected Set<InputToken<?>> inputSet() {
		return new HashSet<>(Arrays.asList(new UndrilledMetalDisk()));
	}

	@Override
	protected Set<OutputToken> outputSet() {
		return new HashSet<>(Arrays.asList(new DrilledMetalDisk()));
	}

	@Override
	protected String getModelName() {
		return "Drill";
	}

	@Override
	protected int processingTime() {
		return 2;
	}
	
	@Override
	protected TimeAdvanceEvent generateTimeAdvanceEvent() {
		double rt = Schedulers.CURRENT.getRealTime();
		double ta = partsBin.size() > 0 ? timeRemaining : Integer.MAX_VALUE;
		return new TimeAdvanceEvent<DrillModel>(this, rt + ta, 0);
	}

}
