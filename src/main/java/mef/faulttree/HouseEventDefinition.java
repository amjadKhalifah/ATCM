package mef.faulttree;

import mef.general.Attribute;
import mef.general.BooleanConstant;
import org.dom4j.Element;

import java.util.List;

public class HouseEventDefinition extends EventDefinition {
    private BooleanConstant constant;

    public HouseEventDefinition(String name, BooleanConstant constant) {
        super(name, Role.PUBLIC, null, null);
        this.constant = constant;
    }

    public HouseEventDefinition(String name, Role role, String label, List<Attribute> attributes, BooleanConstant constant) {
        super(name, role, label, attributes);
        this.constant = constant;
    }

    @Override
    public Element toXML(Element element) {
        Element defineHouseEvent = element.addElement("define-house-event");
        defineHouseEvent.addAttribute("name", this.getName());
        if (constant != null) {
            Element constant = this.getConstant().toXML(defineHouseEvent);
        }
        // attributes
        if (this.getAttributes() != null) {
            Element attributes = defineHouseEvent.addElement("attributes");
            for (Attribute attribute : this.getAttributes()) {
                attribute.toXML(attributes);
            }
        }
        // label
        if (this.getLabel() != null) {
            Element label = defineHouseEvent.addElement("label");
            label.setText(this.getLabel());
        }
        // role
        if (this.getRole() != null && this.getRole() != Role.PUBLIC)
            defineHouseEvent.addAttribute("role", this.getRole().toString().toLowerCase());

        return defineHouseEvent;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        HouseEventDefinition that = (HouseEventDefinition) o;

        return constant != null ? constant.equals(that.constant) : that.constant == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (constant != null ? constant.hashCode() : 0);
        return result;
    }

    public BooleanConstant getConstant() {
        return constant;
    }
}
