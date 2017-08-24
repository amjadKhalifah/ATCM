package mef.formula;

import org.dom4j.Element;

import java.util.List;

public class BasicBooleanOperator extends BooleanOperator{

    public enum OperatorType {
        and, or, not, xor, iff, nand, nor
    }

    private OperatorType type;
    private List<Formula> formulas;

    public BasicBooleanOperator(OperatorType type, List<Formula> formulas) {
        this.type = type;
        this.formulas = formulas;
    }

    @Override
    public Element toXML(Element element) {
        Element operator = element.addElement(this.getType().toString());
        formulas.forEach(f -> f.toXML(operator));
        return operator;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BasicBooleanOperator that = (BasicBooleanOperator) o;

        if (type != that.type) return false;
        return formulas != null ? formulas.equals(that.formulas) : that.formulas == null;
    }

    @Override
    public int hashCode() {
        int result = type != null ? type.hashCode() : 0;
        result = 31 * result + (formulas != null ? formulas.hashCode() : 0);
        return result;
    }

    public OperatorType getType() {
        return type;
    }

    public List<Formula> getFormulas() {
        return formulas;
    }
}
