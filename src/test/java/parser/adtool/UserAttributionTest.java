package parser.adtool;

import attacker_attribution.User;
import attacker_attribution.UserParser;
import org.junit.Before;
import org.junit.Test;
import util.Util;

import java.io.File;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static junit.framework.TestCase.*;

public class UserAttributionTest {
    File stealMasterKeyXML;
    File stealMasterKeyUnfoldedXML;
    File stealMasterKeyUnfoldedNewXML;
    File usersXML;
    ADTParser ADTParser;
    Set<User> users;

    @Before
    public void setUp() throws Exception {
        stealMasterKeyXML = new File(this.getClass().getClassLoader()
                .getResource("user_attribution/Steal_Master_Key.adt").getPath()
                .replaceAll("%20", " "));
        stealMasterKeyUnfoldedXML = new File(this.getClass().getClassLoader()
                .getResource("user_attribution/Steal_Master_Key_unfolded.adt").getPath()
                .replaceAll("%20", " "));
        stealMasterKeyUnfoldedNewXML = new File(this.getClass().getClassLoader()
                .getResource("user_attribution/Steal_Master_Key_unfolded_new.adt").getPath()
                .replaceAll("%20", " "));
        usersXML = new File(this.getClass().getClassLoader()
                .getResource("user_attribution/users.xml").getPath().replaceAll("%20", " "));

        ADTParser = new ADTParser();

        User u1 = new User("1", "U1", "admin", 2, null);
        User u2 = new User("2", "U2", "admin", 1, null);
        User u3 = new User("3", "U3", "admin", 0, null);
        users = new HashSet<>(Arrays.asList(u1, u2, u3));
    }

    @Test
    public void shouldCopyNode() throws Exception {
        ADTNode tree = ADTParser.fromAD(stealMasterKeyXML);
        ADTNode treeCloned = new ADTNode(tree);

        assertEquals(tree, treeCloned);
        assertFalse(tree == treeCloned);
    }

    @Test
    public void shouldUnfoldTreeNew() throws Exception {
        ADTNode tree = ADTParser.fromAD(stealMasterKeyXML);
        ADTNode treeUnfolded = ADTParser.fromAD(stealMasterKeyUnfoldedNewXML);
        ADTNode treeUnfoldedActual = tree.unfold(users, new int[] {0,1});
        assertEquals(treeUnfolded, treeUnfoldedActual);

        // compare strings; helpful if previous assertion fails
        String treeUnfoldedStr = treeUnfolded.toXML().asXML();
        treeUnfoldedStr = treeUnfoldedStr
                .replace(" ", "")
                .replace("\n", "")
                .replace(">", ">\n")
                .replace("<label>\n", "<label>").trim();
        String treeUnfoldedActualStr = treeUnfoldedActual.toXML().asXML();
        treeUnfoldedActualStr = treeUnfoldedActualStr
                .replace(" ", "")
                .replace("\n", "")
                .replace(">", ">\n")
                .replace("<label>\n", "<label>").trim();
        assertEquals(treeUnfoldedStr, treeUnfoldedActualStr);

        /*
        if config is invalid (or empty as in this case), the unfolding should be equal to the old version, i.e.
        unfold at the very top
         */
        ADTNode tree2 = ADTParser.fromAD(stealMasterKeyXML);
        ADTNode treeUnfolded2 = ADTParser.fromAD(stealMasterKeyUnfoldedXML);
        ADTNode treeUnfoldedActual2 = tree2.unfold(users, new int[] {});
        assertEquals(treeUnfolded2, treeUnfoldedActual2);
    }

    @Test
    @Deprecated
    public void shouldUnfoldTree() throws Exception {
        ADTNode tree = ADTParser.fromAD(stealMasterKeyXML);
        ADTNode treeUnfolded = ADTParser.fromAD(stealMasterKeyUnfoldedXML);
        ADTNode treeUnfoldedActual = tree.unfold(users);
        assertEquals(treeUnfolded, treeUnfoldedActual);

        // compare strings; helpful if previous assertion fails
        String treeUnfoldedStr = treeUnfolded.toXML().asXML();
        treeUnfoldedStr = treeUnfoldedStr
                .replace(" ", "")
                .replace("\n", "")
                .replace(">", ">\n")
                .replace("<label>\n", "<label>").trim();
        String treeUnfoldedActualStr = treeUnfoldedActual.toXML().asXML();
        treeUnfoldedActualStr = treeUnfoldedActualStr
                .replace(" ", "")
                .replace("\n", "")
                .replace(">", ">\n")
                .replace("<label>\n", "<label>").trim();
        assertEquals(treeUnfoldedStr, treeUnfoldedActualStr);
    }

    @Test
    public void shouldGenerateXML() throws Exception {
        String stealMasterKeyXMLStr = Util.fileToString(stealMasterKeyXML.toURI().toURL())
                .replace(" ", "").replace("\t","");
        String stealMasterKeyUnfoldedXMLStr = Util.fileToString(stealMasterKeyUnfoldedXML.toURI().toURL())
                .replace(" ", "").replace("\t","");

        String stealMasterKeyXMLStrActual = ADTParser.fromAD(stealMasterKeyXML).toXML().asXML()
                .replace(" ", "")
                .replace("\n", "")
                .replace(">", ">\n")
                .replace("<label>\n", "<label>").trim();
        String stealMasterKeyXMLUnfoldedStrActual = ADTParser.fromAD(stealMasterKeyUnfoldedXML).toXML().asXML()
                .replace(" ", "")
                .replace("\n", "")
                .replace(">", ">\n")
                .replace("<label>\n", "<label>").trim();

        assertEquals(stealMasterKeyXMLStr, stealMasterKeyXMLStrActual);
        assertEquals(stealMasterKeyUnfoldedXMLStr, stealMasterKeyXMLUnfoldedStrActual);
    }

    @Test
    public void shouldReturnUsers() throws Exception {
        Set<User> usersActual = UserParser.parse(usersXML);
        assertEquals(users, usersActual);
    }
}
