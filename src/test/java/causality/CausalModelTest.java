package causality;

import mef.faulttree.FaultTreeDefinition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.ModelProvider;

import static org.junit.Assert.assertEquals;

public class CausalModelTest {
    CausalModel billySuzyCausalModel;
    CausalModel billySuzyCausalModelNoNegation;

    @Before
    public void setUp() throws Exception {
        billySuzyCausalModel = ModelProvider.billySuzyCausalModel();
        
        billySuzyCausalModelNoNegation = ModelProvider.billySuzyCausalModelRealNegation();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_Should_CreateCausaModel() {
        FaultTreeDefinition billySuzyNegationMEF = ModelProvider.billySuzyRealNegationMEF();
        assertEquals(billySuzyCausalModelNoNegation, CausalModel.fromMEF(billySuzyNegationMEF));
    }

    @Test
    public void test_Should_CreateReport() {
        String report = billySuzyCausalModel.toReport();
    }
}
