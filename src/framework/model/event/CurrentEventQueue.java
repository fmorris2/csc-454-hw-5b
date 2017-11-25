package framework.model.event;

import framework.pqueue.CustomPriorityQueue;

public class CurrentEventQueue {
	private static CustomPriorityQueue queue;
	
	protected CurrentEventQueue() {
		
	}
	
	public static CustomPriorityQueue get() {
		if(queue == null) {
			queue = new CustomPriorityQueue();
		}
		
		return queue;
	}
	
	public static double getRealTime() {
		DiscreteEvent current = get().peek();
		return current == null ? 0 : current.REAL_TIME;
	}
	
	public static String getTimeString() {
		DiscreteEvent current = get().peek();
		return current == null ? "(0,0)" : "("+current.REAL_TIME+","+current.DISCRETE_TIME+")";
	}
}
