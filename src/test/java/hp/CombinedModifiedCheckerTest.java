package hp;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import causality.CausalModel;
import util.ModelProvider;

public class CombinedModifiedCheckerTest {

	CausalModel billySuzyCausalModel, billySuzyCausalModel2;
	BindableModifiedChecker bindableChecker;
	ModifiedChecker checker;
    @Before
    public void setUp() throws Exception {
        billySuzyCausalModel = ModelProvider.billySuzyCausalModelNoNegation();
        billySuzyCausalModel2 = ModelProvider.billySuzyCausalModelNoNegation();
        
        bindableChecker = new BindableModifiedChecker(billySuzyCausalModel);
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
	public void testTwoImplsConiditonOne() {
	
		assertTrue("condition one is not equal in the two impls", checker.checkConditionOne(billySuzyCausalModel.getVariableByName("ST"),true,billySuzyCausalModel.getVariableByName("BS"), true) == bindableChecker.checkConditionOne(billySuzyCausalModel.getVariableByName("ST"),true,billySuzyCausalModel.getVariableByName("BS"), true));
		assertTrue("condition one is not equal in the two impls", checker.checkConditionOne(billySuzyCausalModel.getVariableByName("BT"),true,billySuzyCausalModel.getVariableByName("BH"), false) == bindableChecker.checkConditionOne(billySuzyCausalModel.getVariableByName("BT"),true,billySuzyCausalModel.getVariableByName("BH"), false));
	}
	@Test
	public void testTwoImplsConiditonTwo() {
		
		assertTrue("condition one failed",checker.checkConditionTwo(billySuzyCausalModel.getVariableByName("BT"),true,billySuzyCausalModel.getVariableByName("BS"), true).containsAll(bindableChecker.checkConditionTwo(billySuzyCausalModel.getVariableByName("BT"),true,billySuzyCausalModel.getVariableByName("BS"), true)));
		
		assertTrue("condition one failed",checker.checkConditionTwo(billySuzyCausalModel.getVariableByName("ST"),true,billySuzyCausalModel.getVariableByName("BS"),false).containsAll(bindableChecker.checkConditionTwo(billySuzyCausalModel.getVariableByName("ST"),true,billySuzyCausalModel.getVariableByName("BS"),false)));
		
		
	}
//	@Ignore
//	@Test
//	public void testCheckConditionThree() {
//		fail("Not yet implemented");
//	}

}
