package mef.formula;

import org.dom4j.Element;

public class Gate extends Event{
    public Gate(String name) {
        super(name);
    }

    @Override
    public Element toXML(Element element) {
        Element gate = element.addElement("gate");
        gate.addAttribute("name", this.getName());
        return gate;
    }
}
