package causality;

import mef.faulttree.FaultTreeDefinition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.ModelProvider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class CausalModelTest {
    CausalModel billySuzyCausalModel;


    @Before
    public void setUp() throws Exception {
        billySuzyCausalModel = ModelProvider.billySuzyCausalModel();
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
}
