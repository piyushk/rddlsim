/**
 * RDDL: A text display of variable assignments in case no other specific  
 *       visualization is specified.
 * 
 * @author Scott Sanner (ssanner@gmail.com)
 * @version 10/10/10
 *
 **/

package rddl.viz;

import java.util.ArrayList;
import java.util.Map;

import rddl.EvalException;
import rddl.State;
import rddl.RDDL.LCONST;
import rddl.RDDL.PVAR_NAME;

public class GenericScreenDisplay extends StateViz {

	public GenericScreenDisplay() {
		_bSuppressNonFluents = true;
	}

	public GenericScreenDisplay(boolean suppress_nonfluents) {
		_bSuppressNonFluents = suppress_nonfluents;
	}
	
	public boolean _bSuppressNonFluents = false;
	
	public void display(State s, int time) {
		
		System.out.println("\n============================\n" + 
				           "TIME = " + time + 
				           "\n============================\n" +
				           getStateDescription(s));
	}

	//////////////////////////////////////////////////////////////////////

	public String getStateDescription(State s) {
		StringBuilder sb = new StringBuilder();
		
		// Go through all variable types (state, interm, observ, action, nonfluent)
		for (Map.Entry<String,ArrayList<PVAR_NAME>> e : s._hmTypeMap.entrySet()) {
			
			if (_bSuppressNonFluents && e.getKey().equals("nonfluent"))
				continue;
			
			// Go through all variable names p for a variable type
			for (PVAR_NAME p : e.getValue()) 
				try {
					// Go through all term groundings for variable p
					ArrayList<ArrayList<LCONST>> gfluents = s.generateAtoms(p);										
					for (ArrayList<LCONST> gfluent : gfluents)
						sb.append("- " + e.getKey() + ": " + p + 
								(gfluent.size() > 0 ? gfluent : "") + " := " + 
								s.getPVariableAssign(p, gfluent) + "\n");
						
				} catch (EvalException ex) {
					sb.append("- could not retrieve assignment " + s + " for " + p + "\n");
				}
		}
				
		return sb.toString();
	}
}
