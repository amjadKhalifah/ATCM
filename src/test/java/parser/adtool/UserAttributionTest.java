package parser.adtool;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

import static junit.framework.TestCase.*;

public class UserAttributionTest {
    File stealMasterKeyXML;
    ADTParser ADTParser;

    @Before
    public void setUp() throws Exception {
        stealMasterKeyXML = new File(this.getClass().getClassLoader()
                .getResource("user_attribution/Steal_Master_Key.adt").getPath()
                .replaceAll("%20", " "));
        ADTParser = new ADTParser();
    }

    @Test
    public void shouldUnfoldTree() throws Exception {
        ADTNode tree = ADTParser.fromAD(stealMasterKeyXML);
        assertNull(tree.unfold(null));
    }
}
