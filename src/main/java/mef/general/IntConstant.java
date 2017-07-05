package mef.general;


import org.dom4j.Element;

public class IntConstant extends Constant{
    private int value;

    public IntConstant(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

    @Override
    public Element toXML(Element element) {
        Element constant = element.addElement("int");
        constant.addAttribute("value", (new Integer(value)).toString());
        return constant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IntConstant that = (IntConstant) o;

        return value == that.value;
    }

    @Override
    public int hashCode() {
        return value;
    }
}
