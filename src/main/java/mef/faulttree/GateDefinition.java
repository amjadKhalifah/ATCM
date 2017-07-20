package mef.faulttree;

import mef.general.Attribute;
import mef.formula.Formula;
import mef.general.Constant;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.ArrayList;
import java.util.List;

public class GateDefinition extends EventDefinition {
    private Formula formula;
    // NOTE: custom parameter; not part of the specification
    private Constant expression;

    public GateDefinition(String name, Formula formula) {
        super(name, Role.PUBLIC, null, null);
        this.formula = formula;
    }

    public GateDefinition(String name, Formula formula, Constant expression) {
        super(name, Role.PUBLIC, null, null);
        this.formula = formula;
        this.expression = expression;
    }

    public GateDefinition(String name, Role role, String label, List<Attribute> attributes, Formula formula) {
        super(name, role, label, attributes);
        this.formula = formula;
    }

    @Override
    public Element toXML(Element element) {
        Element defineGate = element.addElement("define-gate");
        defineGate.addAttribute("name", this.getName());
        Element formulaXML = this.getFormula().toXML(defineGate);

        return defineGate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        GateDefinition that = (GateDefinition) o;

        return formula != null ? formula.equals(that.formula) : that.formula == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (formula != null ? formula.hashCode() : 0);
        return result;
    }

    public Formula getFormula() {
        return formula;
    }

    public Constant getExpression() {
        return expression;
    }
}
