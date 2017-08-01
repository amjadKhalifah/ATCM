package hp;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import causality.CausalModel;
import util.ModelProvider;

public class ModifiedCheckerTest {

	CausalModel billySuzyCausalModel;
    HPChecker checker;

    @Before
    public void setUp() throws Exception {
        billySuzyCausalModel = ModelProvider.billySuzyCausalModel();
         checker = new ModifiedChecker(billySuzyCausalModel);
         
    }


	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCheckConditionOne() {
		
		assertTrue("condition one failed",checker.checkConditionOne("SH", "BS", true, true));
	}
	@Ignore
	@Test
	public void testCheckConditionTwo() {
		fail("Not yet implemented");
	}
	@Ignore
	@Test
	public void testCheckConditionThree() {
		fail("Not yet implemented");
	}

}
