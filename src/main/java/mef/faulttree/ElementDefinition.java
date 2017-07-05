package mef.faulttree;

import org.dom4j.Element;

public abstract class ElementDefinition {
    public abstract Element toXML(Element element);
}
