package hp;

import static org.junit.Assert.*;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import causality.CausalModel;
import mef.faulttree.FaultTreeDefinition;
import parser.adtool.ADTParser;
import util.GuavaPowerSet;

public class BindableKeysTest {

	File stealMasterKeyUnfoldedXML;

	ADTParser ADTParser;

	CausalModel causalModel;
	BindableModifiedChecker checker;
	private static final Logger logger = LogManager.getLogger(BindableKeysTest.class);

	@Before
	public void setUp() throws Exception {

		stealMasterKeyUnfoldedXML = new File(
				this.getClass().getClassLoader().getResource("user_attribution/Steal_Master_Key_unfolded_trimmed.adt")
						.getPath().replaceAll("%20", " "));
		ADTParser = new ADTParser();
		FaultTreeDefinition keysMEF = ADTParser.toMEF(stealMasterKeyUnfoldedXML, null);
		causalModel = CausalModel.fromMEF(keysMEF);
		checker = new BindableModifiedChecker(causalModel, new GuavaPowerSet());
		logger.info(causalModel.getEndogenousVars());
		Map<String, Boolean> actualValues = new HashMap<String, Boolean>() {
			{
				put("U1_Access_exo", true);
				put("U1_Attach_Debugger_exo", true);

				put("U2_Access_exo", false);
				put("U2_Attach_Debugger_exo", false);

				put("U1_From_Script_exo", false);
				put("U1_From_Network_exo", false);

				put("U1_From_DB_exo", false);
				put("U1_From_File_exo", false);

				put("U2_From_DB_exo", false);
				put("U2_From_File_exo", false);

				put("U2_From_Script_exo", false);
				put("U2_From_Network_exo", true);

			}
		};

		checker.setExovalues(actualValues);
		logger.info(causalModel.getEndogenousVars());
//		logger.info(causalModel.toReport());

	}

	@After
	public void tearDown() throws Exception {
	}

	 @Test
	public void testIsCause() {

		assertFalse("is cause failed", checker.isCause(causalModel.getVariableByName("U1_Access"), true,
				causalModel.getVariableByName("Steal_Master_Key"), true).isEmpty());
		 assertTrue("is cause failed", checker
		 .isCause(causalModel.getVariableByName("U2_Access"), false,
		 causalModel.getVariableByName("Steal_Master_Key"), true)
		 .isEmpty());
	}

	@Test
	public void testFindCause() {

		assertFalse("find cause failed",
				checker.findCause(causalModel.getVariableByName("Steal_Master_Key")).isEmpty());
	}

	@Test
	public void testCheckConditionOne() {

		assertTrue("condition one failed", checker.checkConditionOne(causalModel.getVariableByName("U1_Access"), true,
				causalModel.getVariableByName("Steal_Master_Key"), true));
	}

	 @Test
	public void testCheckConditionTwo() {
		// assertFalse("condition one
		// failed",!checker.checkConditionTwo(manualModel.getVariableByName("endo1"),true,manualModel.getVariableByName("endo6"),
		// true).isEmpty());

		assertFalse("condition two failed", checker.checkConditionTwo(causalModel.getVariableByName("U1_Access"), true,
				causalModel.getVariableByName("Steal_Master_Key"), true).isEmpty());

	}

}
