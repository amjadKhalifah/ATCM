package metrics;

import org.junit.Before;
import org.junit.Test;
import parser.adtool.ADTNode;
import parser.adtool.ADTParser;

import java.io.File;

import static org.junit.Assert.*;

public class MetricsTest {
    ADTNode stealMasterKey;
    ADTNode stealMasterKeyUnfolded;

    @Before
    public void setUp() throws Exception {
        ADTParser adtParser = new ADTParser();
        File stealMasterKeyXML = new File(this.getClass().getClassLoader()
                .getResource("user_attribution/Steal_Master_Key.adt").getPath()
                .replaceAll("%20", " "));
        File stealMasterKeyUnfoldedXML = new File(this.getClass().getClassLoader()
                .getResource("user_attribution/Steal_Master_Key_unfolded.adt").getPath()
                .replaceAll("%20", " "));
        stealMasterKey = adtParser.fromAD(stealMasterKeyXML);
        stealMasterKeyUnfolded = adtParser.fromAD(stealMasterKeyUnfoldedXML);
    }

    @Test
    public void should_ReturnMetrics_For_StealMasterKeyAttackTree() throws Exception {
        Metrics metrics = new Metrics(12,11);
        Metrics metricsActual = new Metrics(stealMasterKey);
        assertEquals(metrics, metricsActual);
    }

    @Test
    public void should_ReturnMetrics_For_StealMasterKeyUnfoldedAttackTree() throws Exception {
        Metrics metrics = new Metrics(36,35);
        Metrics metricsActual = new Metrics(stealMasterKeyUnfolded);
        assertEquals(metrics, metricsActual);
    }
}