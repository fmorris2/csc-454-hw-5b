package framework.model;

import java.util.ArrayList;
import java.util.List;

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
		couplings.addAll(getCouplings());
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
		System.out.println("");
	}
	
	private void executeSubModelsWithEvents() {
		for(Model m : subModels) {
			if(!m.queuedEvents.isEmpty()) {
				m.executeFunctions();
				if(!m.output.isEmpty()) {
					routeOutput(m);
				}
				m.resetInputAndOutput();
			}
		}
	}
	
	private void routeOutput(Model m) {
		debug("routeOutput for " + m);
		couplings.stream()
			.filter(c -> c.FIRST == m)
			.forEach(c -> {
				if(c.LAST == null) {
					debug("Routing output from " + m + " to " + this);
					output.addAll(m.output);
				}
				else {
					debug("Routing output from " + m + " to " + c.LAST);
					InputToken[] routedOutput = m.output.stream().map(o -> (InputToken)o).toArray(InputToken[]::new);
					c.LAST.queueEvents(new InputEvent(CurrentEventQueue.getRealTime(), 0, routedOutput));
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
