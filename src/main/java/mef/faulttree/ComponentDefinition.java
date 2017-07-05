package mef.faulttree;

import mef.general.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import java.util.List;

public class ComponentDefinition extends ElementDefinition {
    private String name;
    private Role role;
    private String label;
    private List<Attribute> attributes;
    private List<ElementDefinition> elementDefinitions;

    public ComponentDefinition(String name, List<ElementDefinition> elementDefinitions) {
        this.name = name;
        this.role = Role.PUBLIC;
        this.label = null;
        this.attributes = null;
        this.elementDefinitions = elementDefinitions;
    }

    public ComponentDefinition(String name, Role role, String label, List<Attribute> attributes, List<ElementDefinition> elementDefinitions) {
        this.name = name;
        this.role = role;
        this.label = label;
        this.attributes = attributes;
        this.elementDefinitions = elementDefinitions;
    }

    @Override
    public Element toXML(Element element) {
        Element component = element.addElement("define-component");
        // name
        component.addAttribute("name", this.getName());
        // elementDefinitions
        elementDefinitions.forEach(e -> e.toXML(component));
        // attributes
        if (this.getAttributes() != null) {
            Element attributes = component.addElement("attributes");
            for (Attribute attribute : this.getAttributes()) {
                attribute.toXML(attributes);
            }
        }
        // label
        if (this.getLabel() != null) {
            Element label = component.addElement("label");
            label.setText(this.getLabel());
        }
        // role
        if (this.getRole() != null && this.getRole() != Role.PUBLIC)
            component.addAttribute("role", this.getRole().toString().toLowerCase());
        return component;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ComponentDefinition that = (ComponentDefinition) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (role != that.role) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        if (attributes != null ? !attributes.equals(that.attributes) : that.attributes != null) return false;
        return elementDefinitions != null ? elementDefinitions.equals(that.elementDefinitions) : that.elementDefinitions == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (role != null ? role.hashCode() : 0);
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        result = 31 * result + (elementDefinitions != null ? elementDefinitions.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public Role getRole() {
        return role;
    }

    public String getLabel() {
        return label;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public List<ElementDefinition> getElementDefinitions() {
        return elementDefinitions;
    }
}
