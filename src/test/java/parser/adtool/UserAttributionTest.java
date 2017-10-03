package parser.adtool;

import attacker_attribution.User;
import org.junit.Before;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.*;

public class UserAttributionTest {
    File stealMasterKeyXML;
    File stealMasterKeyUnfoldedXML;
    ADTParser ADTParser;
    Set<User> users;

    @Before
    public void setUp() throws Exception {
        stealMasterKeyXML = new File(this.getClass().getClassLoader()
                .getResource("user_attribution/Steal_Master_Key.xml").getPath()
                .replaceAll("%20", " "));
        stealMasterKeyUnfoldedXML = new File(this.getClass().getClassLoader()
                .getResource("user_attribution/Steal_Master_Key_unfolded.xml").getPath()
                .replaceAll("%20", " "));
        ADTParser = new ADTParser();

        User u1 = new User("1", "U1", "admin", null);
        User u2 = new User("2", "U2", "admin", null);
        users = new HashSet<>(Arrays.asList(u1, u2));
    }

    @Test
    public void shouldCopyNode() throws Exception {
        ADTNode tree = ADTParser.fromAD(stealMasterKeyXML);
        ADTNode treeCloned = new ADTNode(tree);

        assertEquals(tree, treeCloned);
        assertFalse(tree == treeCloned);
    }

    @Test
    public void shouldUnfoldTree() throws Exception {
        ADTNode tree = ADTParser.fromAD(stealMasterKeyXML);
        ADTNode treeUnfolded = ADTParser.fromAD(stealMasterKeyUnfoldedXML);
        ADTNode treeUnfoldedActual = tree.unfold(users);
        assertEquals(treeUnfolded, treeUnfoldedActual);
    }
}
