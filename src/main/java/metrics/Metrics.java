package metrics;

import parser.adtool.ADTNode;

import java.util.List;

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

    private int getNodes(ADTNode node) {
        List<ADTNode> children = node.getChildren();
        if (children == null)
            // if node does not have any children return 1, i.e. count the node
            return 1;
        else
            // else, sum up the number of nodes of all children
            return 1 + children.stream().mapToInt(this::getNodes).sum();
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
}
