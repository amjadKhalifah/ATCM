package util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import causality.Variable;


public class RecursionPowerSet implements PowerSetUtil {

	

	// option two for the powerset generation
	// adapted from
	// https://stackoverflow.com/questions/1670862/obtaining-a-powerset-of-a-set-in-java
	@Override
	public Set<? extends Set<Variable>>  getPowerSet(Set<Variable> originalSet) {
		Set<Set<Variable>> sets = new HashSet<Set<Variable>>();
		if (originalSet.isEmpty()) {
			sets.add(new HashSet<Variable>());
			return sets;
		}
		List<Variable> list = new ArrayList<Variable>(originalSet);
		Variable head = list.get(0);
		Set<Variable> rest = new HashSet<Variable>(list.subList(1, list.size()));
		for (Set<Variable> set : getPowerSet(rest)) {
			Set<Variable> newSet = new HashSet<Variable>();
			newSet.add(head);
			newSet.addAll(set);
			sets.add(newSet);
			sets.add(set);
		}
		return sets;
	}



}
