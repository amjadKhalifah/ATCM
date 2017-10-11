package parser.adtool;

import mef.faulttree.FaultTreeDefinition;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import util.ModelProvider;

import java.io.File;

import static org.junit.Assert.*;

public class ADTParserTest {
    File bankingXML = new File(this.getClass().getClassLoader().getResource("banking.adt").getPath().replaceAll("%20", " "));
    ADTParser ADTParser = new ADTParser();

    FaultTreeDefinition banking;

    @Before
    public void setUp() throws Exception {
        banking = ModelProvider.banking();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void Should_ReturnFaultTreeDefinition() throws Exception {
        FaultTreeDefinition bankingActual = ADTParser.toMEF(bankingXML, null);
        String s1 = banking.toXML().asXML().replace(" ", "")
                .replace("\n", "")
                .replace(">", ">\n");;
        String s2 = bankingActual.toXML().asXML().replace(" ", "")
                .replace("\n", "")
                .replace(">", ">\n");;
        assertEquals(s1, s2);
        assertEquals(banking, bankingActual);
    }

}