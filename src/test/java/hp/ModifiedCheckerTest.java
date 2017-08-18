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
	ModifiedChecker checker;

    @Before
    public void setUp() throws Exception {
        billySuzyCausalModel = ModelProvider.billySuzyCausalModelNoNegation();
        System.out.println(billySuzyCausalModel.toReport());
         checker = new ModifiedChecker(billySuzyCausalModel);
    }


	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testFindCause() {
	
		assertFalse("find cause failed",checker.findCause(billySuzyCausalModel.getVariableByName("BS")).isEmpty());
	}
	
	@Test
	public void testCheckConditionOne() {
	
		assertTrue("condition one failed",checker.checkConditionOne(billySuzyCausalModel.getVariableByName("ST"),true,billySuzyCausalModel.getVariableByName("BS"), true));
	}
	@Test
	public void testCheckConditionTwo() {
		
		assertFalse("condition one failed",!checker.checkConditionTwo(billySuzyCausalModel.getVariableByName("BT"),true,billySuzyCausalModel.getVariableByName("BS"), true).isEmpty());
		
		assertTrue("condition one failed",!checker.checkConditionTwo(billySuzyCausalModel.getVariableByName("ST"),true,billySuzyCausalModel.getVariableByName("BS"), true).isEmpty());
		

		
	}
	@Ignore
	@Test
	public void testCheckConditionThree() {
		fail("Not yet implemented");
	}

}
