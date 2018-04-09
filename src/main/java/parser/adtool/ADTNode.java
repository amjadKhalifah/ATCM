package parser.adtool;

import attacker_attribution.User;
import mef.faulttree.ElementDefinition;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.tree.DefaultText;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ADTNode {

    public enum Refinement {
        CONJUNCTIVE, DISJUNCTIVE
    }

    public static final String USER_ATTRIBUTION_SEPARATOR = "_";
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

    /**
     * Creates a dummy ADTNode with specified number of branches and specified number of levels. All sub-trees have
     * branch size 2
     * @param branches
     * @param levels
     */
    public ADTNode(int branches, int levels) {
        this.ID = "-1";
        this.label = "-1";
        this.refinement = Refinement.DISJUNCTIVE;
        this.probability = 0;
        this.children = new ArrayList<>();

        if (levels > 0) {
            for (int i = 0; i < branches; i++) {
                ADTNode adtNode = generateTree(2, levels - 1);
                this.children.add(adtNode);
            }
        }

        this.setIDs("1");
        this.setLabels();
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

    @Deprecated
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

    public ADTNode unfold(Set<User> users, int[] unfoldLevels) {
        List<ADTNode> userNodes = new ArrayList<>();
        for (User user : users) {
            ADTNode userNode = new ADTNode(this);
            userNode.annotate(user.getName());
            userNodes.add(userNode);
        }
        // connect user nodes at correct position such that tree is properly unfolded
        if (unfoldLevels == null || unfoldLevels.length != this.children.size()) {
            // if config is invalid, just start unfolding at the very top of the tree, i.e. at the first children
            this.connect(userNodes, 0);
        } else {
            for (int i=0; i<unfoldLevels.length; i++) {
                this.children.get(i).connect(userNodes, unfoldLevels[i]);
            }
        }
        ADTNode unfolded = new ADTNode(this.ID, this.label, this.children, Refinement.DISJUNCTIVE, 0);
        // set again unique IDs; before they are not unique as the nodes in the user trees may have the same ones
        unfolded.setIDs(unfolded.ID);
        return unfolded;
    }

    private ADTNode annotate(String annotation) {
        // prefix current label with annotation
        this.label = annotation + USER_ATTRIBUTION_SEPARATOR + this.label;
        if (this.children != null) {
            // prefix all children
            for (ADTNode child : this.children) {
                child.annotate(annotation);
            }
        }
        return this;
    }

    @Deprecated
    private void connect(List<ADTNode> userNodes) {
        if (this.children == null || this.children.size() == 0) {
            // if node has no children at all, just connect all userNodes to current node
            this.children = userNodes;
        } else {
            /*
             * Loop through all children and find matching subtrees in userNodes
             * Connect those subtrees to the current child. On that way, we properly unfold the tree.
             */
            for (ADTNode childNode : this.children) {
                // set gate to OR
                childNode.refinement = Refinement.DISJUNCTIVE;
                List<ADTNode> childUserNodes = new ArrayList<>();
                // get user-specific subtrees that fit, i.e. those that have the current childNode as root
                for (ADTNode userNode : userNodes) {
                    ADTNode node = userNode.getByID(childNode.ID);
                    // add to list of child user nodes
                    childUserNodes.add(node);
                }
                // set children of current childNode to new children, i.e. the user-specific ones
                childNode.children = childUserNodes;
            }
        }
    }

    private void connect(List<ADTNode> userNodes, int unfoldLevel) {
        if (unfoldLevel == 0) {
            /*
             * Loop through all children and find matching subtrees in userNodes
             * Connect those subtrees to the current child. On that way, we properly unfold the tree.
             */
            for (ADTNode childNode : this.children) {
                // set gate to OR
                childNode.refinement = Refinement.DISJUNCTIVE;
                List<ADTNode> childUserNodes = new ArrayList<>();
                // get user-specific subtrees that fit, i.e. those that have the current childNode as root
                for (ADTNode userNode : userNodes) {
                    ADTNode node = userNode.getByID(childNode.ID);
                    // add to list of child user nodes
                    childUserNodes.add(node);
                }
                // set children of current childNode to new children, i.e. the user-specific ones
                childNode.children = childUserNodes;
            }
        } else {
            // call method recursively for all children
            this.children.forEach(c -> c.connect(userNodes, unfoldLevel - 1));
        }
    }

    private void setIDs(String ID) {
        this.ID = ID;
        for (int i = 0; i < this.children.size(); i++) {
            ADTNode child = this.children.get(i);
            child.setIDs(ID + "." + (i + 1));
        }
    }

    private void setLabels() {
        this.label = this.ID.replace(".", "_");
        this.children.forEach(ADTNode::setLabels);
    }

    public Document toXML() {
        Document document = DocumentHelper.createDocument();
        Element root = document.addElement("adtree");
        this.toXMLChild(root);

        return document;
    }

    private Element toXMLChild(Element parent) {
        Element node = parent.addElement("node");
        node.addAttribute("refinement", this.refinement.toString().toLowerCase());
        Element label = node.addElement("label");
        label.add(new DefaultText(this.label));

        for (ADTNode child : this.children) {
            child.toXMLChild(node);
        }

        return node;
    }

    private ADTNode generateTree(int branches, int levels) {
        ADTNode adtNodeRoot = null;
        if (levels > 0) {
            adtNodeRoot = new ADTNode("", "" , new ArrayList<>(),
                    Refinement.DISJUNCTIVE, 0);
            for (int i = 0; i < branches; i++) {
                ADTNode adtNode = generateTree(branches, levels - 1);
                if (adtNode != null)
                    adtNodeRoot.children.add(adtNode);
            }
        }
        return adtNodeRoot;
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
