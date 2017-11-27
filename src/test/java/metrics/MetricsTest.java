package metrics;

import attacker_attribution.User;
import causality.CausalModel;
import org.junit.Before;
import org.junit.Test;
import parser.adtool.ADTNode;
import parser.adtool.ADTParser;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class MetricsTest {
    ADTNode stealMasterKey;
    ADTNode stealMasterKeyUnfolded;

    CausalModel causalModelStealMasterKey;
    CausalModel causalModelStealMasterKeyUnfolded;

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

        User u1 = new User("1", "U1", "admin", 2, null);
        User u2 = new User("2", "U2", "admin", 1, null);
        User u3 = new User("3", "U3", "admin", 0, null);
        Set<User> users = new HashSet<>(Arrays.asList(u1, u2, u3));

        causalModelStealMasterKey = CausalModel.fromMEF(adtParser.toMEF(stealMasterKeyXML, null));
        causalModelStealMasterKeyUnfolded = CausalModel.fromMEF(adtParser.toMEF(stealMasterKeyXML, users), users);

    }

    @Test
    public void should_ReturnMetrics_For_StealMasterKeyAttackTree() throws Exception {
        Metrics metrics = new Metrics(12, 11, 6);
        Metrics metricsActual = new Metrics(stealMasterKey);
        assertEquals(metrics, metricsActual);
    }

    @Test
    public void should_ReturnMetrics_For_StealMasterKeyUnfoldedAttackTree() throws Exception {
        Metrics metrics = new Metrics(36, 35, 18);
        Metrics metricsActual = new Metrics(stealMasterKeyUnfolded);
        assertEquals(metrics, metricsActual);
    }

    @Test
    public void should_ReturnMetrics_For_StealMasterKeyCausalModel() throws Exception {
        Metrics metrics = new Metrics(18, 17, 6);
        Metrics metricsActual = new Metrics(causalModelStealMasterKey);
        assertEquals(metrics, metricsActual);
    }

    @Test
    public void should_ReturnMetrics_For_StealMasterKeyUnfoldedCausalModel() throws Exception {
        Metrics metrics = new Metrics(54, 59, 18);
        Metrics metricsActual = new Metrics(causalModelStealMasterKeyUnfolded);
        assertEquals(metrics, metricsActual);
    }
}