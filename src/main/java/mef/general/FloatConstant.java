package mef.general;


import org.dom4j.Element;

import java.text.DecimalFormat;
import java.text.NumberFormat;

public class FloatConstant extends Constant {
    private double value;

    public FloatConstant(double value) {
        this.value = value;
    }

    @Override
    public Element toXML(Element element) {
        NumberFormat formatter = new DecimalFormat("0.0#E0");

        Element constant = element.addElement("float");
        constant.addAttribute("value", formatter.format(this.getValue()).toLowerCase().replace(",", "."));
        return constant;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FloatConstant that = (FloatConstant) o;

        return Double.compare(that.value, value) == 0;
    }

    @Override
    public int hashCode() {
        long temp = Double.doubleToLongBits(value);
        return (int) (temp ^ (temp >>> 32));
    }

    public double getValue() {
        return value;
    }
}
