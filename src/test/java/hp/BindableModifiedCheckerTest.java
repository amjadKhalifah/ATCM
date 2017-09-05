package hp;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import causality.CausalModel;
import util.GuavaPowerSet;
import util.ModelProvider;

public class BindableModifiedCheckerTest {

	CausalModel billySuzyCausalModel;
	BindableModifiedChecker checker;

    @Before
    public void setUp() throws Exception {
        billySuzyCausalModel = ModelProvider.billySuzyCausalModelRealNegation();
        checker = new BindableModifiedChecker(billySuzyCausalModel,new GuavaPowerSet());

    	// in bindable version we only need to set the exogenous values
		Map<String, Boolean> actualValues = new HashMap<String, Boolean>() {
			{
				put("ST_exo", true);
				put("BT_exo", true);
				
//				put("exo1", true);
//				put("exo2", true);
//				put("exo3", true);
//				put("exo2", true);
			}
		};
		// in bindable version we only need to set the exogenous values
		checker.setExovalues( actualValues);
        
    }


	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsCause() {
	
		assertFalse("is cause failed",checker.isCause(billySuzyCausalModel.getVariableByName("ST"),true,billySuzyCausalModel.getVariableByName("BS"), true).isEmpty());
		assertTrue("is cause failed",checker.isCause(billySuzyCausalModel.getVariableByName("BT"),true,billySuzyCausalModel.getVariableByName("BS"), true).isEmpty());
	}
	@Test
	public void testFindCause() {
	
		assertFalse("find cause failed",checker.findCause(billySuzyCausalModel.getVariableByName("BS")).isEmpty());
	}
	
	@Test
	public void testCheckConditionOne() {
	
		assertTrue("condition one failed",checker.checkConditionOne(billySuzyCausalModel.getVariableByName("ST"),true,billySuzyCausalModel.getVariableByName("BS"), true));
		assertTrue("condition one failed",checker.checkConditionOne(billySuzyCausalModel.getVariableByName("BT"),true,billySuzyCausalModel.getVariableByName("BH"), false));
		
		
	}
	@Test
	public void testCheckConditionTwo() {
		
		assertFalse("condition one failed",!checker.checkConditionTwo(billySuzyCausalModel.getVariableByName("BT"),true,billySuzyCausalModel.getVariableByName("BS"), true).isEmpty());
		
		assertTrue("condition one failed",!checker.checkConditionTwo(billySuzyCausalModel.getVariableByName("ST"),true,billySuzyCausalModel.getVariableByName("BS"),false).isEmpty());
		

		
	}
//	@Ignore
//	@Test
//	public void testCheckConditionThree() {
//		fail("Not yet implemented");
//	}

}
