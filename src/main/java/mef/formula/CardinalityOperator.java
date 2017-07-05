package mef.formula;

import org.dom4j.Element;

import java.util.List;

public class CardinalityOperator extends BooleanOperator{
    private int min;
    private int max;
    private List<Formula> formulas;

    public CardinalityOperator(int min, int max, List<Formula> formulas) {
        this.min = min;
        this.max = max;
        this.formulas = formulas;
    }

    @Override
    public Element toXML(Element element) {
        Element cardinality = element.addElement("cardinality");
        cardinality.addAttribute("min", (new Integer(min)).toString());
        cardinality.addAttribute("max", (new Integer(max)).toString());
        formulas.forEach(f -> f.toXML(cardinality));
        return cardinality;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CardinalityOperator that = (CardinalityOperator) o;

        if (min != that.min) return false;
        if (max != that.max) return false;
        return formulas != null ? formulas.equals(that.formulas) : that.formulas == null;
    }

    @Override
    public int hashCode() {
        int result = min;
        result = 31 * result + max;
        result = 31 * result + (formulas != null ? formulas.hashCode() : 0);
        return result;
    }

    public int getMin() {
        return min;
    }

    public int getMax() {
        return max;
    }

    public List<Formula> getFormulas() {
        return formulas;
    }
}
