package mef.faulttree;

import mef.general.Attribute;
import mef.general.Constant;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.List;

public class BasicEventDefinition extends EventDefinition {
    // actually an expression can be more, but we simplify here
    private Constant expression;

    public BasicEventDefinition(String name) {
        super(name, Role.PUBLIC, null, null);
    }

    public BasicEventDefinition(String name, Constant expression) {
        super(name, Role.PUBLIC, null, null);
        this.expression = expression;
    }

    public BasicEventDefinition(String name, Role role, String label, List<Attribute> attributes, Constant expression) {
        super(name, role, label, attributes);
        this.expression = expression;
    }

    @Override
    public Element toXML(Element element) {
        Element defineBasicEvent = element.addElement("define-basic-event");
        // name
        defineBasicEvent.addAttribute("name", this.getName());
        // expression
        if (this.getExpression() != null)
            expression.toXML(defineBasicEvent);
        // attributes
        if (this.getAttributes() != null) {
            Element attributes = defineBasicEvent.addElement("attributes");
            for (Attribute attribute : this.getAttributes()) {
                attribute.toXML(attributes);
            }
        }
        // label
        if (this.getLabel() != null) {
            Element label = defineBasicEvent.addElement("label");
            label.setText(this.getLabel());
        }
        // role
        if (this.getRole() != null && this.getRole() != Role.PUBLIC)
            defineBasicEvent.addAttribute("role", this.getRole().toString().toLowerCase());

        return defineBasicEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        BasicEventDefinition that = (BasicEventDefinition) o;

        return expression != null ? expression.equals(that.expression) : that.expression == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (expression != null ? expression.hashCode() : 0);
        return result;
    }

    public Constant getExpression() {
        return expression;
    }
}
