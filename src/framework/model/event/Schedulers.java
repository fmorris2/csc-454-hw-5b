package framework.model.event;

import framework.pqueue.CustomPriorityQueue;

public class Schedulers {
	public static final CustomPriorityQueue GLOBAL = new CustomPriorityQueue();
	public static final CustomPriorityQueue CURRENT = new CustomPriorityQueue();
}
