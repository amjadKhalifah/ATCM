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

    // copy constructor
    public ADTNode(ADTNode adtNode) {
        List<ADTNode> childrenCloned;
        if (adtNode.children == null)
            childrenCloned = null;
        else if (adtNode.children.size() == 0)
            childrenCloned = new ArrayList<>();
        else {
            childrenCloned = new ArrayList<>();
            adtNode.children.forEach(n -> childrenCloned.add(new ADTNode(n)));
        }
        this.label = new String(adtNode.label);
        this.children = childrenCloned;
        this.refinement = adtNode.refinement;
        this.probability = adtNode.probability;
    }

    public ADTNode unfold(Set<User> users) {
        List<ADTNode> userNodes = new ArrayList<>();
        for (User user : users) {
            ADTNode userNode = new ADTNode(this);
            userNode.annotate(user.getName());
            userNodes.add(userNode);
        }
        return new ADTNode(this.label, userNodes, Refinement.DISJUNCTIVE, 0);
    }

    private ADTNode annotate(String annotation) {
        this.label = annotation + "." + this.label;
        if (this.children != null) {
            for (ADTNode child : this.children) {
                child.annotate(annotation);
            }
        }
        return this;
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
