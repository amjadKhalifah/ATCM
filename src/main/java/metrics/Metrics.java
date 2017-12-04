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
    private int ands;
    private int ors;

    public Metrics(int nodes, int edges, int leafs, int ands, int ors) {
        this.nodes = nodes;
        this.edges = edges;
        this.leafs = leafs;
        this.ands = ands;
        this.ors = ors;
    }

    public Metrics(ADTNode node) {
        this.nodes = this.getNodes(node);
        this.edges = this.nodes - 1;
        this.leafs = this.getNumberOfLeafs(node);
        this.ands = this.getNumberOfranchType(node, ADTNode.Refinement.CONJUNCTIVE);
        this.ors = this.getNumberOfranchType(node, ADTNode.Refinement.DISJUNCTIVE);
    }

    public Metrics (CausalModel causalModel) {
        this.nodes = causalModel.getVariables().size();
        this.edges = getNumberOfEdges(causalModel.getVariables());
        this.leafs = causalModel.getVariables().stream().filter(v -> v instanceof ExogenousVariable)
                .collect(Collectors.toSet()).size();
        this.ands = causalModel.getVariables().stream()
                .mapToInt(v -> this.getNumberOfOperatorTypes(v, BasicBooleanOperator.OperatorType.and)).sum();
        this.ors = causalModel.getVariables().stream()
                .mapToInt(v -> this.getNumberOfOperatorTypes(v, BasicBooleanOperator.OperatorType.or)).sum();
    }

    @Override
    public String toString() {
        return "Metrics{" +
                "nodes=" + nodes +
                ", edges=" + edges +
                ", leafs=" + leafs +
                ", ands=" + ands +
                ", ors=" + ors +
                '}';
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

    private int getNumberOfLeafs(ADTNode node) {
        if (node.getChildren() == null || node.getChildren().size() == 0) {
            return 1;
        } else {
            return node.getChildren().stream().mapToInt(this::getNumberOfLeafs).sum();
        }
    }

    private int getNumberOfranchType(ADTNode node, ADTNode.Refinement refinement) {
        if (node.getChildren() == null || node.getChildren().size() == 0) {
            return 0;
        } else {
            int numberOfAndsChildren = node.getChildren().stream()
                    .mapToInt(c -> this.getNumberOfranchType(c, refinement)).sum();
            return (node.getRefinement() == refinement ? 1 : 0) + numberOfAndsChildren;
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

    private int getNumberOfOperatorTypes(Formula formula, BasicBooleanOperator.OperatorType operatorType) {
        if (formula instanceof ExogenousVariable) {
            return 0;
        } else if (formula instanceof EndogenousVariable){
            EndogenousVariable endogenousVariable = (EndogenousVariable) formula;
            return getNumberOfOperatorTypes(endogenousVariable.getFormula(), operatorType);
        } else if (formula instanceof BasicBooleanOperator) {
            BasicBooleanOperator operator = (BasicBooleanOperator) formula;
            return (operator.getType() == operatorType ? 1 : 0) + operator.getFormulas().stream()
                    .filter(f -> f instanceof BasicBooleanOperator)
                    .mapToInt(f -> this.getNumberOfOperatorTypes(f, operatorType)).sum();
        } else {
            return 0;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Metrics metrics = (Metrics) o;

        if (nodes != metrics.nodes) return false;
        if (edges != metrics.edges) return false;
        if (leafs != metrics.leafs) return false;
        if (ands != metrics.ands) return false;
        return ors == metrics.ors;
    }

    @Override
    public int hashCode() {
        int result = nodes;
        result = 31 * result + edges;
        result = 31 * result + leafs;
        result = 31 * result + ands;
        result = 31 * result + ors;
        return result;
    }

    public int getNodes() {
        return nodes;
    }

    public int getEdges() {
        return edges;
    }

    public int getLeafs() {
        return leafs;
    }

    public int getAnds() {
        return ands;
    }

    public int getOrs() {
        return ors;
    }
}
