package framework.model.event;

import framework.model.Model;

public class DiscreteEvent<T extends Model> implements Comparable<DiscreteEvent<?>> {
	private static final double ERROR_MARGIN = 0.05;
	public final double REAL_TIME;
	public int DISCRETE_TIME;
	
	protected DiscreteEvent(double rt, int dt) {
		REAL_TIME = rt;
		DISCRETE_TIME = dt;
	}

	@Override
	public int compareTo(DiscreteEvent o) {
		double realTimeDiff = REAL_TIME - o.REAL_TIME;
		if(realTimeDiff <= ERROR_MARGIN) {
			realTimeDiff = 0;
		}
		
		return realTimeDiff != 0 ? (int)(realTimeDiff * 10) : DISCRETE_TIME - o.DISCRETE_TIME;
	}
	
	public String toString() {
		return "["+getClass().getSimpleName()+"] ("+REAL_TIME+","+DISCRETE_TIME+")";
	}
	
}
