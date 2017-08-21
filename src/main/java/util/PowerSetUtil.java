package util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.collections.api.set.MutableSet;
import org.eclipse.collections.api.set.UnsortedSetIterable;
import org.eclipse.collections.impl.set.mutable.UnifiedSet;

import com.google.common.collect.Sets;

import causality.Variable;


public class PowerSetUtil {

	// for the first implementation we need all the combinations of the
	// variables, which is the powerset
	// should be
	// 2powN//https://www.quora.com/What-is-the-most-efficient-algorithm-to-calculate-a-power-set
	// option one to use for the power set generation
	public static Set<Set<Variable>> getPowerSetUsingGuava(Set<Variable> variables) {
		// this method is limited to 30 vars
		Set<Set<Variable>> variablePowerSet = Sets.powerSet(variables);

		return variablePowerSet;
	}

	// option two for the powerset generation
	// adapted from
	// https://stackoverflow.com/questions/1670862/obtaining-a-powerset-of-a-set-in-java
	public static Set<Set<Variable>> getPowerSetUsingRec(Set<Variable> originalSet) {
		Set<Set<Variable>> sets = new HashSet<Set<Variable>>();
		if (originalSet.isEmpty()) {
			sets.add(new HashSet<Variable>());
			return sets;
		}
		List<Variable> list = new ArrayList<Variable>(originalSet);
		Variable head = list.get(0);
		Set<Variable> rest = new HashSet<Variable>(list.subList(1, list.size()));
		for (Set<Variable> set : getPowerSetUsingRec(rest)) {
			Set<Variable> newSet = new HashSet<Variable>();
			newSet.add(head);
			newSet.addAll(set);
			sets.add(newSet);
			sets.add(set);
		}
		return sets;
	}

	// option 3 using eclipse collections
	public static MutableSet<UnsortedSetIterable<Variable>> getPowerSetUsingEC(Set<Variable> originalSet) {
		UnifiedSet<Variable> set = new UnifiedSet<>(originalSet);
		return set.powerSet();

	}

}
