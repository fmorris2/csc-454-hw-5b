package machinery.parts;

import framework.model.token.input.InputToken;
import framework.model.token.output.OutputToken;
import machinery.drill_model.DrillModel;

public class UndrilledMetalDisk extends InputToken<DrillModel> implements OutputToken {

	@Override
	public String getName() {
		return "Undrilled Metal Disk";
	}

}
