package hp;

import java.util.Set;


import causality.Variable;

/** witness is a result of a successfull but for check
 *  according to hp it is x', W, w 
 * @author Ibrahim Amjad
 *
 */
public class Witness {
	private Variable x;
	private Set<Variable> w;
	

	public Witness(Variable x, Set<Variable> w) {
		super();
		this.x = x;
		this.w = w;
	}
	
	public Variable getX() {
		return x;
	}


	public void setX(Variable x) {
		this.x = x;
	}


	public Set<Variable> getW() {
		return w;
	}


	public void setW(Set<Variable> w) {
		this.w = w;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((w == null) ? 0 : w.hashCode());
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		return result;
	}

	
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Witness other = (Witness) obj;
		if (w == null) {
			if (other.w != null)
				return false;
		} else if (!w.equals(other.w))
			return false;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		return true;
	}
 

	@Override
	public String toString() {
		return "Witness [x=" + x + ", w=" + w + "] \n";
	}

	

}
