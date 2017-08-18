package mef.formula;

import causality.EndogenousVariable;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

public class FormulaTest {
    Formula f1;
    Formula f2;
    Formula f3;
    Formula f4;

    @Before
    public void setUp() throws Exception {
        EndogenousVariable v1 = new EndogenousVariable("V1", null);
        EndogenousVariable v2 = new EndogenousVariable("V2", null);
        EndogenousVariable v3 = new EndogenousVariable("V3", null);

        // (V1 and V2)
        f1 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.and, Arrays.asList(v1, v2));

        // ((V1 and V2) or V3)
        f2 = new BasicBooleanOperator(BasicBooleanOperator.OperatorType.or, Arrays.asList(f1, v3));

        // (V1 -> V2)
        f3 = new ImplyOperator(v1, v2);

        // (((V1 and V2) or V3) -> V1)
        f4 = new ImplyOperator(f2, v1);
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void Should_PrintFormulaWithBasicBooleanOperator() throws Exception {
        String f1Expected = "(V1 and V2)";
        assertEquals(f1Expected, f1.print(new HashSet<>()));

        String f2Expected = "((V1 and V2) or V3)";
        assertEquals(f2Expected, f2.print(new HashSet<>()));
    }

    @Test
    public void Should_PrintFormulaWithMoreComplexOperators() throws Exception {
        String f3Expected = "(V1 -> V2)";
        assertEquals(f3Expected, f3.print(new HashSet<>()));

        String f4Expected = "(((V1 and V2) or V3) -> V1)";
        assertEquals(f4Expected, f4.print(new HashSet<>()));
    }

}