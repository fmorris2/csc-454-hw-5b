package machinery.press_model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import framework.model.event.CurrentEventQueue;
import framework.model.event.TimeAdvanceEvent;
import framework.model.token.input.InputToken;
import framework.model.token.output.OutputToken;
import machinery.SimpleMachine;
import machinery.parts.MetalBall;
import machinery.parts.UndrilledMetalDisk;

public class PressModel extends SimpleMachine {

	@Override
	protected Set<InputToken<?>> inputSet() {
		return new HashSet<>(Arrays.asList(new MetalBall()));
	}

	@Override
	protected Set<OutputToken> outputSet() {
		return new HashSet<>(Arrays.asList(new UndrilledMetalDisk()));
	}

	@Override
	protected String getModelName() {
		return "Press";
	}
	
	@Override
	protected int processingTime() {
		return 1;
	}

	@Override
	protected TimeAdvanceEvent generateTimeAdvanceEvent() {
		double rt = CurrentEventQueue.getRealTime();
		double ta = partsBin.size() > 0 ? timeRemaining : Integer.MAX_VALUE;
		return new TimeAdvanceEvent<PressModel>(this, rt + ta, 0);
	}
}
