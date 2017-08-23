package hp;

import java.util.List;

import causality.CausalModel;
import causality.Variable;

public interface HPChecker {
	CausalModel getCausalModel();	
	
	boolean checkConditionOne(Variable cause, boolean causeValue, Variable effect, boolean effectValue);
	List<Witness> checkConditionTwo(Variable cause, boolean causeValue, Variable effect, boolean effectValue);
	boolean checkConditionThree();
	 List<Witness> findCause(Variable effect);

}
