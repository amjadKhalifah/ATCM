package util;

import causality.CausalModel;
import causality.EndogenousVariable;
import causality.ExogenousVariable;
import causality.Variable;
import mef.faulttree.*;
import mef.formula.*;
import mef.general.BooleanConstant;
import mef.general.FloatConstant;
import parser.emfta.EMFTAEvent;
import parser.emfta.EMFTAFTAModel;
import parser.emfta.EMFTAGate;

import java.util.*;

public class ModelProvider {

    public static FaultTreeDefinition faultTree1() {
        Gate g1 = new Gate("g1");
        Gate g2 = new Gate("g2");
        List<Formula> formulasTop = new ArrayList<>(Arrays.asList(g1, g2));
        BasicBooleanOperator or1 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.or, formulasTop);
        GateDefinition topDef = new GateDefinition("top", or1);

        HouseEvent h1 = new HouseEvent("h1");
        Gate g3 = new Gate("g3");
        Gate g4 = new Gate("g4");
        List<Formula> formulasG1 = new ArrayList<>(Arrays.asList(h1, g3, g4));
        BasicBooleanOperator and1 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.and, formulasG1);
        GateDefinition g1Def = new GateDefinition("g1", and1);

        BasicBooleanOperator not1 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.not, Arrays.asList(h1));
        BasicEvent e2 = new BasicEvent("e2");
        List<Formula> formulasG2 = new ArrayList<>(Arrays.asList(not1, e2, g4));
        BasicBooleanOperator and2 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.and, formulasG2);
        GateDefinition g2Def = new GateDefinition("g2", and2);

        BasicEvent e1 = new BasicEvent("e1");
        BasicEvent e3 = new BasicEvent("e3");
        List<Formula> formulasG3 = new ArrayList<>(Arrays.asList(e1, e3));
        BasicBooleanOperator or3 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.or, formulasG3);
        GateDefinition g3Def = new GateDefinition("g3", or3);

        BasicEvent e4 = new BasicEvent("e4");
        List<Formula> formulasG4 = new ArrayList<>(Arrays.asList(e3, e4));
        BasicBooleanOperator or4 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.or, formulasG4);
        GateDefinition g4Def = new GateDefinition("g4", or4);

        HouseEventDefinition h1Def = new HouseEventDefinition("h1", new BooleanConstant(true));

        List<ElementDefinition> elementDefinitions1 = new ArrayList<>(Arrays.asList(topDef, g1Def, g2Def, g3Def,
                g4Def, h1Def));

        FaultTreeDefinition faultTree1 = new FaultTreeDefinition("FT1", null, null, elementDefinitions1);

        return faultTree1;
    }

    public static FaultTreeDefinition faultTree2() {
        Gate g1 = new Gate("G1");
        Gate g2 = new Gate("G2");
        Gate g3 = new Gate("G3");
        List<Formula> formulasTop = new ArrayList<>(Arrays.asList(g1, g2, g3));
        BasicBooleanOperator or1 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.or, formulasTop);
        GateDefinition topDef = new GateDefinition("TOP", or1);

        BasicEvent be1 = new BasicEvent("BE1");
        BasicEvent be2 = new BasicEvent("BE2");
        List<Formula> formulasG1 = new ArrayList<>(Arrays.asList(be1, be2));
        BasicBooleanOperator and1 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.and, formulasG1);
        GateDefinition g1Def = new GateDefinition("G1", and1);

        BasicEvent be3 = new BasicEvent("BE3");
        List<Formula> formulasG2 = new ArrayList<>(Arrays.asList(be1, be3));
        BasicBooleanOperator and2 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.and, formulasG2);
        GateDefinition g2Def = new GateDefinition("G2", and2);

        FloatConstant constBe1 = new FloatConstant(1.2e-3);
        BasicEventDefinition be1Def = new BasicEventDefinition("BE1", constBe1);

        FloatConstant constBe2 = new FloatConstant(2.4e-3);
        BasicEventDefinition be2Def = new BasicEventDefinition("BE2", constBe2);
        FloatConstant constBe3 = new FloatConstant(5.2e-3);
        BasicEventDefinition be3Def = new BasicEventDefinition("BE3", constBe3);
        List<ElementDefinition> elementDefinitionsB = new ArrayList<>(Arrays.asList(be2Def, be3Def));
        ComponentDefinition b = new ComponentDefinition("B", elementDefinitionsB);

        List<ElementDefinition> elementDefinitionsA = new ArrayList<>(Arrays.asList(g1Def, g2Def, be1Def, b));
        ComponentDefinition a = new ComponentDefinition("A", elementDefinitionsA);

        BasicEvent be4 = new BasicEvent("BE4");
        List<Formula> formulasG3 = new ArrayList<>(Arrays.asList(be1, be4));
        BasicBooleanOperator and3 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.and, formulasG3);
        GateDefinition g3Def = new GateDefinition("G3", and3);

        FloatConstant constBe4 = new FloatConstant(1.6e-3);
        BasicEventDefinition be4Def = new BasicEventDefinition("BE4", constBe4);

        List<ElementDefinition> elementDefinitionsC = new ArrayList<>(Arrays.asList(g3Def, be4Def));
        ComponentDefinition c = new ComponentDefinition("C", elementDefinitionsC);

        List<ElementDefinition> elementDefinitions2 = new ArrayList<>(Arrays.asList(topDef, a, c));
        FaultTreeDefinition faultTree2 = new FaultTreeDefinition("FT", null, null, elementDefinitions2);

        return faultTree2;
    }

    public static EMFTAFTAModel billySuzyEMFTA() {
        EMFTAGate or1 = new EMFTAGate("", new HashSet<>(Arrays.asList(1,2)), EMFTAGate.EMFTAGateType.OR);
        EMFTAEvent bs = new EMFTAEvent(EMFTAEvent.EMFTAEventType.Basic, "BS", "Bottle Shatters", 0.5D, or1);

        EMFTAGate and1 = new EMFTAGate("", new HashSet<>(Arrays.asList(3,4)), EMFTAGate.EMFTAGateType.AND);
        EMFTAEvent bh = new EMFTAEvent(EMFTAEvent.EMFTAEventType.Basic, "BH", "Billy Hits", 0.1D, and1);

        EMFTAGate and2 = new EMFTAGate("", new HashSet<>(Arrays.asList(5)), EMFTAGate.EMFTAGateType.AND);
        EMFTAEvent sh = new EMFTAEvent(EMFTAEvent.EMFTAEventType.Basic, "SH", "Suzy Hits", 0.01D, and2);
        EMFTAEvent bt = new EMFTAEvent(EMFTAEvent.EMFTAEventType.Basic, "BT", "Billy Throws", 0.001D, null);
        EMFTAEvent notSh = new EMFTAEvent(EMFTAEvent.EMFTAEventType.Basic, "-SH", "Suzy does not hit", 0.0011D,
                null);
        EMFTAEvent st = new EMFTAEvent(EMFTAEvent.EMFTAEventType.Basic, "ST", "Suzy throws", 0.0012D, null);

        List<EMFTAEvent> events = new ArrayList<>(Arrays.asList(bs, bh, sh, bt, notSh, st));
        EMFTAFTAModel billySuzy = new EMFTAFTAModel("BillySuzy", "", null, 0, events);
        return billySuzy;
    }

    public static FaultTreeDefinition billySuzyMEF() {
        Gate bh = new Gate("BH");
        Gate sh = new Gate("SH");
        List<Formula> formulasBs = new ArrayList<>(Arrays.asList(bh, sh));
        BasicBooleanOperator or1 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.or, formulasBs);
        GateDefinition bsDef = new GateDefinition("BS", or1);

        BasicEvent bt = new BasicEvent("BT");
        BasicEvent notSh = new BasicEvent("-SH");
        List<Formula> formulasBh = new ArrayList<>(Arrays.asList(bt, notSh));
        BasicBooleanOperator and1 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.and, formulasBh);
        GateDefinition bhDef = new GateDefinition("BH", and1);

        BasicEvent st = new BasicEvent("ST");
        List<Formula> formulasSh = new ArrayList<>(Arrays.asList(st));
        BasicBooleanOperator and2 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.and, formulasSh);
        GateDefinition shDef = new GateDefinition("SH", and2);

        FloatConstant f1 = new FloatConstant(0.001);
        BasicEventDefinition btDef = new BasicEventDefinition("BT", f1);

        FloatConstant f2 = new FloatConstant(0.0011);
        BasicEventDefinition notShDef = new BasicEventDefinition("-SH", f2);

        FloatConstant f3 = new FloatConstant(0.0012);
        BasicEventDefinition stDef = new BasicEventDefinition("ST", f3);

        List<ElementDefinition> elementDefinitions = new ArrayList<>(Arrays.asList(bsDef, bhDef, shDef, btDef,
                notShDef, stDef));
        FaultTreeDefinition billySuzy = new FaultTreeDefinition("BillySuzy", null, null, elementDefinitions);
        return billySuzy;
    }

    public static FaultTreeDefinition banking() {

        BasicEvent e1 = new BasicEvent("get tan");
        BasicEvent e2 = new BasicEvent("get password");
        List<Formula> f1 = Arrays.asList(e2, e1);
        BasicBooleanOperator and1 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.and, f1);
        GateDefinition gateDef1 = new GateDefinition("obtain online", and1);

        BasicEvent e3 = new BasicEvent("hijack bank server");
        BasicEvent e4 = new BasicEvent("initial transfer via debit card");
        Gate g0 = new Gate("obtain online");
        List<Formula> f2 = Arrays.asList(g0, e3, e4);
        BasicBooleanOperator or1 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.or, f2);
        GateDefinition gateDef2 = new GateDefinition("Transfer Money out of account", or1);

        BasicEventDefinition e1Def = new BasicEventDefinition("get tan");
        BasicEventDefinition e2Def = new BasicEventDefinition("get password");
        BasicEventDefinition e3Def = new BasicEventDefinition("hijack bank server");
        BasicEventDefinition e4Def = new BasicEventDefinition("initial transfer via debit card");
        List<ElementDefinition> elementDefinitions = Arrays.asList(gateDef2,gateDef1, e2Def, e1Def, e3Def, e4Def);
        FaultTreeDefinition banking = new FaultTreeDefinition("Transfer Money out of account", null, null, elementDefinitions);
        return banking;
    }

    public static CausalModel billySuzyCausalModel() {
        ExogenousVariable stExo = new ExogenousVariable("ST_exo");
        EndogenousVariable st = new EndogenousVariable("ST", stExo, 0.0012);
        ExogenousVariable btExo = new ExogenousVariable("BT_exo");
        EndogenousVariable bt = new EndogenousVariable("BT", btExo, 0.001);
        ExogenousVariable noShExo = new ExogenousVariable("-SH_exo");
        EndogenousVariable notSh = new EndogenousVariable("-SH", noShExo, 0.0011);

        BasicBooleanOperator and1 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.and,
                Arrays.asList(bt, notSh));
        EndogenousVariable bh = new EndogenousVariable("BH", and1);

        BasicBooleanOperator and2 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.and, Arrays.asList(st));
        EndogenousVariable sh = new EndogenousVariable("SH", and2);

        BasicBooleanOperator or1 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.or,
                Arrays.asList(bh, sh));
        EndogenousVariable bs = new EndogenousVariable("BS", or1);

        Set<Variable> variables = new HashSet<>(Arrays.asList(bs, bh, sh, bt, notSh, st, btExo, noShExo, stExo));
        CausalModel billySuzy = new CausalModel("BillySuzy", variables);
        return billySuzy;
    }
}
