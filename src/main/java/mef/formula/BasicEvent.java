package mef.formula;

import org.dom4j.Element;

public class BasicEvent extends Event{
    public BasicEvent(String name) {
        super(name);
    }

    @Override
    public Element toXML(Element element) {
        Element houseEvent = element.addElement("basic-event");
        houseEvent.addAttribute("name", this.getName());
        return houseEvent;
    }
}
