package mef.general;

import org.dom4j.Element;

public class Attribute {
    private String name;
    private String value;
    private String type;

    public Attribute(String name, String value, String type) {
        this.name = name;
        this.value = value;
        this.type = type;
    }

    public Element toXML(Element element) {
        Element attribute = element.addElement("attribute");
        attribute.addAttribute("name", name);
        attribute.addAttribute("value", value);
        attribute.addAttribute("type", type);
        return attribute;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Attribute attribute = (Attribute) o;

        if (name != null ? !name.equals(attribute.name) : attribute.name != null) return false;
        if (value != null ? !value.equals(attribute.value) : attribute.value != null) return false;
        return type != null ? type.equals(attribute.type) : attribute.type == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (value != null ? value.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public String getType() {
        return type;
    }
}
