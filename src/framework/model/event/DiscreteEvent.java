package framework.model.event;

import framework.model.Model;

public class DiscreteEvent<T extends Model> implements Comparable<DiscreteEvent<?>> {
	public final double REAL_TIME;
	public int DISCRETE_TIME;
	
	private final long REAL_TIME_LONG;
	
	protected DiscreteEvent(double rt, int dt) {
		REAL_TIME = rt;
		REAL_TIME_LONG = Math.round(rt * 100);
		DISCRETE_TIME = dt;
	}

	@Override
	public int compareTo(DiscreteEvent o) {
		final long REAL_TIME_DIFF = REAL_TIME_LONG - o.REAL_TIME_LONG;
		final int DIFF = REAL_TIME_DIFF < Integer.MIN_VALUE ? Integer.MIN_VALUE 
				: REAL_TIME_DIFF > Integer.MAX_VALUE ? Integer.MAX_VALUE 
						: (int)REAL_TIME_DIFF;
		return DIFF != 0 ? DIFF : DISCRETE_TIME - o.DISCRETE_TIME;
	}
	
	public String toString() {
		return "["+getClass().getSimpleName()+"] ("+REAL_TIME+","+DISCRETE_TIME+")";
	}
	
}
