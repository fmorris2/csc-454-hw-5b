package framework.model.event;

import java.util.Arrays;

import framework.model.token.input.InputToken;

public class InputEvent extends DiscreteEvent {
	public final InputToken<?>[] INPUT;
	
	public InputEvent(double realTime, int discreteTime, InputToken<?>... tokens) {
		super(realTime, discreteTime);
		INPUT = tokens;
	}
	
	public String toString() {
		return super.toString() + ", input: " + Arrays.toString(INPUT);
	}
}
