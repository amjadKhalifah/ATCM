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

        FloatConstant fBS = new FloatConstant(0.5);
        GateDefinition bsDef = new GateDefinition("BS", or1, fBS);

        BasicEvent bt = new BasicEvent("BT");
        BasicEvent notSh = new BasicEvent("-SH");
        List<Formula> formulasBh = new ArrayList<>(Arrays.asList(bt, notSh));
        BasicBooleanOperator and1 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.and, formulasBh);

        FloatConstant fBH = new FloatConstant(0.1);
        GateDefinition bhDef = new GateDefinition("BH", and1, fBH);

        BasicEvent st = new BasicEvent("ST");
        List<Formula> formulasSh = new ArrayList<>(Arrays.asList(st));
        BasicBooleanOperator and2 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.and, formulasSh);

        FloatConstant fSH = new FloatConstant(0.01);
        GateDefinition shDef = new GateDefinition("SH", and2, fSH);

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

    public static CausalModel stealMasterKeyUnfoldedWithPreemption() {
        // Unfolded: U1 Decrypt The Key
        ExogenousVariable u1FSExo = new ExogenousVariable("U1 From Script_exo");
        EndogenousVariable u1FS = new EndogenousVariable("U1 From Script", u1FSExo);
        ExogenousVariable u1FNExo = new ExogenousVariable("U1 From Network_exo");
        EndogenousVariable u1FN = new EndogenousVariable("U1 From Network", u1FNExo);
        ExogenousVariable u1FFExo = new ExogenousVariable("U1 From File_exo");
        EndogenousVariable u1FF = new EndogenousVariable("U1 From File", u1FFExo);
        ExogenousVariable u1FDExo = new ExogenousVariable("U1 From DB_exo");
        EndogenousVariable u1FD = new EndogenousVariable("U1 From DB", u1FDExo);
        EndogenousVariable u1GTP = new EndogenousVariable("U1 Get The Passphrase", new BasicBooleanOperator
                (BasicBooleanOperator.OperatorType.or, Arrays.asList(u1FS, u1FN)));
        EndogenousVariable u1GTK = new EndogenousVariable("U1 Get The Key", new BasicBooleanOperator
                (BasicBooleanOperator.OperatorType.or, Arrays.asList(u1FF, u1FD)));
        EndogenousVariable u1DTK = new EndogenousVariable("U1 Decrypt The Key", new BasicBooleanOperator
                (BasicBooleanOperator.OperatorType.and, Arrays.asList(u1GTP, u1GTK)));

        // Unfolded: U2 Decrypt The Key
        ExogenousVariable u2FSExo = new ExogenousVariable("U2 From Script_exo");
        EndogenousVariable u2FS = new EndogenousVariable("U2 From Script", u2FSExo);
        ExogenousVariable u2FNExo = new ExogenousVariable("U2 From Network_exo");
        EndogenousVariable u2FN = new EndogenousVariable("U2 From Network", u2FNExo);
        ExogenousVariable u2FFExo = new ExogenousVariable("U2 From File_exo");
        EndogenousVariable u2FF = new EndogenousVariable("U2 From File", u2FFExo);
        ExogenousVariable u2FDExo = new ExogenousVariable("U2 From DB_exo");
        EndogenousVariable u2FD = new EndogenousVariable("U2 From DB", u2FDExo);
        EndogenousVariable u2GTP = new EndogenousVariable("U2 Get The Passphrase", new BasicBooleanOperator
                (BasicBooleanOperator.OperatorType.or, Arrays.asList(u2FS, u2FN)));
        EndogenousVariable u2GTK = new EndogenousVariable("U2 Get The Key", new BasicBooleanOperator
                (BasicBooleanOperator.OperatorType.or, Arrays.asList(u2FF, u2FD)));
        EndogenousVariable u2DTK = new EndogenousVariable("U2 Decrypt The Key", new BasicBooleanOperator
                (BasicBooleanOperator.OperatorType.and, Arrays.asList(u2GTP, u2GTK)));

        // Unfolded: U3 Decrypt The Key
        ExogenousVariable u3FSExo = new ExogenousVariable("U3 From Script_exo");
        EndogenousVariable u3FS = new EndogenousVariable("U3 From Script", u3FSExo);
        ExogenousVariable u3FNExo = new ExogenousVariable("U3 From Network_exo");
        EndogenousVariable u3FN = new EndogenousVariable("U3 From Network", u3FNExo);
        ExogenousVariable u3FFExo = new ExogenousVariable("U3 From File_exo");
        EndogenousVariable u3FF = new EndogenousVariable("U3 From File", u3FFExo);
        ExogenousVariable u3FDExo = new ExogenousVariable("U3 From DB_exo");
        EndogenousVariable u3FD = new EndogenousVariable("U3 From DB", u3FDExo);
        EndogenousVariable u3GTP = new EndogenousVariable("U3 Get The Passphrase", new BasicBooleanOperator
                (BasicBooleanOperator.OperatorType.or, Arrays.asList(u3FS, u3FN)));
        EndogenousVariable u3GTK = new EndogenousVariable("U3 Get The Key", new BasicBooleanOperator
                (BasicBooleanOperator.OperatorType.or, Arrays.asList(u3FF, u3FD)));
        EndogenousVariable u3DTK = new EndogenousVariable("U3 Decrypt The Key", new BasicBooleanOperator
                (BasicBooleanOperator.OperatorType.and, Arrays.asList(u3GTP, u3GTK)));

        // Unfolded: U1 Steal Decrypted
        ExogenousVariable u1AExo = new ExogenousVariable("U1 Access_exo");
        EndogenousVariable u1A = new EndogenousVariable("U1 Access", u1AExo);
        ExogenousVariable u1ADExo = new ExogenousVariable("U1 Attach Debugger_exo");
        EndogenousVariable u1AD = new EndogenousVariable("U1 Attach Debugger", u1ADExo);
        EndogenousVariable u1FKMS = new EndogenousVariable("U1 From Key Management Service", new
                BasicBooleanOperator(BasicBooleanOperator.OperatorType.and, Arrays.asList(u1A, u1AD)));
        EndogenousVariable u1SD = new EndogenousVariable("U1 Steal Decrypted", new BasicBooleanOperator
                (BasicBooleanOperator.OperatorType.or, Arrays.asList(u1FKMS)));

        // Unfolded: U2 Steal Decrypted
        ExogenousVariable u2AExo = new ExogenousVariable("U2 Access_exo");
        EndogenousVariable u2A = new EndogenousVariable("U2 Access", u2AExo);
        ExogenousVariable u2ADExo = new ExogenousVariable("U2 Attach Debugger_exo");
        EndogenousVariable u2AD = new EndogenousVariable("U2 Attach Debugger", u2ADExo);
        EndogenousVariable u2FKMS = new EndogenousVariable("U2 From Key Management Service", new
                BasicBooleanOperator(BasicBooleanOperator.OperatorType.and, Arrays.asList(u2A, u2AD)));
        EndogenousVariable u2SD = new EndogenousVariable("U2 Steal Decrypted", new BasicBooleanOperator
                (BasicBooleanOperator.OperatorType.or, Arrays.asList(u2FKMS)));

        // Unfolded: U1 Steal Decrypted
        ExogenousVariable u3AExo = new ExogenousVariable("U3 Access_exo");
        EndogenousVariable u3A = new EndogenousVariable("U3 Access", u3AExo);
        ExogenousVariable u3ADExo = new ExogenousVariable("U3 Attach Debugger_exo");
        EndogenousVariable u3AD = new EndogenousVariable("U3 Attach Debugger", u3ADExo);
        EndogenousVariable u3FKMS = new EndogenousVariable("U3 From Key Management Service", new
                BasicBooleanOperator(BasicBooleanOperator.OperatorType.and, Arrays.asList(u3A, u3AD)));
        EndogenousVariable u3SD = new EndogenousVariable("U3 Steal Decrypted", new BasicBooleanOperator
                (BasicBooleanOperator.OperatorType.or, Arrays.asList(u3FKMS)));

        // Non-user-specific
        EndogenousVariable dtk = new EndogenousVariable("Decrypt The Key", new BasicBooleanOperator
                (BasicBooleanOperator.OperatorType.or, Arrays.asList(u1DTK, u2DTK, u3DTK)));
        EndogenousVariable sd = new EndogenousVariable("Steal Decrypted", new BasicBooleanOperator
                (BasicBooleanOperator.OperatorType.or, Arrays.asList(u1SD, u2SD, u3SD)));
        EndogenousVariable smk = new EndogenousVariable("Steal Master Key", new BasicBooleanOperator
                (BasicBooleanOperator.OperatorType.or, Arrays.asList(dtk, sd)));

        // all variables
        Set<Variable> variables = new HashSet<>(Arrays.asList(
                u1FS, u1FSExo, u1FN, u1FNExo, u1FF, u1FFExo, u1FD, u1FDExo, u1GTP, u1GTK, u1DTK,
                u2FS, u2FSExo, u2FN, u2FNExo, u2FF, u2FFExo, u2FD, u2FDExo, u2GTP, u2GTK, u2DTK,
                u3FS, u3FSExo, u3FN, u3FNExo, u3FF, u3FFExo, u3FD, u3FDExo, u3GTP, u3GTK, u3DTK,
                u1A, u1AExo, u1AD, u1ADExo, u1FKMS, u1SD,
                u2A, u2AExo, u2AD, u2ADExo, u2FKMS, u2SD,
                u3A, u3AExo, u3AD, u3ADExo, u3FKMS, u3SD,
                dtk, sd, smk));
        CausalModel causalModel = new CausalModel("Steal Master Key", variables);
        return causalModel;
    }
}
