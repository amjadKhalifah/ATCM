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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import hp.TopDownChecker;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.binding.BooleanExpression;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import mef.formula.BasicBooleanOperator;
import mef.formula.Formula;

public class EndogenousVariable extends Variable {
	
	private static final Logger logger = LogManager.getLogger(EndogenousVariable.class);
	private final static BooleanProperty TRUE = new SimpleBooleanProperty(true);
	private static final JexlEngine jexl = new JexlBuilder().cache(512).strict(true).silent(false).create();
	private Formula formula;
	private double probability;

	// check with simon if we can fill them earlier
	private String formulaStr = "";
	private String formulaExpression="";
	// list that contains the variables that affect this var
	private Set<Variable> parents = new HashSet<>();

	/**
	 * This is used in the bindable counterfactual analysis this variable holds
	 * the formula and it is bound to the bindable variable
	 */
	private BooleanBinding bindableFormula;

	public String getFormulaStr() {
		return formulaStr;
	}

	public String getFormulaExpression() {
		return formulaExpression;
	}

	public void setFormulaStr(String formulaStr) {
		this.formulaStr = formulaStr;
	}

	public Set<Variable> getParents() {
		return parents;
	}

	public Variable getParentByName(String name) {
		Variable var = parents.stream().filter(v -> name.equals(v.getName())).findAny().orElse(null);
		return var;
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
		if (formula != null) {
			this.formulaStr = formula.print(parents);
			this.formulaExpression= formulaStr.replaceAll("and", "&&").replaceAll("or", "||").replaceAll("not", "!");
			// TODO set the bindable formula based on the string, this should
			// rather be done during the parsing and the creation
			// the formula; could not do it
			// the bindable formula of the simple exo formulas is an and
			// with true
			setBindableFormulaFromStr();

		}

	}

	private void setBindableFormulaFromStr() {
		if (getFormula() instanceof ExogenousVariable) {
			// e.g BT = BT_exo
			ExogenousVariable exo = (ExogenousVariable) this.getFormula();
			// in simple exo formula we and it wit true
			this.setBindableFormula(Bindings.and(getProp(exo.getName()), TRUE));

		} else if (getFormula() instanceof BasicBooleanOperator) {

			// convert the string representation in bindable structure
			// e.gs (ST) (BH or SH) (BT and (not SH))
			// trim, remove parantheses, split by space and loop with cases
			
			String clone = new String(formulaStr);
			clone = clone.trim().replaceAll("\\(", "").replaceAll("\\)", "");
			String[] tokens = clone.split(" ");
			// case of simple x=y
			if (tokens.length == 1) {
				this.setBindableFormula(Bindings.and(getProp(tokens[0]), TRUE));
			} else // should be called for other than zero
				this.setBindableFormula(createFormula(tokens, 1));
		}
		// we already calculated the formula
		// now we set the value to the formula and bind it//??????
		// there are cases with the preemption where the formula was already bound so we reset it by unbinding it
		this.getBindableProperty().unbind();
		this.bindableProperty.set(this.getBindableFormula().getValue());
		this.getBindableProperty().bind(bindableFormula);
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	/**
	 * Evaluates the value of endo variable given a potential cause and set W
	 * 
	 * @return
	 */
	public boolean evaluate(Variable x, Set<Variable> w) {
		 logger.debug("evaluating "+getName()+" parents are "+ parents );
		// under the assumptions it was already set to its reverse
		if (this == x) {
			return this.getValue();
		}
		if (w.contains(this)) {
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
		boolean result = ((Boolean) expression.evaluate(parentCtxt));
		logger.debug("--"+getName()+" --"+this.getFormulaExpression()+" --"+ parentValues );
		logger.debug(getName()+" value: "+ result);
		return result;
	}

	public BooleanBinding getBindableFormula() {
		return bindableFormula;
	}

	public void setBindableFormula(BooleanBinding bindableFormula) {
		this.bindableFormula = bindableFormula;
	}

	/**
	 * This is used whenever we want this value to be updated and update others
	 */
	public void bind() {
		getBindableProperty().bind(bindableFormula);
	}

	/**
	 * This is used when we want to prevent intervening on this variable e.g.
	 * when in W or in x
	 * 
	 */
	public void unBind() {
		getBindableProperty().unbind();
	}

	/**
	 * should i only call it for operators?
	 * 
	 * @param tokens
	 * @param j
	 * @return
	 */
	public BooleanBinding createFormula(String[] tokens, int j) {
		String token = tokens[j];
		assert (token.equals("or") || token.equals("and"));
		if (j < tokens.length - 2) // should exclude the last operator
		{
			
			if (token.equals("or"))
				return Bindings.or(getProp(tokens[j - 1]), createFormula(tokens, j + 2));
			if (token.equals("and"))
				return Bindings.and(getProp(tokens[j - 1]), createFormula(tokens, j + 2));

		}

		// this should happen in the last operator
		if (token.equals("or"))
			return Bindings.or(getProp(tokens[j - 1]), getProp(tokens[j + 1]));
		else
			return Bindings.and(getProp(tokens[j - 1]), getProp(tokens[j + 1]));

	}

	/**
	 * gets the property and negate it if needed
	 * 
	 * @param name
	 * @param prop
	 * @return
	 */
	private BooleanExpression getProp(String name) {
		BooleanExpression operand = getParentByName(name.replace("!", "")).getBindableProperty();
		if (name.startsWith("!")) {
			return operand.not();
		}
		return operand;
	}

	@Override
	public String toString() {

		return super.toString() + " [formula=" + formulaStr + "]";
	}

}
