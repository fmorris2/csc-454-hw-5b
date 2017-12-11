package framework.pqueue;

import java.util.Arrays;

import framework.model.event.DiscreteEvent;
import framework.model.event.InputEvent;
import framework.model.event.TimeAdvanceEvent;

public class CustomPriorityQueue {
	private DiscreteEvent[] orderedArray;
	private double lastPolledRealTime;
	private int discreteTime;
	
	public CustomPriorityQueue() {
		orderedArray = new DiscreteEvent[0];
	}
	
	//public interface
	
	//O(2n) --> This is a very brute-forced & slow algorithm.
	//It was simple & quick to make however
	public void insert(DiscreteEvent element) {
		int indexToInsertAt = 0;
		
		//find appropriate index to insert at
		for(int i = 0; i < orderedArray.length; i++) {
			if(element.compareTo(orderedArray[i]) <= 0) {
				break;
			}
			indexToInsertAt = i + 1;
		}
		
		//insert at appropriate index
		DiscreteEvent[] newArr = new DiscreteEvent[orderedArray.length + 1];
		for(int i = 0; i < newArr.length; i ++) {
			if(i > indexToInsertAt) {
				newArr[i] = orderedArray[i - 1];
			}
			else if(i < indexToInsertAt) {
				newArr[i] = orderedArray[i];
			}
			else {
				newArr[i] = element;
			}
		}
		
		orderedArray = newArr;
	}
		
	///O(1) --> will always be the first element in the array
	public DiscreteEvent peek() {
		return orderedArray.length == 0 ? null : orderedArray[0];
	}
	
	/*	
 	O(n) --> due to copying the array after we remove the min element
	complexity actually depends on the Arrays#copyOfRange implementation... could be
	faster than O(n)
	*/
	public DiscreteEvent poll() {
		
		if(orderedArray.length == 0)
			return null;
		
		DiscreteEvent minElement = orderedArray[0]; //get min element
		
		//we now need to get rid of the min element
		orderedArray = orderedArray.length == 1 ? new DiscreteEvent[0] 
				: Arrays.copyOfRange(orderedArray, 1, orderedArray.length);
		
		lastPolledRealTime = minElement.REAL_TIME;
		return minElement;
	}
	
	public void clear() {
		lastPolledRealTime = orderedArray.length == 0 ? 0 : orderedArray[0].REAL_TIME;
		orderedArray = new DiscreteEvent[0];
	}
	
	public void addAll(DiscreteEvent... elements) {
		for(DiscreteEvent e : elements) {
			insert(e);
		}
	}
	
	public double getElapsedTime() {
		if(orderedArray.length == 0) {
			return 0;
		}
		
		return orderedArray[0].REAL_TIME - lastPolledRealTime;
	}
	
	public double getRealTime() {
		DiscreteEvent current = peek();
		return current == null ? 0 : current.REAL_TIME;
	}
	
	public boolean hasNoInput() {
		return Arrays.stream(orderedArray)
						.noneMatch(e -> e instanceof InputEvent);
	}
	
	public boolean hasAllInfTimeAdv() {
		return Arrays.stream(orderedArray)
				.filter(e -> e instanceof TimeAdvanceEvent)
				.allMatch(e -> ((TimeAdvanceEvent)e).REAL_TIME >= Integer.MAX_VALUE);		
	}
	
	public String getTimeString() {
		DiscreteEvent current = peek();
		return current == null ? "(0,0)" : "("+current.REAL_TIME+","+discreteTime+")";
	}
	
	public DiscreteEvent[] getElements() {
		return orderedArray;
	}
	
	public void setElements(DiscreteEvent[] elements) {
		this.orderedArray = elements;
	}
	 
	public int size() {
		return orderedArray.length;
	}
	
	public boolean isEmpty() {
		return size() == 0;
	}
	
	public void resetDiscreteTime() {
		discreteTime = 0;
	}
	
	public void incrementDiscreteTime() {
		discreteTime++;
	}
}
