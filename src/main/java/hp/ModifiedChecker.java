package hp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.set.UnsortedSetIterable;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;

import com.google.common.collect.Sets;

import causality.CausalModel;
import causality.EndogenousVariable;
import causality.ExogenousVariable;
import causality.Variable;
import mef.formula.Formula;
import util.PowerSetUtil;

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
		// TODO get the model and set its actual values or it should be set
		Map<String, Boolean> actualValues = new HashMap<String, Boolean>()
		{{
		     put("ST_exo", true);
		     put("BT_exo", true);
		     put("BT", true);
		     put("ST", true);
		     put("SH", true);
		     put("BS", true);
		     put("BH", false);
		}};
		
		model = setvalues(model, actualValues);
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
				if (variable.getValue() != value1)
					return false;
			if (variable.getName().equals(var2))
				if (variable.getValue() != value2)
					return false;
		}
		System.out.println("condition one is true");
		return true;
	}

	@Override
	public boolean checkConditionTwo(String cause, String effect, boolean causeValue, boolean effectValue) {
		// AC2 (𝑎^𝑚): There is a set 𝑊 ⃗ of variables in 𝑉 and a setting
		// 𝑥 ⃗′ of the variables in 𝑋 ⃗ such that if (𝑀,𝑢 ⃗ )⊨𝑊 ⃗=𝑤 ⃗,
		// then (𝑀,𝑢 ⃗ )⊨[𝑋 ⃗ ←𝑥 ⃗′,𝑊 ⃗←𝑤 ⃗ ]¬𝜑
		Variable x = model.getVariableByName(cause);
		Variable y = model.getVariableByName(effect);

		
		// three options to get the powerset
		// remove x and y from v before powerset 
		Set<Variable> possibleWElements = model.getEndogenousVars();
		possibleWElements.remove(x);
		possibleWElements.remove(y);
		
		Set<Set<Variable>> gPowerSet = PowerSetUtil.getPowerSetUsingGuava(possibleWElements);
//		gPowerSet.forEach(name -> System.out.println(name));
		
//		Set<Set<Variable>> recPowerSet = PowerSetUtil.getPowerSetUsingRec(possibleWElements);
//		recPowerSet.forEach(name -> System.out.println(name));
		
		
		// apparently the worst performance
//		Set<UnsortedSetIterable<Variable>> ecPowerSet = PowerSetUtil.getPowerSetUsingEC(possibleWElements);
//		ecPowerSet.forEach(name -> System.out.println(name));

		
		//TODO first impl is a brute force of the w
		checkButForTopDown(x, y, model, gPowerSet);
		
		return false;
	}
	
	
	@Override
	public boolean checkConditionThree() {
		// x should be minimal
		return false;
	}


	


	@Override
	public CausalModel getCausalModel() {
		return model;
	}

	private CausalModel setvalues(CausalModel model, Map<String, Boolean> values) {

		for (Variable v : model.getVariables()) {
			v.setValue(values.get(v.getName()));
		}
		return model;

	}
	
	

	
	
	// simple top down implemenattion without any optimizataion 
	public boolean checkButForTopDown(Variable x, Variable y, CausalModel model, Set<? extends Iterable<Variable>> wPowerSet){
		
		/* 1- set the value of the potential cause
		 * 2- recursively get the value of each variable in the formula 
		 * @getval@when getting the value of a variable: 
		 * 	a) if it is in W then return the actual value
		 *  b) else evaluate the formula 
		 *  -- Possible optimization: if it is not going to be effected return its value.  
		* To evaluate a formula redo @getval@ for each varible 
		* 3- check but-for
		*/
		
		// 1
		 x.setValue(!x.getValue());
		boolean yOrigianl = y.getValue();
		wPowerSet.stream().forEach(elem->{// for one possibility of a w
//			System.out.println("checking w: " + elem);
			//TODO think of the optimizations here.. we can filter the varaibles that should be interveined based on the cause and the effect 
			// intervin in the model and change value of x and others and fix w
			//2
			boolean yValue = ((EndogenousVariable)y).evaluate(x, (Set<Variable>)elem);
			
			if (yValue!=yOrigianl){// the result changes
				System.out.println(x.getName() +"="+!x.getValue()+" is a cause of "+y.getName() +"="+yOrigianl+" with witness "+ elem );
			}
			
		});
		
		return false;
	}

	
	public boolean checkButForBottomUP(Variable x, Variable y, CausalModel model, Set<Set<Variable>> wPowerSet){
		/* 1- set the value of the potential cause
		 * 2- recursively get the value of each variable in the formula 
		 * @getval@when getting the value of a variable: 
		 * 	a) if it is in W then return the actual value
		 *  b) else evaluate the formula 
		 *  -- Possible optimization: if it is not going to be effected return its value.  
		* To evaluate a formula redo @getval@ for each varible 
		* 3- check but-for
		*/
		
		// 1
		 x.setValue(!x.getValue());
		boolean yOrigianl = y.getValue();
		wPowerSet.stream().forEach(elem->{// for one possibility of a w
//			System.out.println("checking w: " + elem);
			//TODO think of the optimizations here.. we can filter the varaibles that should be interveined based on the cause and the effect 
			// intervin in the model and change value of x and others and fix w
			//2
			boolean yValue = ((EndogenousVariable)y).evaluate(x, (Set<Variable>)elem);
			
			if (yValue!=yOrigianl){// the result changes
				System.out.println(x.getName() +"="+!x.getValue()+" is a cause of "+y.getName() +"="+yOrigianl+" with witness "+ elem );
			}
			
		});
		
		return false;
	}

}
