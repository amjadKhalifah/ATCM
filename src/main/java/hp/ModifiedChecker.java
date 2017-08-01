package hp;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import causality.CausalModel;
import causality.Variable;

/**
 * Will try some experiments with HP implementation here // we want to check of
 * x=x is an actual cause of y=y
 * 
 * @author Ibrahim Amjad
 *
 */
public class ModifiedChecker implements HPChecker {
	private CausalModel model;

	public ModifiedChecker(CausalModel model) {
		this.model = model;
		//TODO  get the model and set its actual values or it should be set already in the model
		// hardcode the values in the model now 
		model = setvalues(model, new HashMap<>());
	}

	@Override
	public boolean checkConditionOne(String var1, String var2, boolean value1, boolean value2) {
	
		// 2- check their values as indicated
		List<String> names = new ArrayList<>();
		names.add(var1);
		names.add(var2);
		
		Set<Variable> variables = getCausalModel().getVariablesByName(names);
		// TODO check the values in a better way
		for (Variable variable : variables) {
			if (variable.getName().equals(var1))
				if (variable.getValue()!= value1)
					return false;
			if (variable.getName().equals(var2))
				if (variable.getValue()!= value2)
					return false;
		}
		
				
		return true;
	}

	@Override
	public boolean checkConditionTwo() {

		return false;
	}

	@Override
	public boolean checkConditionThree() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public CausalModel getCausalModel() {
		return model;
	}
	
	private CausalModel setvalues(CausalModel model,HashMap<String, Boolean> values){
		
		for (Variable v : model.getVariables()) {
			//TODO set the values here or outside
			v.setValue(true);		
			System.out.println(v);
		}
		
		return model;
		
	}

}
