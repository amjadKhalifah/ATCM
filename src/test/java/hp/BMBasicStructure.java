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

public class BMBasicStructure {

	@Before
	public void setUp() throws Exception {

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void OR_testcase1() {
		CausalModel basicOrmodel = ModelProvider.basicORstructure();
		BindableModifiedChecker checker = new BindableModifiedChecker(basicOrmodel, new GuavaPowerSet());

		// in bindable version we only need to set the exogenous values
		Map<String, Boolean> actualValues = new HashMap<String, Boolean>() {
			{

				put("exo1", true);
				put("exo2", true);

			}
		};
		// in bindable version we only need to set the exogenous values
		checker.setExovalues(actualValues);

		assertTrue(checker.checkConditionTwo(basicOrmodel.getVariableByName("endo1"), true,
				basicOrmodel.getVariableByName("endo3"), true).isEmpty());
		assertTrue(checker.checkConditionTwo(basicOrmodel.getVariableByName("endo2"), true,
				basicOrmodel.getVariableByName("endo3"), true).isEmpty());

	}

	@Test
	public void OR_testcase2() {
		CausalModel basicOrmodel = ModelProvider.basicORstructure();
		BindableModifiedChecker checker = new BindableModifiedChecker(basicOrmodel, new GuavaPowerSet());

		// in bindable version we only need to set the exogenous values
		Map<String, Boolean> actualValues = new HashMap<String, Boolean>() {
			{

				put("exo1", true);
				put("exo2", false);

			}
		};
		// in bindable version we only need to set the exogenous values
		checker.setExovalues(actualValues);

		assertFalse(checker.checkConditionTwo(basicOrmodel.getVariableByName("endo1"), true,
				basicOrmodel.getVariableByName("endo3"), true).isEmpty());
		assertTrue(checker.checkConditionTwo(basicOrmodel.getVariableByName("endo2"), false,
				basicOrmodel.getVariableByName("endo3"), true).isEmpty());

	}

	@Test
	public void OR_testcase3() {
		CausalModel basicOrmodel = ModelProvider.basicORstructure();
		BindableModifiedChecker checker = new BindableModifiedChecker(basicOrmodel, new GuavaPowerSet());

		// in bindable version we only need to set the exogenous values
		Map<String, Boolean> actualValues = new HashMap<String, Boolean>() {
			{

				put("exo1", false);
				put("exo2", true);

			}
		};
		// in bindable version we only need to set the exogenous values
		checker.setExovalues(actualValues);

		assertTrue(checker.checkConditionTwo(basicOrmodel.getVariableByName("endo1"), false,
				basicOrmodel.getVariableByName("endo3"), true).isEmpty());
		assertFalse(checker.checkConditionTwo(basicOrmodel.getVariableByName("endo2"), true,
				basicOrmodel.getVariableByName("endo3"), true).isEmpty());

	}

	@Test
	public void OR_testcase4() {
		CausalModel basicOrmodel = ModelProvider.basicORstructure();
		BindableModifiedChecker checker = new BindableModifiedChecker(basicOrmodel, new GuavaPowerSet());

		// in bindable version we only need to set the exogenous values
		Map<String, Boolean> actualValues = new HashMap<String, Boolean>() {
			{

				put("exo1", false);
				put("exo2", false);

			}
		};
		// in bindable version we only need to set the exogenous values
		checker.setExovalues(actualValues);

		assertFalse(checker.checkConditionTwo(basicOrmodel.getVariableByName("endo1"), false,
				basicOrmodel.getVariableByName("endo3"), false).isEmpty());
		assertFalse(checker.checkConditionTwo(basicOrmodel.getVariableByName("endo2"), false,
				basicOrmodel.getVariableByName("endo3"), false).isEmpty());

	}
	
	
	
	@Test
	public void AND_testcase1() {
		CausalModel basicAndModel = ModelProvider.basicANDstructure();
		BindableModifiedChecker checker = new BindableModifiedChecker(basicAndModel, new GuavaPowerSet());

		// in bindable version we only need to set the exogenous values
		Map<String, Boolean> actualValues = new HashMap<String, Boolean>() {
			{

				put("exo1", true);
				put("exo2", true);

			}
		};
		// in bindable version we only need to set the exogenous values
		checker.setExovalues(actualValues);

		assertFalse(checker.checkConditionTwo(basicAndModel.getVariableByName("endo1"), true,
				basicAndModel.getVariableByName("endo3"), true).isEmpty());
		assertFalse(checker.checkConditionTwo(basicAndModel.getVariableByName("endo2"), true,
				basicAndModel.getVariableByName("endo3"), true).isEmpty());

	}

	@Test
	public void AND_testcase2() {
		CausalModel basicAndModel = ModelProvider.basicANDstructure();
		BindableModifiedChecker checker = new BindableModifiedChecker(basicAndModel, new GuavaPowerSet());

		// in bindable version we only need to set the exogenous values
		Map<String, Boolean> actualValues = new HashMap<String, Boolean>() {
			{

				put("exo1", true);
				put("exo2", false);

			}
		};
		// in bindable version we only need to set the exogenous values
		checker.setExovalues(actualValues);

		assertTrue(checker.checkConditionTwo(basicAndModel.getVariableByName("endo1"), true,
				basicAndModel.getVariableByName("endo3"), true).isEmpty());
		assertFalse(checker.checkConditionTwo(basicAndModel.getVariableByName("endo2"), false,
				basicAndModel.getVariableByName("endo3"), true).isEmpty());

	}

	@Test
	public void AND_testcase3() {
		CausalModel basicAndModel = ModelProvider.basicANDstructure();
		BindableModifiedChecker checker = new BindableModifiedChecker(basicAndModel, new GuavaPowerSet());

		// in bindable version we only need to set the exogenous values
		Map<String, Boolean> actualValues = new HashMap<String, Boolean>() {
			{

				put("exo1", false);
				put("exo2", true);

			}
		};
		// in bindable version we only need to set the exogenous values
		checker.setExovalues(actualValues);

		assertFalse(checker.checkConditionTwo(basicAndModel.getVariableByName("endo1"), false,
				basicAndModel.getVariableByName("endo3"), true).isEmpty());
		assertTrue(checker.checkConditionTwo(basicAndModel.getVariableByName("endo2"), true,
				basicAndModel.getVariableByName("endo3"), true).isEmpty());

	}

	@Test
	public void AND_testcase4() {
		CausalModel basicAndModel = ModelProvider.basicANDstructure();
		BindableModifiedChecker checker = new BindableModifiedChecker(basicAndModel, new GuavaPowerSet());

		// in bindable version we only need to set the exogenous values
		Map<String, Boolean> actualValues = new HashMap<String, Boolean>() {
			{

				put("exo1", false);
				put("exo2", false);

			}
		};
		// in bindable version we only need to set the exogenous values
		checker.setExovalues(actualValues);

		assertTrue(checker.checkConditionTwo(basicAndModel.getVariableByName("endo1"), false,
				basicAndModel.getVariableByName("endo3"), false).isEmpty());
		assertTrue(checker.checkConditionTwo(basicAndModel.getVariableByName("endo2"), false,
				basicAndModel.getVariableByName("endo3"), false).isEmpty());

	}
	
	
}
