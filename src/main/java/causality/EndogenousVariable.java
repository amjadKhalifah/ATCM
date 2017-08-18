package causality;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jexl3.JexlBuilder;
import org.apache.commons.jexl3.JexlContext;
import org.apache.commons.jexl3.JexlEngine;
import org.apache.commons.jexl3.JexlExpression;
import org.apache.commons.jexl3.MapContext;

import mef.formula.Formula;

public class EndogenousVariable extends Variable {
	private Formula formula;
	private double probability;
	private static  final JexlEngine jexl = new JexlBuilder().cache(512).strict(true).silent(false).create();

	// check with simon if we can fill them earlier
	private String formulaStr="";
	// list that contains the variables that affect this var
	private Set<Variable> parents = new HashSet<>();

	public String getFormulaStr() {
		return formulaStr;
	}
	
	public String getFormulaExpression() {
		//TODO do it once
		return formulaStr.replaceAll("and", "&&").replaceAll("or", "||").replaceAll("-", "!");
	}

	public void setFormulaStr(String formulaStr) {
		this.formulaStr = formulaStr;
	}

	public Set<Variable> getParents() {
		return parents;
	}

	public void setParents(Set<Variable> parents) {
		this.parents = parents;
	}

	public EndogenousVariable(String name, Formula formula) {
		super(name);
		setFormula(formula);
	}

	public EndogenousVariable(String name, Formula formula, double probability) {
		super(name);
		setFormula(formula);
		this.probability = probability;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;
		if (!super.equals(o))
			return false;

		EndogenousVariable that = (EndogenousVariable) o;

		return formula != null ? formula.equals(that.formula) : that.formula == null;
	}

	@Override
	public int hashCode() {
		int result = super.hashCode();
		result = 31 * result + (formula != null ? formula.hashCode() : 0);
		return result;
	}

	public Formula getFormula() {
		return formula;
	}

	public void setFormula(Formula formula) {
		this.formula = formula;
		if  (formula != null) {
			this.formulaStr = formula.print(parents);
		}

	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	@Override
	public String toString() {

		return super.toString() + " [formula=" + formulaStr + "]";
	}

	/**
	 * Evaluates the value of endo variable given a potential cause and set W
	 * 
	 * @return
	 */
	public boolean evaluate(Variable x, Set<Variable> w) {
		System.out.println("evaluating "+getName()+" parents are "+ parents );
		// under the assumptions it was already set to its reverse
		if (this == x) {
			return this.getValue();
		}
		if (w.contains(this)) {// TODO check that this works
			return this.getValue();
		}
		if (this.getFormula() instanceof ExogenousVariable) {// assuming this is
																// only the form
																// of ST= st_exo
			return ((ExogenousVariable) this.getFormula()).getValue();
		}

		Map<String, Object> parentValues = new HashMap<>();

		for (Variable variable : parents) {
			
			if (variable instanceof ExogenousVariable)
				parentValues.put(variable.getName(), variable.getValue());
			else if (variable instanceof EndogenousVariable)
				parentValues.put(variable.getName(), ((EndogenousVariable) variable).evaluate(x, w));
		}
		
		
		JexlExpression expression = jexl.createExpression(this.getFormulaExpression());
		JexlContext parentCtxt = new MapContext(parentValues);
		System.out.println("--"+getName()+" --"+this.getFormulaExpression()+" --"+ parentValues );

		boolean result = ((Boolean) expression.evaluate(parentCtxt));
		System.out.println(getName()+" value: "+ result);
		return result;
	}

}
