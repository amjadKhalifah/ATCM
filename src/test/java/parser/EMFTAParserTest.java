package parser;

import mef.faulttree.FaultTreeDefinition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import parser.emfta.EMFTAFTAModel;
import parser.emfta.EMFTAParser;
import util.ModelProvider;

import java.io.File;

import static org.junit.Assert.assertEquals;


public class EMFTAParserTest {
	
    File billySuzy = new File(this.getClass().getClassLoader().getResource("Suzy_Billy.emfta").getPath().replaceAll("%20", " "));
    EMFTAParser emftaParser = new EMFTAParser();

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void test_fromEMFTA() throws Exception {
        EMFTAFTAModel emftaftaModelActual = emftaParser.fromEMFTA(billySuzy);
        EMFTAFTAModel emftaftaModelExpected = ModelProvider.billySuzyEMFTA();

        assertEquals(emftaftaModelExpected, emftaftaModelActual);
    }

    @Test
    public void test_toMEF() throws Exception {
        FaultTreeDefinition ftDefActual = emftaParser.toMEF(billySuzy);
        FaultTreeDefinition ftDefExpected = ModelProvider.billySuzyMEF();

        assertEquals(ftDefExpected, ftDefActual);
    }
}