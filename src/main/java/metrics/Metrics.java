package metrics;

import causality.CausalModel;
import causality.EndogenousVariable;
import causality.ExogenousVariable;
import causality.Variable;
import mef.formula.BasicBooleanOperator;
import mef.formula.Formula;
import parser.adtool.ADTNode;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Metrics {
    private int nodes;
    private int edges;

    public Metrics(int nodes, int edges) {
        this.nodes = nodes;
        this.edges = edges;
    }

    public Metrics(ADTNode node) {
        this.nodes = this.getNodes(node);
        this.edges = this.nodes - 1;
    }

    public Metrics (CausalModel causalModel) {
        // TODO
        this.nodes = causalModel.getVariables().size();
        this.edges = getNumberOfEdges(causalModel.getVariables());
    }

    private int getNodes(ADTNode node) {
        List<ADTNode> children = node.getChildren();
        if (children == null)
            // if node does not have any children return 1, i.e. count the node
            return 1;
        else
            // else, sum up the number of nodes of all children
            return 1 + children.stream().mapToInt(this::getNodes).sum();
    }

    private int getNumberOfEdges(Set<Variable> variables) {
        int edges = 0;
        for (Variable variable : variables) {
            if (variable instanceof EndogenousVariable) {
                edges += getInvolvedVariables(((EndogenousVariable) variable).getFormula()).size();
            }
        }
        return edges;
    }

    /**
     * This is a helper method that returns the number of variables in a formula that defines a variable. So, the
     * passed formula parameter is assumed to define a variable. It returns a set such that variables used multiple
     * times in a formula are returned only once
     * @param formula
     * @return
     */
    private Set<Variable> getInvolvedVariables(Formula formula) {
        Set<Variable> variables = new HashSet<>();
        if (formula instanceof Variable) {
            variables.add((Variable) formula);
        } else if (formula instanceof BasicBooleanOperator) {
            BasicBooleanOperator operator = (BasicBooleanOperator) formula;
            operator.getFormulas().forEach(f -> variables.addAll(getInvolvedVariables(f)));
        }
        return  variables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Metrics metrics = (Metrics) o;

        if (nodes != metrics.nodes) return false;
        return edges == metrics.edges;
    }

    @Override
    public int hashCode() {
        int result = nodes;
        result = 31 * result + edges;
        return result;
    }

    public int getNodes() {
        return nodes;
    }

    public int getEdges() {
        return edges;
    }
}
