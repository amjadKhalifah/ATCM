package parser.adtool;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.*;

public class AttackAttributionTest {


    @Before
    public void setUp() throws Exception {

    }

    @Test
    public void shouldUnfoldTree() throws Exception {
        // TODO parse tree
        ADTNode adtNode = new ADTNode(null, null, null, 0);
        assertNull(adtNode.unfold(null, null));
    }
}
