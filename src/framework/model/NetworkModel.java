package framework.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import framework.model.coupling.Coupling;
import framework.model.event.CurrentEventQueue;
import framework.model.event.DiscreteEvent;
import framework.model.event.EventQueue;
import framework.model.event.InputEvent;
import framework.model.event.TimeAdvanceEvent;
import framework.model.token.input.InputToken;

public abstract class NetworkModel extends Model {
	private List<Model> subModels = new ArrayList<>();
	private List<Coupling> couplings = new ArrayList<>();
	
	protected abstract List<Model> getSubModels();
	protected abstract List<Coupling> getCouplings();
	
	public NetworkModel build() {
		this.subModels = getSubModels();
		getCouplings().stream()
			.filter(c -> verifyCoupling(c))
			.forEach(c -> this.couplings.add(c));
		return this;
	}
	
	public void run() {
		debug("state at " + CurrentEventQueue.getTimeString() + " - " + this.getStateString());
		debug("elapsed time: " + CurrentEventQueue.get().getElapsedTime());
		debug("event queue size at " + CurrentEventQueue.getTimeString() + " - " + EventQueue.get().size());
		debug("current events queue size at " + CurrentEventQueue.getTimeString() + " - " + CurrentEventQueue.get().size());
	
		passRelevantEventsToSubModels(CurrentEventQueue.get().getElements());
		executeSubModelsWithEvents();
		
		CurrentEventQueue.get().clear();
		prepareNextEvents();
		//realTime = CURRENT_EVENTS.peek().REAL_TIME;
		System.out.println("");
	}
	
	private void executeSubModelsWithEvents() {
		for(Model m : subModels) {
			if(!m.queuedEvents.isEmpty()) {
				m.executeFunctions();
				if(m.output != null) {
					routeOutput(m);
				}
				m.resetInputAndOutput();
			}
		}
	}
	
	private void routeOutput(Model m) {
		couplings.stream()
			.filter(c -> c.FIRST == m)
			.forEach(c -> {
				if(c.LAST == null) {
					
				}
				else {
					
				}
			});
	}
	
	@Override
	public void executeFunctions() {
		passRelevantEventsToSubModels(queuedEvents.toArray(new DiscreteEvent[queuedEvents.size()]));
		executeSubModelsWithEvents();
	}
	
	private void passRelevantEventsToSubModels(DiscreteEvent[] events) {
		List<InputEvent> inputList = new ArrayList<>();
		for(DiscreteEvent e : events) {
			if(e instanceof InputEvent) {
				inputList.add((InputEvent)e);
			}
			else //time advance event
			{
				((TimeAdvanceEvent)e).getModel().queueEvents(e);
			}
		}
		
		if(inputList.isEmpty()) {
			return;
		}
		couplings.stream().filter(c -> c.FIRST == null).forEach(c -> c.LAST.queueEvents(inputList.toArray(new InputEvent[inputList.size()])));
	}
	
	private void prepareNextEvents() {
		if(!EventQueue.get().isEmpty()) {
			do {
				DiscreteEvent event = EventQueue.get().poll();
				debug("Adding event from event queue to current events: " + event);
				CurrentEventQueue.get().insert(event);
			}while(!EventQueue.get().isEmpty() && EventQueue.get().peek().REAL_TIME <= CurrentEventQueue.getRealTime());
		}
	}
	
	public void initializeTimeAdvance() {
		for(Model m : subModels) {
			if(m instanceof NetworkModel) {
				((NetworkModel)m).initializeTimeAdvance();
			}
			else {
				((AtomicModel)m).timeAdvance();
			}
		}
	}
	
	private boolean verifyCoupling(Coupling c) {
		switch(c.TYPE) {
		case INPUT_TO_INPUT:
		case OUTPUT_TO_INPUT:
			if(c.FIRST == c.LAST)
				return false;
			if(c.FIRST == null || c.LAST == null)
				return true;
			
			return c.FIRST.INPUT_SET.stream().allMatch(i -> containsType(i, c.LAST.INPUT_SET));
		default:
			return true;
		}
	}
	
	private boolean containsType(InputToken<?> type, Set<InputToken<?>> set)
	{
		for(InputToken<?> t : set) {
			if(type.getClass().equals(t.getClass()))
				return true;
		}
		
		return false;
	}
	
	@Override
	public void resetInputAndOutput() {
		super.resetInputAndOutput();
		for(Model m : subModels) {
			m.resetInputAndOutput();
		}
	}
	
	@Override
	public void setDebugMode(boolean debug) {
		super.setDebugMode(debug);
		for(Model m : subModels) {
			m.setDebugMode(debug);
		}
	}
}
