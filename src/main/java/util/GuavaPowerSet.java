package util;

import java.util.Set;

import com.google.common.collect.Sets;

import causality.Variable;


public class GuavaPowerSet implements PowerSetUtil {

	// for the first implementation we need all the combinations of the
		// variables, which is the powerset
		// should be
		// 2powN//https://www.quora.com/What-is-the-most-efficient-algorithm-to-calculate-a-power-set
		// option one to use for the power set generation
	
	@Override
	public Set<Set<Variable>> getPowerSet(Set<Variable> variables) {
		// this method is limited to 30 vars
		Set<Set<Variable>> variablePowerSet = Sets.powerSet(variables);

		return variablePowerSet;
	}
	

	



}
