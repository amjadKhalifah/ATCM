package mef.general;


import org.dom4j.Element;

public class BooleanConstant extends Constant{
    private boolean value;

    public BooleanConstant(boolean value) {
        this.value = value;
    }

    @Override
    public Element toXML(Element element) {
        Element constant = element.addElement("constant");
        constant.addAttribute("value", ""+this.getValue());
        return constant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BooleanConstant that = (BooleanConstant) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return (value ? 1 : 0);
    }

    public boolean getValue() {
        return value;
    }
}
