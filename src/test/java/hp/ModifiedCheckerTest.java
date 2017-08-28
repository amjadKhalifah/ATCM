package hp;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import causality.CausalModel;
import util.ModelProvider;

public class ModifiedCheckerTest {

	CausalModel billySuzyCausalModel;
	ModifiedChecker checker;

    @Before
    public void setUp() throws Exception {
        billySuzyCausalModel = ModelProvider.billySuzyCausalModelRealNegation();
         checker = new ModifiedChecker(billySuzyCausalModel);
         Map<String, Boolean> actualValues = new HashMap<String, Boolean>() {
 			{
 				put("ST_exo", true);
 				put("BT_exo", true);
 				put("BT", true);
 				put("ST", true);
 				put("SH", true);
 				put("BS", true);
 				put("BH", false);
 			}
 		};

 		checker.setvalues( actualValues);
         
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
//	@Ignore
//	@Test
//	public void testCheckConditionThree() {
//		fail("Not yet implemented");
//	}

}
