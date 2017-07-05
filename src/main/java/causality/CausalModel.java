package causality;

import mef.faulttree.*;
import mef.formula.*;
import mef.general.Constant;
import mef.general.FloatConstant;

import java.util.*;
import java.util.stream.Collectors;

public class CausalModel {
    private String name;
    private Set<Variable> variables;

    public CausalModel(String name, Set<Variable> variables) {
        this.name = name;
        this.variables = variables;
    }

    /**
     * Create causal model from MEF/Open-PSA fault tree
     *
     * @param faultTreeDefinition
     * @return
     */
    public static CausalModel fromMEF(FaultTreeDefinition faultTreeDefinition) {
        String name = faultTreeDefinition.getName();
        List<ElementDefinition> elementDefinitions = faultTreeDefinition.getElementDefinitions();

        // get all variables of the model
        HashMap<String, Variable> variables = getVariables(faultTreeDefinition);

        // set variables
        for (ElementDefinition elementDefinition : elementDefinitions) {
            if (elementDefinition instanceof GateDefinition) {
                GateDefinition gateDefinition = (GateDefinition) elementDefinition;
                Formula formula = gateDefinition.getFormula();
                Formula formulaConverted = convertFormula(formula, variables);

                EndogenousVariable correspondingVariable = (EndogenousVariable) variables.get(gateDefinition.getName());
                correspondingVariable.setFormula(formulaConverted);
            }
        }

        CausalModel causalModel = new CausalModel(name, new HashSet<>(variables.values()));
        return causalModel;
    }

    // convert formula from MEF/Open-PSA to causal model formula
    private static Formula convertFormula(Formula formula, HashMap<String, Variable> variables) {
        /*
        IMPORTANT:
        When converting the formulas, the following problem occurs: In MEF, the different event and gate objects do
        not have a connection even if they describe the same element. For instance, there may exist a gate-definition
         and a gate instance, but both are represented by independent objects. However, when it comes to causal
         models, it is desirable, that the variables used in the formulas are defined exactly one and if a variable
         occurs multiple times, it is always the same object. We overcome this problem, be creating ALL the variables
          that exist, beforehand and pass them as hashmap. Within this function, the variables are connected, i.e.
          the actual formulas are created.
         */

        if (formula instanceof BasicEvent) {
            return variables.get(((BasicEvent) formula).getName());
        } else if (formula instanceof Gate) {
            return variables.get(((Gate) formula).getName());
        } else if (formula instanceof BasicBooleanOperator) {
            List<Formula> formulas = ((BasicBooleanOperator) formula).getFormulas().stream().map
                    (f -> convertFormula(f, variables)).collect(Collectors.toList());
            BasicBooleanOperator op = new BasicBooleanOperator(((BasicBooleanOperator) formula).getType(), formulas);
            return op;
        } else if (formula instanceof ImplyOperator) {
            ImplyOperator op = (ImplyOperator) formula;
            ImplyOperator implyOperator = new ImplyOperator(convertFormula(op.getLeftFormula(), variables),
                    convertFormula(op.getRightFormula(), variables));
            return implyOperator;
        }

        return null;
    }

    // return all the variables within a FaultTreeDefinition
    private static HashMap<String, Variable> getVariables(FaultTreeDefinition faultTreeDefinition) {
        /* IMPORTANT
        assumes that fault tree def contains only gate defs, basic event defs and house event defs
        Each basic event must have a definition (can be empty)
         */

        HashMap<String, Variable> variables = new HashMap<>();
        List<ElementDefinition> elementDefinitions = faultTreeDefinition.getElementDefinitions();

        for (ElementDefinition elementDefinition : elementDefinitions) {
            if (elementDefinition instanceof GateDefinition) {
                GateDefinition g = (GateDefinition) elementDefinition;
                EndogenousVariable endo = new EndogenousVariable(g.getName(), null);
                variables.put(endo.getName(), endo);
            } else if (elementDefinition instanceof BasicEventDefinition) {
                BasicEventDefinition b = (BasicEventDefinition) elementDefinition;
                Constant expression = b.getExpression();
                ExogenousVariable exo = new ExogenousVariable(b.getName() + "_exo");
                EndogenousVariable endo = new EndogenousVariable(b.getName(), exo);
                // set probability
                if (expression instanceof FloatConstant) {
                    FloatConstant probability = (FloatConstant) expression;
                    if (probability.getValue() != 0D)
                        endo.setProbability(probability.getValue());
                }

                variables.put(endo.getName(), endo);
                variables.put(exo.getName(), exo);
            } else if (elementDefinition instanceof HouseEventDefinition) {
                HouseEventDefinition h = (HouseEventDefinition) elementDefinition;
                ExogenousVariable exo = new ExogenousVariable(h.getName());
                variables.put(exo.getName(), exo);
            }
        }

        return variables;
    }

    /**
     * Turns causal model into a report
     * @return
     */
    public String toReport() {
        Set<Variable> exogenousVariables = this.getVariables().stream()
                .filter(v -> v instanceof ExogenousVariable).collect(Collectors.toSet());
        Set<Variable> endogenousVariables = this.getVariables().stream()
                .filter(v -> v instanceof EndogenousVariable).collect(Collectors.toSet());
        Set<Variable> leafVariables = endogenousVariables.stream()
                .filter(e -> ((EndogenousVariable) e).getFormula() instanceof ExogenousVariable)
                .collect(Collectors.toSet());
        Set<Variable> nonLeafVariables = endogenousVariables.stream()
                .filter(e -> !(((EndogenousVariable) e).getFormula() instanceof ExogenousVariable))
                .collect(Collectors.toSet());
        Set<EndogenousVariable> variablesWithNonZeroProbabilities = endogenousVariables.stream()
                .filter(e -> ((EndogenousVariable) e).getProbability() != 0D).map(v -> (EndogenousVariable) v)
                .collect(Collectors.toSet());

        // construct report
        String str = "Causal Model for '" + this.getName() + "'\n\n";
        str += "EXOGENOUS VARIABLES:\n";
        str += printVariables(exogenousVariables) + "\n";
        str += "ENDOGENOUS VARIABLES:\n";
        str += printVariables(leafVariables);
        str += printVariables(nonLeafVariables) + "\n";
        str += "PROBABILITIES:\n";
        str += printProbabilities(variablesWithNonZeroProbabilities);
        return str;
    }

    private String printVariables(Set<Variable> variables) {
        String str = "";
        for (Variable variable : variables) {
            str += variable.print() + "\n";
        }
        return str;
    }

    private String printProbabilities(Set<EndogenousVariable> variables) {
        String str = "";
        for (EndogenousVariable variable : variables) {
            str += variable.getName() + ": " + variable.getProbability() + "\n";
        }

        return str;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CausalModel that = (CausalModel) o;

        if (name != null ? !name.equals(that.name) : that.name != null) return false;
        return variables != null ? variables.equals(that.variables) : that.variables == null;
    }

    @Override
    public int hashCode() {
        int result = name != null ? name.hashCode() : 0;
        result = 31 * result + (variables != null ? variables.hashCode() : 0);
        return result;
    }

    public String getName() {
        return name;
    }


    public Set<Variable> getVariables() {
        return variables;
    }
}