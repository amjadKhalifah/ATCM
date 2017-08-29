package hp;

import java.util.ArrayList;
import java.util.List;

import causality.CausalModel;
import causality.Variable;
import util.PowerSetUtil;

/**
 * @author Ibrahim Amjad
 *
 */
public abstract class HPChecker {
	
	 /**
	 * we have different implementations of how to get the powerset, so this will be set by the caller 
	 */
	PowerSetUtil powersetUtil; 
	abstract CausalModel getCausalModel();

	abstract boolean checkConditionOne(Variable cause, boolean causeValue, Variable effect, boolean effectValue);

	abstract List<Witness> checkConditionTwo(Variable cause, boolean causeValue, Variable effect, boolean effectValue);

	abstract boolean checkConditionThree();

	abstract List<Witness> findCause(Variable effect);

	
	/** This method checks if a varibale is cause of other by checking the first and second condifion
	 * @param cause
	 * @param causeValue
	 * @param effect
	 * @param effectValue
	 * @return a list of witnesses or empty array in case @cause is not a cause of @effect
	 */
	public List<Witness> isCause(Variable cause, boolean causeValue, Variable effect, boolean effectValue) {
		if (checkConditionOne(cause, causeValue, effect, effectValue)){
			return checkConditionTwo(cause, causeValue, effect, effectValue);
		}
		return new ArrayList<Witness>();
		
	}

	
	
}
