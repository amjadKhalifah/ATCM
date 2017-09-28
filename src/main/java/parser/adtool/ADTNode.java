package parser.adtool;

import attacker_attribution.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ADTNode {

    public enum Refinement {
        CONJUNCTIVE, DISJUNCTIVE
    }

    private String label;
    private List<ADTNode> children;
    private  Refinement refinement;
    private double probability;

    public ADTNode(String label, List<ADTNode> children, Refinement refinement, double probability) {
        this.label = label;
        this.children = children;
        this.refinement = refinement;
        this.probability = probability;
    }

    public ADTNode unfold(Set<User> users) {
        return new ADTNode(this.label, new ArrayList<>(), Refinement.DISJUNCTIVE, 0);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ADTNode adtNode = (ADTNode) o;

        if (Double.compare(adtNode.probability, probability) != 0) return false;
        if (label != null ? !label.equals(adtNode.label) : adtNode.label != null) return false;
        if (children != null ? !children.equals(adtNode.children) : adtNode.children != null) return false;
        return refinement == adtNode.refinement;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = label != null ? label.hashCode() : 0;
        result = 31 * result + (children != null ? children.hashCode() : 0);
        result = 31 * result + (refinement != null ? refinement.hashCode() : 0);
        temp = Double.doubleToLongBits(probability);
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public String getLabel() {
        return label;
    }

    public List<ADTNode> getChildren() {
        return children;
    }

    public Refinement getRefinement() {
        return refinement;
    }

    public double getProbability() {
        return probability;
    }
}
