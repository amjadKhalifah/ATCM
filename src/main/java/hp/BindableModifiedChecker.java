package hp;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
public class BindableModifiedChecker extends HPChecker {
	
	private CausalModel model;
	private static final Logger logger = LogManager.getLogger(BindableModifiedChecker.class);

	public BindableModifiedChecker(CausalModel model, PowerSetUtil powerSet) {
		this.model = model;
		this.powersetUtil=powerSet;

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

			if (checkConditionOne(potentialCause, potentialCause.getBindableProperty().get(), effect,
					effect.getBindableProperty().get())) {
				proofs.addAll(checkConditionTwo(potentialCause, potentialCause.getBindableProperty().get(), effect,
						effect.getBindableProperty().get()));
			}

		});

		logger.info("finding causes " + proofs.get(0));

		return proofs;
	}

	@Override
	public boolean checkConditionOne(Variable cause, boolean causeValue, Variable effect, boolean effectValue) {
		
		logger.info("condition one: checking value of cause {} {}", cause, causeValue);
		logger.info("condition one: checking value of effect {} {}", effect, effectValue);
		
		if (cause.getBindableProperty().get() != causeValue)
			return false;
		if (effect.getBindableProperty().get() != effectValue)
			return false;
		logger.info("condition one is true");
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

		Set<? extends Iterable<Variable>> gPowerSet = powersetUtil.getPowerSet(possibleWElements);


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

	public void setExovalues(Map<String, Boolean> values) {

		for (Variable v : model.getExogenousVars()) {
			v.setBindablePropertyValue(values.get(v.getName()));
		}

	

	}

	/**
	 * this function checks the but-for with bindable nature
	 * 
	 * @param x
	 * @param y
	 * @param wPowerSet
	 * @return
	 */
	public List<Witness> checkButForTopDown(Variable x, Variable y, Set<? extends Iterable<Variable>> wPowerSet) {
		List<Witness> proofs = new ArrayList<>();

		boolean yOrigianl = y.getBindableProperty().get();
		boolean xOrigianl = x.getBindableProperty().get();
		wPowerSet.stream().forEach(elem -> {// for one possibility of a w
			// TODO think of the optimizations here.. we can filter the
			// first should unbind all the vars in W
			unbindVars(elem);

			// varaibles that should be interveined based on the cause and the
			// effect
			// intervin in the model and change value of x and others and fix w
			((EndogenousVariable) x).unBind();// so that it wont be affected by
												// others
			x.setBindablePropertyValue(!xOrigianl);

			boolean yValue = ((EndogenousVariable) y).getBindableProperty().get();

			if (yValue != yOrigianl) {// the result changes
				 logger.debug(x.getName() + "=" + !x.getValue() + " is a cause of " + y.getName() + "=" + yOrigianl
				 + " with witness " + elem);
				proofs.add(new Witness(x, (Set<Variable>) elem));
			}

			// reset and clean up
			bindVars(elem);

			x.setBindablePropertyValue(xOrigianl);
			((EndogenousVariable) x).bind();// so that it wont be affected by
			// others
		});
		
		return proofs;
	}

	private void unbindVars(Iterable<Variable> elem) {

		elem.forEach(var -> ((EndogenousVariable) var).unBind());
	}

	private void bindVars(Iterable<Variable> elem) {

		elem.forEach(var -> ((EndogenousVariable) var).bind());
	}



}
