package mef;

import mef.faulttree.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import parser.emfta.EMFTAParser;
import util.ModelProvider;
import util.Util;

import java.io.File;
import java.net.URL;

import static org.junit.Assert.assertEquals;

public class MEFFaultTreeXMLTest {
    URL urlFaultTree1 = this.getClass().getClassLoader().getResource("fault_tree_1.xml");
    URL urlFaultTree2 = this.getClass().getClassLoader().getResource("fault_tree_2.xml");
    URL urlBillySuzy = this.getClass().getClassLoader().getResource("Suzy_Billy.xml");
    URL urlBillySuzyNoProbEMFTA = this.getClass().getClassLoader().getResource("Suzy_Billy_noprob.emfta");
    URL urlBillySuzyNoProbMEF = this.getClass().getClassLoader().getResource("Suzy_Billy_noprob.xml");

    String faultTree1MEF;
    String faultTree2MEF2;
    String faultTreeBillySuzyMEF;
    String faultTreeBillySuzy_noprobMEF;

    FaultTreeDefinition faultTree1;
    FaultTreeDefinition faultTree2;
    FaultTreeDefinition billySuzy;
    FaultTreeDefinition billySuzyNoProb;


    @Before
    public void setUp() throws Exception {
        faultTree1MEF = Util.fileToString(urlFaultTree1.toString())
                .replace(" ", "").replace("\r","");
        faultTree2MEF2 = Util.fileToString(urlFaultTree2)
                .replace(" ", "").replace("\r","");
        faultTreeBillySuzyMEF = Util.fileToString(urlBillySuzy)
                .replace(" ", "").replace("\r","");
        faultTreeBillySuzy_noprobMEF = Util.fileToString(urlBillySuzyNoProbMEF)
                .replace(" ", "").replace("\r","");

        faultTree1 = ModelProvider.faultTree1();
        faultTree2 = ModelProvider.faultTree2();
        billySuzy = ModelProvider.billySuzyMEF();
        billySuzyNoProb = (new EMFTAParser()).toMEF(new File(urlBillySuzyNoProbEMFTA.getPath()));
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void faultTreeToXML() {
        String faultTree1Str = faultTree1.toXML().asXML()
                .replace(" ", "")
                .replace("\n", "")
                .replace(">", ">\n");
        // remove last \n
        faultTree1Str = faultTree1Str.substring(0, faultTree1Str.length() - 1);

        String faultTree2Str = faultTree2.toXML().asXML()
                .replace(" ", "")
                .replace("\n", "")
                .replace(">", ">\n");
        faultTree2Str = faultTree2Str.substring(0, faultTree2Str.length() - 1);

        String billySuzyStr = billySuzy.toXML().asXML()
                .replace(" ", "")
                .replace("\n", "")
                .replace(">", ">\n");
        billySuzyStr = billySuzyStr.substring(0, billySuzyStr.length() - 1);

        String billySuzyNoProbStr = billySuzyNoProb.toXML().asXML()
                .replace(" ", "")
                .replace("\n", "")
                .replace(">", ">\n");
        billySuzyNoProbStr = billySuzyNoProbStr.substring(0, billySuzyNoProbStr.length() - 1);

        assertEquals(faultTree1MEF, faultTree1Str);
        assertEquals(faultTree2MEF2, faultTree2Str);
        assertEquals(faultTreeBillySuzyMEF, billySuzyStr);
        assertEquals(faultTreeBillySuzy_noprobMEF, billySuzyNoProbStr);
    }
}
