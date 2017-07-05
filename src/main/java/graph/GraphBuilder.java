package graph;

import causality.CausalModel;
import causality.EndogenousVariable;
import causality.Variable;
import mef.formula.BasicBooleanOperator;
import mef.formula.Formula;
import mef.formula.ImplyOperator;
import org.jgrapht.DirectedGraph;
import org.jgrapht.ext.DOTExporter;
import org.jgrapht.ext.StringComponentNameProvider;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultEdge;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class GraphBuilder {

    public static void export(CausalModel causalModel, String filePath) throws IOException {
        // create graph from causal model
        DirectedGraph graph = build(causalModel);
        // initialize dot exporter
        DOTExporter<String, DefaultEdge> exporter
                = new DOTExporter<>(new StringComponentNameProvider<>(), null,null);
        // write .dot file to specified path
        exporter.exportGraph(graph, new FileWriter(filePath));
    }

    private static DirectedGraph<String, DefaultEdge> build(CausalModel causalModel) {
        // instantiate graph
        DirectedGraph<String, DefaultEdge> directedGraph =
                new DefaultDirectedGraph(DefaultEdge.class);
        // add nodes
        causalModel.getVariables().forEach(v -> directedGraph.addVertex(v.getName().replace(" ","")));
        // add edges
        causalModel.getVariables().stream().filter(v -> v instanceof EndogenousVariable)
                .forEach(v -> addEdges(directedGraph, ((EndogenousVariable) v).getFormula(), v.getName()));

        return directedGraph;
    }

    private static void addEdges(DirectedGraph<String, DefaultEdge> graph, Formula formula, String parent) {
        // if formula is variable just add one edge
        if (formula instanceof Variable) {
            Variable variable = (Variable) formula;
            graph.addEdge(variable.getName(). replace(" ", ""), parent.replace(" ", ""));
        }
        // if formula is an operator, call addEdges recursively
        else if (formula instanceof BasicBooleanOperator) {
            BasicBooleanOperator operator = (BasicBooleanOperator) formula;
            List<Formula> formulas = operator.getFormulas();
            formulas.forEach(f -> addEdges(graph, f, parent));
        } else if (formula instanceof ImplyOperator) {
            ImplyOperator imply = (ImplyOperator) formula;
            addEdges(graph, imply.getLeftFormula(), parent);
            addEdges(graph, imply.getRightFormula(), parent);
        }
}
}
