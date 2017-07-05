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
}
