package framework.model.coupling;

import framework.model.Model;

public class Coupling {
	public final Model FIRST;
	public final Model LAST;
	public final CouplingType TYPE;
	
	public Coupling(Model first, Model last, CouplingType type) {
		FIRST = first;
		LAST = last;
		TYPE = type;
	}
}
