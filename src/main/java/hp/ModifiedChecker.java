package hp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import causality.CausalModel;
import causality.EndogenousVariable;
import causality.Variable;
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
		Map<String, Boolean> actualValues = new HashMap<String, Boolean>() {
			{
				put("ST_exo", true);
				put("BT_exo", true);
				put("BT", true);
				put("ST", true);
				put("SH", true);
				put("BS", true);
				put("BH", false);
			}
		};

		model = setvalues(model, actualValues);
	}

	/**
	 * This function checks all possible causes and verify the conditions for
	 * them
	 * 
	 * @param effect
	 * @return
	 */
	public List<Witness> findCause(Variable effect) {
		// holds the result
		List<Witness> proofs = new ArrayList<>();
		// start with all singleton endogenous vars
		// TODO whatever heuristic to rather than brute-forcing should be
		// applied here
		Set<Variable> potentialCauses = model.getEndogenousVars();
		// remove the effect
		potentialCauses.remove(effect);
		potentialCauses.stream().forEach(potentialCause -> {

			if (checkConditionOne(potentialCause, potentialCause.getValue(), effect, effect.getValue())) {
				proofs.addAll(checkConditionTwo(potentialCause, potentialCause.getValue(), effect, effect.getValue()));
			}

		});
		
		
		System.out.println("finding causes "+ proofs);

		return proofs;
	}

	@Override
	public boolean checkConditionOne(Variable cause, boolean causeValue, Variable effect, boolean effectValue) {

		if (cause.getValue() != causeValue)
			return false;
		if (effect.getValue() != effectValue)
			return false;
		System.out.println("condition one is true");
		return true;
	}

	@Override
	public List<Witness> checkConditionTwo(Variable cause, boolean causeValue, Variable effect, boolean effectValue) {
		// AC2 (𝑎^𝑚): There is a set 𝑊 ⃗ of variables in 𝑉 and a setting
		// 𝑥 ⃗′ of the variables in 𝑋 ⃗ such that if (𝑀,𝑢 ⃗ )⊨𝑊 ⃗=𝑤 ⃗,
		// then (𝑀,𝑢 ⃗ )⊨[𝑋 ⃗ ←𝑥 ⃗′,𝑊 ⃗←𝑤 ⃗ ]¬𝜑

		// three options to get the powerset
		// remove x and y from v before powerset
		Set<Variable> possibleWElements = model.getEndogenousVars();
		possibleWElements.remove(cause);
		possibleWElements.remove(effect);

		Set<Set<Variable>> gPowerSet = PowerSetUtil.getPowerSetUsingGuava(possibleWElements);
		// gPowerSet.forEach(name -> System.out.println(name));

		// Set<Set<Variable>> recPowerSet =
		// PowerSetUtil.getPowerSetUsingRec(possibleWElements);
		// recPowerSet.forEach(name -> System.out.println(name));

		// apparently the worst performance
		// Set<UnsortedSetIterable<Variable>> ecPowerSet =
		// PowerSetUtil.getPowerSetUsingEC(possibleWElements);
		// ecPowerSet.forEach(name -> System.out.println(name));

		// this will check but-for of x,y for all W
		return checkButForTopDown(cause, effect, gPowerSet);
	}

	@Override
	public boolean checkConditionThree() {
		// TODO To implemnnet later
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

	/**
	 * this functions check but for of the inputs 1- set the value of the
	 * potential cause 2- recursively get the value of each variable in the
	 * formula
	 * 
	 * @getval@when getting the value of a variable: a) if it is in W then
	 *              return the actual value b) else evaluate the formula --
	 *              Possible optimization: if it is not going to be effected
	 *              return its value. To evaluate a formula redo @getval@ for
	 *              each varible 3- check but-for
	 * @param x
	 * @param y
	 * @param wPowerSet
	 * @return
	 */
	public List<Witness> checkButForTopDown(Variable x, Variable y, Set<? extends Iterable<Variable>> wPowerSet) {
		List<Witness> proofs = new ArrayList<>();
		// 1
		x.setValue(!x.getValue());
		boolean yOrigianl = y.getValue();
		wPowerSet.stream().forEach(elem -> {// for one possibility of a w
			// TODO think of the optimizations here.. we can filter the
			// varaibles that should be interveined based on the cause and the
			// effect
			// intervin in the model and change value of x and others and fix w
			// 2
			boolean yValue = ((EndogenousVariable) y).evaluate(x, (Set<Variable>) elem);
			// 3
			if (yValue != yOrigianl) {// the result changes
//				System.out.println(x.getName() + "=" + !x.getValue() + " is a cause of " + y.getName() + "=" + yOrigianl
//						+ " with witness " + elem);
				proofs.add(new Witness(x, (Set<Variable>) elem));
			}

		});

		// reset x
		x.setValue(!x.getValue());

		return proofs;
	}

}
