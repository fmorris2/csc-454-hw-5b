package framework.model;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import framework.model.event.CurrentEventQueue;
import framework.model.event.DiscreteEvent;
import framework.model.event.EventQueue;
import framework.model.event.TimeAdvanceEvent;
import framework.model.token.output.OutputToken;

public abstract class AtomicModel extends Model {
	public AtomicModel() {
		super();
	}
	
	protected abstract void deltaInt();
	protected abstract void deltaExt();
	protected abstract void deltaConf();
	protected abstract OutputToken[] lambda();
	protected abstract TimeAdvanceEvent generateTimeAdvanceEvent();
	
	@Override
	public void executeFunctions() {
		final boolean TIME_ADVANCE_NEEDED = queuedEvents.stream().anyMatch(e -> e instanceof TimeAdvanceEvent);
		
		if(TIME_ADVANCE_NEEDED) {
			handleLambda();
		}
		
		if(queuedEvents.size() >= 2 && TIME_ADVANCE_NEEDED) {
			deltaConf();
		}
		else if(queuedEvents.size() >= 1 && !TIME_ADVANCE_NEEDED) {
			deltaExt();
		}
		else if(TIME_ADVANCE_NEEDED) {
			deltaInt();
		}
		CurrentEventQueue.discreteTime++;
	}
	
	private void handleLambda() {
		log("output at " + CurrentEventQueue.getTimeString() + " - ");
		output.addAll(Arrays.asList(lambda()));
		output.stream().forEach(o -> System.out.println("\t\t\t"+o.getName()));
	}
	
	public void timeAdvance()
	{
		List<DiscreteEvent> temp = Arrays.stream(EventQueue.get().getElements())
			.collect(Collectors.toList());
		temp.removeIf(event -> event instanceof TimeAdvanceEvent && ((TimeAdvanceEvent)event).getModel().getClass().equals(this.getClass()));
		EventQueue.get().setElements(temp.toArray(new DiscreteEvent[temp.size()]));
		
		DiscreteEvent newTimeAdvance = generateTimeAdvanceEvent();
		EventQueue.get().insert(newTimeAdvance);
		debug("new time advance has been scheduled: " + newTimeAdvance);
	}
}
