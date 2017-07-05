package mef.formula;

import org.dom4j.Element;

public class ImplyOperator extends BooleanOperator{
    private Formula leftFormula;
    private Formula rightFormula;

    public ImplyOperator(Formula leftFormula, Formula rightFormula) {
        this.leftFormula = leftFormula;
        this.rightFormula = rightFormula;
    }

    @Override
    public Element toXML(Element element) {
        Element imply = element.addElement("imply");
        leftFormula.toXML(imply);
        rightFormula.toXML(imply);
        return imply;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ImplyOperator that = (ImplyOperator) o;

        if (leftFormula != null ? !leftFormula.equals(that.leftFormula) : that.leftFormula != null) return false;
        return rightFormula != null ? rightFormula.equals(that.rightFormula) : that.rightFormula == null;
    }

    @Override
    public int hashCode() {
        int result = leftFormula != null ? leftFormula.hashCode() : 0;
        result = 31 * result + (rightFormula != null ? rightFormula.hashCode() : 0);
        return result;
    }

    public Formula getLeftFormula() {
        return leftFormula;
    }

    public Formula getRightFormula() {
        return rightFormula;
    }
}
