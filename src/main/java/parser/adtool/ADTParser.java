package parser.adtool;

import mef.faulttree.BasicEventDefinition;
import mef.faulttree.ElementDefinition;
import mef.faulttree.FaultTreeDefinition;
import mef.faulttree.GateDefinition;
import mef.formula.BasicBooleanOperator;
import mef.formula.BasicEvent;
import mef.formula.Formula;
import mef.formula.Gate;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import parser.Parser;
import parser.XMLParser;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

import static mef.formula.BasicBooleanOperator.OperatorType.and;
import static mef.formula.BasicBooleanOperator.OperatorType.or;

public class ADTParser extends Parser {

    /**
     * Convert ADT file to FaultTreeDefinition.
     * @param file
     * @return
     */
    @Override
    public FaultTreeDefinition toMEF(File file) {
        FaultTreeDefinition faultTreeDefinition = null;
        ADTNode adtree = fromAD(file);
        if (adtree.getLabel() != null) {
            String name = adtree.getLabel();
            List<ElementDefinition> elementDefinitions = parseNodeToElementDefinition(adtree);
            faultTreeDefinition = new FaultTreeDefinition(name, null, null, elementDefinitions);
        }
        return faultTreeDefinition;
    }

    // convert a ADTools xml file to object representation
    private ADTNode fromAD(File file) {
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
            adtree = parseNode((Element) root.elements().get(0));

        } catch (MalformedURLException | DocumentException e) {
            e.printStackTrace();
        }

        return adtree;
    }

    // parse a node and recursively its children
    private ADTNode parseNode(Element element) {
        String label = "";
        Set<ADTNode> children = new HashSet<>();
        ADTNode.Refinement refinement =
                ADTNode.Refinement.valueOf(element.attributeValue("refinement", "DISJUNCTIVE").toUpperCase());

        // walk through elements and recursively parse child nodes
        for (Iterator i = element.elementIterator(); i.hasNext(); ) {
            Element e = (Element) i.next();
            if (e.getName().equals("label")) {
                label = e.getText();
            } else if (e.getName().equals("node")) {
                ADTNode node = parseNode(e);
                children.add(node);
            }
        }

        ADTNode node = new ADTNode(label, children, refinement);
        return node;
    }

    // parse each node to an ElementDefinition (for ADT only GateDefinitions or BasicEventDefinitions are possible
    private List<ElementDefinition> parseNodeToElementDefinition(ADTNode node) {
        List<ElementDefinition> elementDefinitions = new ArrayList<>();
        // get all the nodes in the tree
        Set<ADTNode> nodes = unwrap(node);
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
                BasicEventDefinition basicEventDefinition = new BasicEventDefinition(name);
                elementDefinitions.add(basicEventDefinition);
            }
        }
        return elementDefinitions;
    }

    /*
    * The ADT xml is a very hierarchical structure. To simply working with it, this method unwraps this structure and
     * adds all the existing nodes into a set. */
    private Set<ADTNode> unwrap(ADTNode node) {
        Set<ADTNode> nodes = new HashSet<>();
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