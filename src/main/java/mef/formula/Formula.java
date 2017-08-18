package mef.formula;

import causality.EndogenousVariable;
import causality.ExogenousVariable;
import causality.Variable;
import mef.formula.BasicBooleanOperator.OperatorType;

import org.dom4j.Element;

import java.util.List;
import java.util.Set;

public abstract class Formula {
	public abstract Element toXML(Element element);
	/**
	 * Convert formula to string; does not extend variables
	 * 
	 * @return formula as string
	 */
	public String print(Set<Variable> parents) {
		// ENDO
		
		
		if (this instanceof EndogenousVariable) {
			EndogenousVariable endo = (EndogenousVariable) this;
			String formula = endo.getName() + " = ";
			// formula is defined by exogenous variable only
			if (endo.getFormula() instanceof ExogenousVariable) {
				ExogenousVariable exo = (ExogenousVariable) endo.getFormula();
				parents.add(exo);
				return formula + exo.getName();
			}
			// formula is more complex
			else if (endo.getFormula() instanceof BasicBooleanOperator) {
				BasicBooleanOperator operator = (BasicBooleanOperator) endo.getFormula();
				return formula + operator.print(parents);
			}
		}
		// EXO
		else if (this instanceof ExogenousVariable) {
			//TODO ??
			parents.add((ExogenousVariable) this);
			return ((ExogenousVariable) this).getName();
		}

		// OPERATOR
		// only available for BasicBooleanOperator and ImplyOperator
		else if (this instanceof BasicBooleanOperator || this instanceof ImplyOperator) {
			return this.printInnerFormula(parents);
		}
		return null;
	}

	/*
	 * The variables inside a formula shall not be extended. Therefore, a
	 * separate function is defined that is applied to inner formulas
	 */
	private String printInnerFormula(Set<Variable> parents) {
		// VARIABLE
		if (this instanceof Variable) {
			// return name only
			parents.add((Variable) this);
			return ((Variable) this).getName();
		}

		// OPERATOR
		// only available for BasicBooleanOperator and ImplyOperator
		else if (this instanceof BasicBooleanOperator) {
			BasicBooleanOperator operator = (BasicBooleanOperator) this;
			List<Formula> formulas = operator.getFormulas();
			String formulasStr = "";
			// print all formulas and connect them with the respective operator
			for (Formula formula : formulas) {
				if (formulasStr.equals("")){
					// first formula does not need an operator on the left
					 if (operator.getType()== OperatorType.not)
						 formulasStr += operator.getType()+" ";
					formulasStr += formula.printInnerFormula(parents);
					}
				else
					formulasStr += " " + operator.getType() + " " + formula.printInnerFormula(parents);
			}

			return "(" + formulasStr + ")";
		} else if (this instanceof ImplyOperator) {
			ImplyOperator imply = (ImplyOperator) this;
			Formula formulaLeft = imply.getLeftFormula();
			Formula formulaRight = imply.getRightFormula();
			// print left and right formula and connect them with "->" in
			// between
			String formulaStr = formulaLeft.printInnerFormula(parents) + " -> " + formulaRight.printInnerFormula(parents);

			return "(" + formulaStr + ")";
		}
		return null;
	}

	

}
