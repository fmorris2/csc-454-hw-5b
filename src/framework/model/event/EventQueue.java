package framework.model.event;

import framework.pqueue.CustomPriorityQueue;

public class EventQueue {
	private static CustomPriorityQueue queue;
	
	protected EventQueue() {
		
	}
	
	public static CustomPriorityQueue get() {
		if(queue == null) {
			queue = new CustomPriorityQueue();
		}
		
		return queue;
	}
}
