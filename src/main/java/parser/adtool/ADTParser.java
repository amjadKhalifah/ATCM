package parser.adtool;

import attacker_attribution.User;
import attacker_attribution.UserParser;
import mef.faulttree.BasicEventDefinition;
import mef.faulttree.ElementDefinition;
import mef.faulttree.FaultTreeDefinition;
import mef.faulttree.GateDefinition;
import mef.formula.BasicBooleanOperator;
import mef.formula.BasicEvent;
import mef.formula.Formula;
import mef.formula.Gate;
import mef.general.FloatConstant;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import parser.Parser;
import parser.XMLParser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;
import java.util.List;

import static mef.formula.BasicBooleanOperator.OperatorType.and;
import static mef.formula.BasicBooleanOperator.OperatorType.or;

public class ADTParser extends Parser {

    /**
     * Convert ADT file to FaultTreeDefinition.
     * @param file
     * @return
     */
    @Override
    public FaultTreeDefinition toMEF(File file, File users) {
        FaultTreeDefinition faultTreeDefinition = null;
        ADTNode adtree = fromAD(file);

        if (users != null) {
            Set<User> userSet = UserParser.parse(users);
            adtree.unfold(userSet);
        }

        if (adtree.getLabel() != null) {
            // get name of the tree
            String name = adtree.getLabel().replace("\n","").replace("\r","");
            // parse the definitions in the attack tree to element definitions
            List<ElementDefinition> elementDefinitions = parseNodeToElementDefinition(adtree);
            // create new fault tree
            faultTreeDefinition = new FaultTreeDefinition(name, null, null, elementDefinitions);
        }
        return faultTreeDefinition;
    }

    // convert a ADTools xml file to object representation
    public ADTNode fromAD(File file) {
        ADTNode adtree = null;
        try {
            // get file URL
            URL url = file.toURI().toURL();
            // get dom4j Document object
            Document document = XMLParser.parse(url);
            // root xml element
            Element root = document.getRootElement();

            // type of root element needs to be equal to emfta:FTAModel; otherwise xml is no emfta model
            if (!root.getQualifiedName().equals("adtree"))
                throw new DocumentException("XML is not valid ADTree XML.");

            // parse
            adtree = parseNode((Element) root.elements().get(0), "0");

        } catch (MalformedURLException | DocumentException e) {
            e.printStackTrace();
        }

        return adtree;
    }

    // parse a node and recursively its children
    private ADTNode parseNode(Element element, String ID) {
        String label = "";
        List<ADTNode> children = new ArrayList<>();
        ADTNode.Refinement refinement =
                ADTNode.Refinement.valueOf(element.attributeValue("refinement", "DISJUNCTIVE").toUpperCase());
        double probability = 0D;

        // walk through elements and recursively parse child nodes
        int counter=0;
        for (Iterator i = element.elementIterator(); i.hasNext(); ) {
            Element e = (Element) i.next();
            if (e.getName().equals("label")) {
                label = e.getText().replace("\n","").replace("\r","");
            } else if (e.getName().equals("parameter")) {
                if (e.attributeValue("domainId", "").equals("ProbSucc1")) {
                    String probabilityStr = e.getText();
                    try {
                        probability = Double.parseDouble(probabilityStr);
                    } catch (NumberFormatException ex) {
                        System.err.println("Cannot parse probability of " + probabilityStr);
                    }
                }
            } else if (e.getName().equals("node")) {
                counter++;
                ADTNode node = parseNode(e, ID + "." + counter);
                children.add(node);
            }
        }
        ADTNode node = new ADTNode(""+ID, label, children, refinement, probability);
        return node;
    }

    // parse each node to an ElementDefinition (for ADT only GateDefinitions or BasicEventDefinitions are possible
    private List<ElementDefinition> parseNodeToElementDefinition(ADTNode node) {
        List<ElementDefinition> elementDefinitions = new ArrayList<>();
        // get all the nodes in the tree
        List<ADTNode> nodes = unwrap(node);
        // iterate through nodes
        for (ADTNode n : nodes) {
            String name = n.getLabel();
            // if a node has children, it will be considered as gate -> GateDefinition
            if (n.getChildren().size() > 0) {
                // get the operator based on the refinement attribute: DISJUNCTIVE -> OR; CONJUNCTIVE -> AND
                BasicBooleanOperator.OperatorType type = n.getRefinement() == ADTNode.Refinement.DISJUNCTIVE ? or : and;

                List<Formula> formulas = new ArrayList<>();
                for (ADTNode child : n.getChildren()) {
                    String childName = child.getLabel();
                    // if a child of the node has children itself, it is considered as Gate
                    if (child.getChildren().size() > 0) {
                        Gate gate = new Gate(childName);
                        formulas.add(gate);
                    }
                    // otherwise, it is a BasicEvent
                    else {
                        BasicEvent basicEvent = new BasicEvent(childName);
                        formulas.add(basicEvent);
                    }
                }

                Formula formula = new BasicBooleanOperator(type, formulas);
                GateDefinition gateDefinition = new GateDefinition(name, formula);
                elementDefinitions.add(gateDefinition);
            }
            // if there aren't any children, it is a leaf node -> BasicEventDefinition
            else {
                FloatConstant f = null;
                if (n.getProbability() > 0)
                    f = new FloatConstant(n.getProbability());
                BasicEventDefinition basicEventDefinition = new BasicEventDefinition(name, f);
                elementDefinitions.add(basicEventDefinition);
            }
        }
        return elementDefinitions;
    }

    /*
    * The ADT xml is a very hierarchical structure. To simply working with it, this method unwraps this structure and
     * adds all the existing nodes into a set. */
    private List<ADTNode> unwrap(ADTNode node) {
        List<ADTNode> nodes = new ArrayList<>();
        nodes.add(node);
        // if there are child nodes, unwrap them as well
        if (node.getChildren().size() > 0) {
            for (ADTNode n : node.getChildren()) {
                nodes.addAll(unwrap(n));
            }
        }
        return nodes;
    }
}
