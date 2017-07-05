package parser.adtool;

import java.util.Set;

public class ADTNode {

    public enum Refinement {
        CONJUNCTIVE, DISJUNCTIVE
    }

    private String label;
    private Set<ADTNode> children;
    private  Refinement refinement;

    public ADTNode(String label, Set<ADTNode> children, Refinement refinement) {
        this.label = label;
        this.children = children;
        this.refinement = refinement;
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
}
