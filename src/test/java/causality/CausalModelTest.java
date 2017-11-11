package causality;

import attacker_attribution.User;
import mef.faulttree.FaultTreeDefinition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import parser.adtool.ADTParser;
import util.ModelProvider;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CausalModelTest {
    CausalModel billySuzyCausalModel;
    File stealMasterKeyXML;
    Set<User> users;


    @Before
    public void setUp() throws Exception {
        billySuzyCausalModel = ModelProvider.billySuzyCausalModel();
        stealMasterKeyXML = new File(this.getClass().getClassLoader()
                .getResource("user_attribution/Steal_Master_Key.adt").getPath()
                .replaceAll("%20", " "));

        User u1 = new User("1", "U1", "admin", 2, null);
        User u2 = new User("2", "U2", "admin", 1, null);
        User u3 = new User("3", "U3", "admin", 0, null);
        users = new HashSet<>(Arrays.asList(u1, u2, u3));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_Should_CreateCausaModel() {
        FaultTreeDefinition billySuzyMEF = ModelProvider.billySuzyMEF();
        assertEquals(billySuzyCausalModel, CausalModel.fromMEF(billySuzyMEF));
    }

    @Test
    public void test_Should_CreateReport() {
        String report = billySuzyCausalModel.toReport();
        System.out.println(report);
    }

    @Test
    public void Should_ReturnRootVariables() {
        Variable bs = billySuzyCausalModel.getVariables().stream().filter(v-> v.getName().equals("BS"))
                .findFirst().get();
        Set<Variable> rootVariables = new HashSet<>(Arrays.asList(bs));
        Set<Variable> rootVariablesActual = billySuzyCausalModel.getRootVariables();
        assertEquals(rootVariables, rootVariablesActual);
    }

    @Test
    public void Should_CreateCausalModelWithUserAttributionAndPreemption() throws Exception {
        ADTParser adtParser = new ADTParser();
        CausalModel stealMasterKeyUnfoldedCausalModel = ModelProvider.stealMasterKeyUnfoldedWithPreemption();
        FaultTreeDefinition stealMasterKeyMEF = adtParser.toMEF(stealMasterKeyXML, users);
        CausalModel stealMasterKeyUnfoldedCausalModelActual = CausalModel.fromMEF(stealMasterKeyMEF, users);
        assertEquals(stealMasterKeyUnfoldedCausalModel, stealMasterKeyUnfoldedCausalModelActual);
    }
}
