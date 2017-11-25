package framework.model.event;

import framework.model.Model;

public class TimeAdvanceEvent<T extends Model> extends DiscreteEvent {
	private T model;
	
	public TimeAdvanceEvent(T model, double rt, int dt) {
		super(rt, dt);
		this.model = model;
	}
	
	public T getModel() {
		return model;
	}
	
}
