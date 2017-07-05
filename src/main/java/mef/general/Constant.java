package mef.general;

import org.dom4j.Element;


public abstract class Constant {
    // actually, only BooleanConstant is a formula element. This needs to be adapted possibly

    public abstract Element toXML(Element element);
}
