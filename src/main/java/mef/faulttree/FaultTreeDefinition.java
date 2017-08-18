package mef.faulttree;

import mef.general.Attribute;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class FaultTreeDefinition {
    private String name;
    private String label;
    private List<Attribute> attributes;
    private List<ElementDefinition> elementDefinitions;

    public FaultTreeDefinition(String name, String label, List<Attribute> attributes, List<ElementDefinition> elementDefinitions) {
        this.name = name;
        this.label = label;
        this.attributes = attributes;
        this.elementDefinitions = elementDefinitions;
    }

    public Document toXML() {
        Document document = DocumentHelper.createDocument();
        document.addDocType("opsa-mef", null, null);
        Element root = document.addElement("opsa-mef");
        Element defineFaultTree = root.addElement("define-fault-tree");
        defineFaultTree.addAttribute("name", this.getName());

        for (ElementDefinition elementDefinition : elementDefinitions) {
            Element e = elementDefinition.toXML(defineFaultTree);
        }

        return document;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FaultTreeDefinition that = (FaultTreeDefinition) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        if (label != null ? !label.equals(that.label) : that.label != null) return false;
        if (attributes != null ? !attributes.equals(that.attributes) : that.attributes != null) return false;
        return elementDefinitions != null ? elementDefinitions.equals(that.elementDefinitions) : that.elementDefinitions == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (label != null ? label.hashCode() : 0);
        result = 31 * result + (attributes != null ? attributes.hashCode() : 0);
        result = 31 * result + (elementDefinitions != null ? elementDefinitions.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
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
