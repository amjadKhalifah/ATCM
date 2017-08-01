package causality;

import mef.formula.BasicEvent;
import mef.formula.Event;
import mef.formula.Formula;
import mef.formula.Gate;

public class EndogenousVariable extends Variable{
    private Formula formula;
    private double probability;

    public EndogenousVariable(String name, Formula formula) {
        super(name);
        this.formula = formula;
    }

    public EndogenousVariable(String name, Formula formula, double probability) {
        super(name);
        this.formula = formula;
        this.probability = probability;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        EndogenousVariable that = (EndogenousVariable) o;

        return formula != null ? formula.equals(that.formula) : that.formula == null;
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (formula != null ? formula.hashCode() : 0);
        return result;
    }

    public Formula getFormula() {
        return formula;
    }

    public void setFormula(Formula formula) {
        this.formula = formula;
    }

    public double getProbability() {
        return probability;
    }

    public void setProbability(double probability) {
        this.probability = probability;
    }

	@Override
	public String toString() {
	 
		return  super.toString()+" [formula=" + formula.print() + "]";
	}
    
    
    
}
