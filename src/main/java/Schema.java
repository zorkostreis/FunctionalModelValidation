import java.util.List;

public class Schema {
    private List<Node> nodes;
    private List<Link> links;

    public boolean hasStartAndEndNode() {
        return !(getStartNode() == null || getEndNode() == null);
    }
    public Node getStartNode() {
        for (Node node : nodes) {
            if (node.isStart()) {
                return node;
            }
        }
        return null;
    }

    public Node getEndNode() {
        for (Node node : nodes) {
            if (node.isEnd()) {
                return node;
            }
        }
        return null;
    }

    public List<Node> getNodes() {
        return nodes;
    }
    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }
}
