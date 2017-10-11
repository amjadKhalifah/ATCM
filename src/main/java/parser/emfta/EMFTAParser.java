package parser.emfta;

import mef.faulttree.*;
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
import java.util.stream.Collectors;

public class EMFTAParser extends Parser {

    /**
     * Parse EMFTA file to object representation
     * @param file .emfta file
     * @return EMFTA model as object
     */
    public EMFTAFTAModel fromEMFTA(File file) {
        EMFTAFTAModel emftaftaModel = null;
        try {
            // get file URL
            URL url = file.toURI().toURL();
            // get dom4j Document object
            Document document = XMLParser.parse(url);
            // root xml element
            Element root = document.getRootElement();

            // type of root element needs to be equal to emfta:FTAModel; otherwise xml is no emfta model
            if (!root.getQualifiedName().equals("emfta:FTAModel"))
                throw new DocumentException("XML is not valid EMFTA XML.");

            // get properties of ftamodel
            String ftaModelName = root.attributeValue("name");
            String ftaModelDesc = root.attributeValue("description");
            String ftaModelComments = root.attributeValue("comments");

            // get the root event; necessary for iteratively parsing all nodes
            int rootEventIndex = getEventIndex(root.attributeValue("root"));
            // parse events in the fault tree
            List<EMFTAEvent> events = parseEvents(root);

            emftaftaModel = new EMFTAFTAModel(ftaModelName, ftaModelDesc, ftaModelComments,
                    rootEventIndex, events);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

        return emftaftaModel;
    }

    private List<EMFTAEvent> parseEvents(Element root) {
        List<EMFTAEvent> events = new ArrayList<>();

        // iterate through events
        for (Iterator i = root.elementIterator(); i.hasNext(); ) {
            Element element = (Element) i.next();
            // parse current event
            EMFTAEvent emftaEvent = parseEvent(element);
            events.add(emftaEvent);
        }

        return events;
    }

    private EMFTAEvent parseEvent(Element element) {
        // get properties of events; if attribute is not available, default values are set
        EMFTAEvent.EMFTAEventType type = EMFTAEvent.EMFTAEventType.valueOf(element.attributeValue("type", "Basic"));
        String name = element.attributeValue("name", "");
        String description = element.attributeValue("description", "");
        double probability = Double.parseDouble(element.attributeValue("probability", "0.0"));

        // a gate is optional; only parse it, if it exists
        EMFTAGate gate = null;
        if (element.elements().size() > 0)
            gate = parseGate((Element) element.elements().get(0));

        EMFTAEvent event = new EMFTAEvent(type, name, description, probability, gate);
        return event;
    }

    private EMFTAGate parseGate(Element element) {
        // get properties of a gate
        String description = element.attributeValue("description", "");
        EMFTAGate.EMFTAGateType type = EMFTAGate.EMFTAGateType.valueOf(element.attributeValue("type", "OR"));
        /*
        * Example for value of 'events' attribute: "//@events.2 //@events.4 //@events.6 //@events.5"
        * Parsing the event indices works as follows:
        * 1. Check, if 'events' attribute exists at all (if not, return an empty set)
        * 2. Split value of 'events' attribute at whitespaces
        * 3. get event index
        * 5. create set
        * */
        Set<Integer> eventIndices = !element.attributeValue("events", "").equals("") ?
                Arrays.stream(element.attributeValue("events").split(" "))
                        .map(s -> getEventIndex(s))
                        .collect(Collectors.toSet())
                : new HashSet<>();

        EMFTAGate gate = new EMFTAGate(description, eventIndices, type);
        return gate;
    }

    private int getEventIndex(String s) {
        /*
        * Example for string s: //@events.1
        * 1. split at dot (.)
        * 2. take the right part, i.e. the index
        * 3. parse it to integer and return
        * */
        int index = Integer.parseInt(s.split("\\.")[1]);
        return index;
    }

    /**
     * Convert an EMTFA file to a Open-PSA conformant fault tree representation
     * @param file file .emfta file
     * @param users NOT USED FOR THIS PARSER
     * @return
     */
    @Override
    public FaultTreeDefinition toMEF(File file, File users) {
        // get object representation of EMFTA file
        EMFTAFTAModel emftaftaModel = fromEMFTA(file);

        List<ElementDefinition> elementDefinitions = new ArrayList<>();

        for (EMFTAEvent emftaEvent : emftaftaModel.getEvents()) {
            // TODO so far, only AND, OR and XOR can be taken into account
            if (emftaEvent.getGate() != null &&
                    Arrays.asList(EMFTAGate.EMFTAGateType.OR, EMFTAGate.EMFTAGateType.AND, EMFTAGate.EMFTAGateType.XOR)
                            .contains(emftaEvent.getGate().getType())) {
                // convert operator type
                BasicBooleanOperator.OperatorType t = BasicBooleanOperator.OperatorType.valueOf(emftaEvent.getGate()
                        .getType().toString().toLowerCase());

                // convert elements of gate to Formula object
                List<Formula> formulas = toFormulas(emftaEvent.getGate().getEventIndices().stream().map(i ->
                        emftaftaModel.getEvents().get(i)).collect(Collectors.toList()));

                // create boolean operator object using the type and the formulas
                BasicBooleanOperator o = new BasicBooleanOperator(t, formulas);

                // get probability
                FloatConstant f = null;
                if (emftaEvent.getProbability() > 0)
                    f = new FloatConstant(emftaEvent.getProbability());

                // create new GateDefinition
                GateDefinition gateDefinition = new GateDefinition(emftaEvent.getName(), o, f);
                elementDefinitions.add(gateDefinition);
            } else {
                // convert probability of event to FloatConstant object
                FloatConstant f = null;
                if (emftaEvent.getProbability() > 0)
                    f = new FloatConstant(emftaEvent.getProbability());

                // create new BasicEventDefinition using the constant
                BasicEventDefinition basicEventDefinition = new BasicEventDefinition(emftaEvent.getName(), f);
                elementDefinitions.add(basicEventDefinition);
            }
        }

        FaultTreeDefinition ftDef = new FaultTreeDefinition(emftaftaModel.getName(), null, null, elementDefinitions);
        return ftDef;
    }

    /**
     * Convert a list of EMFTA events to a list of formulas
     * @param events
     * @return
     */
    private List<Formula> toFormulas(List<EMFTAEvent> events) {
        List<Formula> formulas = new ArrayList<>();
        for (EMFTAEvent event : events) {
            // if EMFTA event does NOT define a gate, create BasicEvent object
            if (event.getGate() == null)
                formulas.add(new BasicEvent(event.getName()));
            // otherwise, create a Gate object
            else
                formulas.add(new Gate(event.getName()));
        }
        return formulas;
    }
}
