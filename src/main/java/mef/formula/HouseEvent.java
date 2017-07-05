package mef.formula;

import org.dom4j.Element;

public class HouseEvent extends Event {
    public HouseEvent(String name) {
        super(name);
    }

    @Override
    public Element toXML(Element element) {
        Element houseEvent = element.addElement("house-event");
        houseEvent.addAttribute("name", this.getName());
        return houseEvent;
    }
}
