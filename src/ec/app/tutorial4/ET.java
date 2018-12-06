package ec.app.tutorial4;

import ec.EvolutionState;
import ec.Problem;
import ec.gp.ADFStack;
import ec.gp.GPData;
import ec.gp.GPIndividual;
import ec.gp.GPNode;

public class ET extends GPNode {
	
	public String toString() {
		return "ET";
	}

	public int expectedChildren() {
		return 0;
	}

	public void eval(final EvolutionState state, final int thread, final GPData input, final ADFStack stack,
			final GPIndividual individual, final Problem problem) {
		DoubleData rd = ((DoubleData) (input));
		rd.x =((MultiValuedRegression)problem).executionTime;
//		double[] c = ((MultiValuedRegression) problem).currentValue;
//		if (c.length >= 1)
//			rd.x = ((MultiValuedRegression) problem).currentValue[0];
//		else
//			rd.x = 0;
	}
}

