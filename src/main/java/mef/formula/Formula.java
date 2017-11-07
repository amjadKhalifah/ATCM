package mef.formula;


import causality.EndogenousVariable;
import causality.ExogenousVariable;
import causality.Variable;
import org.dom4j.Element;

import java.util.List;

public abstract class Formula {
    public abstract Element toXML(Element element);

    /**
     * Convert formula to string; does not extend variables
     * @return formula as string
     */
    public String print() {
        // ENDO
        if (this instanceof EndogenousVariable) {
            EndogenousVariable endo = (EndogenousVariable) this;
            String formula = endo.getName() + " = ";
            // formula is defined by exogenous variable only
            if (endo.getFormula() instanceof ExogenousVariable) {
                ExogenousVariable exo = (ExogenousVariable) endo.getFormula();
                return formula + exo.getName();
            }
            // formula is more complex
            else if (endo.getFormula() instanceof BasicBooleanOperator) {
                BasicBooleanOperator operator = (BasicBooleanOperator) endo.getFormula();
                return formula + operator.print();
            }
        }
        // EXO
        else if (this instanceof ExogenousVariable) {
            return ((ExogenousVariable) this).getName();
        }

        // OPERATOR
        // only available for BasicBooleanOperator and ImplyOperator
        else if (this instanceof BasicBooleanOperator || this instanceof ImplyOperator) {
            return this.printInnerFormula();
        }
        return null;
    }

    /*
    The variables inside a formula shall not be extended. Therefore, a separate function is defined that is applied to
    inner formulas
     */
    private String printInnerFormula() {
        // VARIABLE
        if (this instanceof Variable) {
            // return name only
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
                if (formulasStr.equals(""))
                    // first formula does not need an operator on the left
                    formulasStr += formula.printInnerFormula();
                else
                    formulasStr += " " + operator.getType() + " " + formula.printInnerFormula();
            }

            return "(" + formulasStr + ")";
        } else if (this instanceof ImplyOperator) {
            ImplyOperator imply = (ImplyOperator) this;
            Formula formulaLeft = imply.getLeftFormula();
            Formula formulaRight = imply.getRightFormula();
            // print left and right formula and connect them with "->" in between
            String formulaStr = formulaLeft.printInnerFormula() + " -> " + formulaRight.printInnerFormula();

            return "(" + formulaStr + ")";
        }
        return null;
    }

    /**
     * Finds a variable by name. Returns null, if no variable with the passed name can be found. Assumes that each
     * variable has a unique name.
     * @param name
     * @return
     */
    public Variable findVariableByName(String name) {
        Variable variable = null;
        if (this instanceof Variable && ((Variable) this).getName().equals(name)) {
            // if this is instance of Variable, we just need to check, if this Variable has the passed name
            variable = (Variable) this;
        } else if (this instanceof BasicBooleanOperator) {
            /*
            if this is an operator, we need to search through all the formulas in this operator and check, if we
            find the veriable there
             */
            BasicBooleanOperator basicBooleanOperator = (BasicBooleanOperator) this;
            for (Formula formula : basicBooleanOperator.getFormulas()) {
                variable = formula.findVariableByName(name);
                if (variable != null)
                    break;
            }
        } else if (this instanceof ImplyOperator) {
            /*
            if this is an ImplyOperator, we need to check if we find the variable in the left or right side
             */
            ImplyOperator implyOperator = (ImplyOperator) this;
            variable = implyOperator.getLeftFormula().findVariableByName(name);
            if (variable == null)
                variable = implyOperator.getRightFormula().findVariableByName(name);
        }
        return variable;
    }

    /**
     * Returns whether a formula contains the specified Variable. Only works for formulas using BasicBooleanOperators
     * or ImplyOperators.
     * @param variable
     * @return
     */
    public boolean containsVariable(Variable variable) {
        if (this instanceof EndogenousVariable) {
            EndogenousVariable endogenousVariable = (EndogenousVariable) this;
            return endogenousVariable.equals(variable) ||
                    (endogenousVariable.getFormula() != null && endogenousVariable.getFormula().containsVariable(variable));
        } else if (this instanceof ExogenousVariable) {
            ExogenousVariable exogenousVariable = (ExogenousVariable) this;
            return exogenousVariable.equals(variable);
        } else if (this instanceof BasicBooleanOperator) {
            BasicBooleanOperator basicBooleanOperator = (BasicBooleanOperator) this;
            return basicBooleanOperator.getFormulas().stream().anyMatch(f -> f != null && f.containsVariable(variable));
        } else if (this instanceof ImplyOperator) {
            ImplyOperator implyOperator = (ImplyOperator) this;
            return (implyOperator.getLeftFormula() != null && implyOperator.getLeftFormula().containsVariable
                    (variable)) || (implyOperator.getRightFormula() != null && implyOperator.getRightFormula()
                    .containsVariable(variable));
        }
        return false;
    }
}
