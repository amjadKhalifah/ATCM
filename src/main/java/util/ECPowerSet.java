package util;

import java.util.Set;

import org.eclipse.collections.impl.set.mutable.UnifiedSet;


import causality.Variable;


public class ECPowerSet implements PowerSetUtil {



	// option 3 using eclipse collections
	
	@Override
	public Set<? extends Iterable<Variable>> getPowerSet(Set<Variable> originalSet) {
		UnifiedSet<Variable> set = new UnifiedSet<>(originalSet);
		return set.powerSet();

	}



}
