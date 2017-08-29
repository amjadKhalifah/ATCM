package hp;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import causality.CausalModel;
import util.ModelProvider;

public class CombinedModifiedCheckerTest {

	CausalModel billySuzyCausalModel, billySuzyCausalModel2;
	BindableModifiedChecker bindableChecker;
	TopDownChecker checker;
    @Before
    public void setUp() throws Exception {
        billySuzyCausalModel = ModelProvider.billySuzyCausalModelNoNegation();
        billySuzyCausalModel2 = ModelProvider.billySuzyCausalModelNoNegation();
        
        bindableChecker = new BindableModifiedChecker(billySuzyCausalModel);
        
        
     // in bindable version we only need to set the exogenous values
     		Map<String, Boolean> actualValues = new HashMap<String, Boolean>() {
     			{
     				put("ST_exo", true);
     				put("BT_exo", true);
     				
//     				put("exo1", true);
//     				put("exo2", true);
//     				put("exo3", true);
//     				put("exo2", true);
     			}
     		};
     		// in bindable version we only need to set the exogenous values
     		bindableChecker.setExovalues( actualValues);
        
        checker = new TopDownChecker(billySuzyCausalModel2);
        
        Map<String, Boolean> values = new HashMap<String, Boolean>() {
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

 		checker.setvalues( values);
    }


	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testFindCause() {
	
		assertTrue("find cause different between impls",new HashSet<>(checker.findCause(billySuzyCausalModel2.getVariableByName("BS"))).equals(new HashSet<>(bindableChecker.findCause(billySuzyCausalModel.getVariableByName("BS")))));
	}
	
	@Test
	public void testTwoImplsConiditonOne() {
	
		assertTrue("condition one is not equal in the two impls", checker.checkConditionOne(billySuzyCausalModel2.getVariableByName("ST"),true,billySuzyCausalModel2.getVariableByName("BS"), true) == bindableChecker.checkConditionOne(billySuzyCausalModel.getVariableByName("ST"),true,billySuzyCausalModel.getVariableByName("BS"), true));
		assertTrue("condition one is not equal in the two impls", checker.checkConditionOne(billySuzyCausalModel2.getVariableByName("BT"),true,billySuzyCausalModel2.getVariableByName("BH"), false) == bindableChecker.checkConditionOne(billySuzyCausalModel.getVariableByName("BT"),true,billySuzyCausalModel.getVariableByName("BH"), false));
	}
	@Test
	public void testTwoImplsConiditonTwo() {
		
		assertTrue("condition one failed",new HashSet<>( checker.checkConditionTwo(billySuzyCausalModel2.getVariableByName("BT"),true,billySuzyCausalModel2.getVariableByName("BS"), true)).equals(new HashSet<>(bindableChecker.checkConditionTwo(billySuzyCausalModel.getVariableByName("BT"),true,billySuzyCausalModel.getVariableByName("BS"), true))));
		
		assertTrue("condition one failed",new HashSet<>(checker.checkConditionTwo(billySuzyCausalModel2.getVariableByName("ST"),true,billySuzyCausalModel2.getVariableByName("BS"),false)).equals(new HashSet<>(bindableChecker.checkConditionTwo(billySuzyCausalModel.getVariableByName("ST"),true,billySuzyCausalModel.getVariableByName("BS"),false))));
		
		
	}
//	@Ignore
//	@Test
//	public void testCheckConditionThree() {
//		fail("Not yet implemented");
//	}

}
