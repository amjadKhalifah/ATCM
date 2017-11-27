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
import java.util.stream.Collectors;

public class Metrics {
    private int nodes;
    private int edges;
    private int leafs;

    public Metrics(int nodes, int edges, int leafs) {
        this.nodes = nodes;
        this.edges = edges;
        this.leafs = leafs;
    }

    public Metrics(ADTNode node) {
        this.nodes = this.getNumberOfNodes(node);
        this.edges = this.nodes - 1;
        this.leafs = this.getNumberOfLeafs(node);
    }

    public Metrics (CausalModel causalModel) {
        this.nodes = causalModel.getVariables().size();
        this.edges = getNumberOfEdges(causalModel.getVariables());
        this.leafs = causalModel.getVariables().stream().filter(v -> v instanceof ExogenousVariable)
                .collect(Collectors.toSet()).size();
    }

    private int getNumberOfNodes(ADTNode node) {
        List<ADTNode> children = node.getChildren();
        if (children == null)
            // if node does not have any children return 1, i.e. count the node
            return 1;
        else
            // else, sum up the number of nodes of all children
            return 1 + children.stream().mapToInt(this::getNumberOfNodes).sum();
    }

    private int getNumberOfLeafs(ADTNode node) {
        if (node.getChildren() == null || node.getChildren().size() == 0) {
            return 1;
        } else {
            return node.getChildren().stream().mapToInt(this::getNumberOfLeafs).sum();
        }
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
        if (edges != metrics.edges) return false;
        return leafs == metrics.leafs;
    }

    @Override
    public int hashCode() {
        int result = nodes;
        result = 31 * result + edges;
        result = 31 * result + leafs;
        return result;
    }

    public int getNumberOfNodes() {
        return nodes;
    }

    public int getEdges() {
        return edges;
    }
}
