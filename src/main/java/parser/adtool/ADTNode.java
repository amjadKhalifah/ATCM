package parser.adtool;

import java.util.Set;

public class ADTNode {

    public enum Refinement {
        CONJUNCTIVE, DISJUNCTIVE
    }

    private String label;
    private Set<ADTNode> children;
    private  Refinement refinement;
    private double probability;

    public ADTNode(String label, Set<ADTNode> children, Refinement refinement, double probability) {
        this.label = label;
        this.children = children;
        this.refinement = refinement;
        this.probability = probability;
    }

    public String getLabel() {
        return label;
    }

    public Set<ADTNode> getChildren() {
        return children;
    }

    public Refinement getRefinement() {
        return refinement;
    }

    public double getProbability() {
        return probability;
    }
}
