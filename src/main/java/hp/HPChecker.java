package hp;

import causality.CausalModel;

public interface HPChecker {
	CausalModel getCausalModel();	
	//TODO add params
	boolean checkConditionOne(String var1, String var2, boolean value1, boolean value2);
	boolean checkConditionTwo(String cause, String effect, boolean causeValue, boolean effectValue);
	boolean checkConditionThree();
	

}
