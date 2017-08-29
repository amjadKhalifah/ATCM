package util;

import java.util.Set;

import causality.Variable;

public interface PowerSetUtil {
	 Set<? extends Iterable<Variable>>  getPowerSet(Set<Variable> variables);
}