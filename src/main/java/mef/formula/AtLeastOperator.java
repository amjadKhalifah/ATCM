package mef.formula;

import org.dom4j.Element;

import java.util.List;

public class AtLeastOperator extends BooleanOperator {
    private int min;
    private List<Formula> formulas;

    public AtLeastOperator(int min, List<Formula> formulas) {
        this.min = min;
        this.formulas = formulas;
    }

    @Override
    public Element toXML(Element element) {
        Element atLeast = element.addElement("atleast");
        atLeast.addAttribute("min", (new Integer(min)).toString());
        formulas.forEach(f -> f.toXML(atLeast));
        return atLeast;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AtLeastOperator that = (AtLeastOperator) o;

        if (min != that.min) return false;
        return formulas != null ? formulas.equals(that.formulas) : that.formulas == null;
    }

    @Override
    public int hashCode() {
        int result = min;
        result = 31 * result + (formulas != null ? formulas.hashCode() : 0);
        return result;
    }

    public int getMin() {
        return min;
    }

    public List<Formula> getFormulas() {
        return formulas;
    }
}
