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
    private Refinement refinement;
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
        // connect user nodes at correct position such that tree is properly unfolded
        this.connect(userNodes);
        ADTNode unfolded = new ADTNode(this.ID, this.label, this.children, Refinement.DISJUNCTIVE, 0);
        // set again unique IDs; before they are not unique as the nodes in the user trees may have the same ones
        unfolded.setIDs(unfolded.ID);
        return unfolded;
    }

    private ADTNode annotate(String annotation) {
        // prefix current label with annotation
        this.label = annotation + "." + this.label;
        if (this.children != null) {
            // prefix all children
            for (ADTNode child : this.children) {
                child.annotate(annotation);
            }
        }
        return this;
    }

    private void connect(List<ADTNode> userNodes) {
        if (this.children == null || this.children.size() == 0) {
            // if node has no children at all, just connect all userNodes to current node
            this.children = userNodes;
        } else if (this.children.size() == 1) {
            /*
            if node has one children only, call connect method on this one child again.
            we do this as we do not unfold at a node with only one child, i.e. no branches
             */
            this.children.get(0).connect(userNodes);
        } else {
            /*
            If the node has more than one child, we can unfold at this branch. Hence, we connect the respective parts
             of the user-specific trees to the current childNode. However. It might be that this childNode itself has
              only one child and this one child again has only one child; in this case, we do not want to unfold, and
               recursively call the connect() method again. If however, the current childNode's child has more than 1
               children, we want to unfold at exactly this one child of the current childNode. To ensure that this is
                done properly, we re-define childNode to its 1 child.
                In all other cases, we just set the children of the current childNode to the respective user-specific
                 subtree.
             */
            for (ADTNode childNode : this.children) {
                if (childNode.children.size() == 1 && childNode.children.get(0).children.size() <= 1) {
                    childNode.connect(userNodes);
                } else {
                    if (childNode.children.size() == 1 && childNode.children.get(0).children.size() >= 1) {
                        // re-define childNode to current childNode's single child
                        childNode = childNode.children.get(0);
                    }
                    // set gate to OR
                    childNode.refinement = Refinement.DISJUNCTIVE;
                    List<ADTNode> childUserNodes = new ArrayList<>();
                    // get user-specific subtrees that fit, i.e. those that have the current childNode as root
                    for (ADTNode userNode : userNodes) {
                        ADTNode node = userNode.getByID(childNode.ID);
                        // add to list of child user nodes
                        childUserNodes.add(node);
                    }
                    // set new children
                    childNode.children = childUserNodes;
                }
            }
        }
    }

    private void setIDs(String ID) {
        this.ID = ID;
        for (int i = 0; i < this.children.size(); i++) {
            ADTNode child = this.children.get(i);
            child.setIDs(ID + "." + (i + 1));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ADTNode adtNode = (ADTNode) o;

        if (Double.compare(adtNode.probability, probability) != 0) return false;
        if (ID != null ? !ID.equals(adtNode.ID) : adtNode.ID != null) return false;
        if (label != null ? !label.equals(adtNode.label) : adtNode.label != null) return false;
        if (children != null ? !children.equals(adtNode.children) : adtNode.children != null) return false;
        return refinement == adtNode.refinement;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = ID != null ? ID.hashCode() : 0;
        result = 31 * result + (label != null ? label.hashCode() : 0);
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
