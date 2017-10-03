package parser.adtool;

import attacker_attribution.User;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ADTNode {

    public enum Refinement {
        CONJUNCTIVE, DISJUNCTIVE
    }

    private String ID;
    private String label;
    private List<ADTNode> children;
    private  Refinement refinement;
    private double probability;

    public ADTNode(String ID, String label, List<ADTNode> children, Refinement refinement, double probability) {
        this.ID = ID;
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
        this.ID = adtNode.ID;
        this.label = new String(adtNode.label);
        this.children = childrenCloned;
        this.refinement = adtNode.refinement;
        this.probability = adtNode.probability;
    }

    public ADTNode getByID(String ID) {
        Set<ADTNode> allNodes = this.getAllNodes();
        for (ADTNode node : allNodes) {
            if (node.ID.equals(ID))
                return node;
        }
        return null;
    }

    private Set<ADTNode> getAllNodes() {
        Set<ADTNode> allNodes = new HashSet<>();
        allNodes.add(this);
        this.children.forEach(n -> allNodes.addAll(n.getAllNodes()));
        return allNodes;
    }

    public ADTNode unfold(Set<User> users) {
        List<ADTNode> userNodes = new ArrayList<>();
        for (User user : users) {
            ADTNode userNode = new ADTNode(this);
            userNode.annotate(user.getName());
            userNodes.add(userNode);
        }

        this.connect(userNodes);
        // TODO IDs
        return new ADTNode(this.ID, this.label, this.children, Refinement.DISJUNCTIVE, 0);
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

    private void connect(List<ADTNode> userNodes) {
        if (this.children == null || this.children.size() == 0) {
            this.children = userNodes;
        } else if (this.children.size() == 1) {
            this.children.get(0).connect(userNodes);
        } else {
            for (ADTNode childNode : this.children) {
                if (childNode.children.size() == 1 && childNode.children.get(0).children.size() <= 1) {
                    childNode.connect(userNodes);
                } else {
                    if (childNode.children.size() == 1 && childNode.children.get(0).children.size() >= 1)
                        childNode = childNode.children.get(0);
                    childNode.refinement = Refinement.DISJUNCTIVE;
                    List<ADTNode> childUserNodes = new ArrayList<>();
                    for (ADTNode userNode : userNodes) {
                        ADTNode node = userNode.getByID(childNode.ID);
                        childUserNodes.add(node);
                    }
                    childNode.children = childUserNodes;
                }
            }
        }
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

    public String getID() {
        return ID;
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
