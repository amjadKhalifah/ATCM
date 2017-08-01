package causality;

import mef.formula.Formula;
import org.dom4j.Element;

public abstract class Variable extends Formula {

    private String name;
    // added by amjad
    private boolean value;

    public Variable(String name) {
        this.name = name;
    }

    @Override
    public Element toXML(Element element) {
        // method not needed as a Variable is not part of the Open-PSA spec
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Variable variable = (Variable) o;

        return name != null ? name.equals(variable.name) : variable.name == null;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    public String getName() {
        return name;
    }

	public boolean getValue() {
		return value;
	}

	public void setValue(boolean value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "Variable [name=" + name + ", value=" + value + "]";
	}
    
	
	
    
}
