package hp;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import causality.CausalModel;
import util.ModelProvider;

public class BindableCheckManualModelTest {

	CausalModel manualModel;
	BindableModifiedChecker checker;
	private static final Logger logger = LogManager.getLogger(BindableCheckManualModelTest.class);
    @Before
    public void setUp() throws Exception {
        manualModel = ModelProvider.testModel();
         checker = new BindableModifiedChecker(manualModel);
         Map<String, Boolean> actualValues = new HashMap<String, Boolean>() {
 			{
 				put("exo1", true);
 				put("exo3", true);
 				put("exo4", true);
// 				put("endo1", true);
// 				put("endo3", true);
// 				put("endo4", true);
// 				
// 				
// 				put("endo2", true);
// 				put("endo5", true);
// 				put("endo6", false);
// 				put("endo7", false);
 			}
 		};

 		checker.setExovalues( actualValues);
 		logger.info(manualModel.toReport());
         
    }


	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testIsCause() {
	
		assertFalse("is cause failed",checker.isCause(manualModel.getVariableByName("endo1"),true,manualModel.getVariableByName("endo5"), true).isEmpty());
		assertTrue("is cause failed",checker.isCause(manualModel.getVariableByName("endo1"),true,manualModel.getVariableByName("endo6"), true).isEmpty());
	}
	@Test
	public void testFindCause() {
	
		assertFalse("find cause failed",checker.findCause(manualModel.getVariableByName("endo5")).isEmpty());
	}
	
	@Test
	public void testCheckConditionOne() {
	
		assertTrue("condition one failed",checker.checkConditionOne(manualModel.getVariableByName("endo1"),true,manualModel.getVariableByName("endo6"), false));
	}
	@Test
	public void testCheckConditionTwo() {
		
//		assertFalse("condition one failed",!checker.checkConditionTwo(manualModel.getVariableByName("endo1"),true,manualModel.getVariableByName("endo6"), true).isEmpty());
		
		assertTrue("condition one failed",!checker.checkConditionTwo(manualModel.getVariableByName("endo1"),true,manualModel.getVariableByName("endo6"), true).isEmpty());
		

		
	}
//	@Ignore
//	@Test
//	public void testCheckConditionThree() {
//		fail("Not yet implemented");
//	}

}
