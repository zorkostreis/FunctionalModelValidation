import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;

public class Schema {
    private List<Node> nodes;
    private List<Link> links;

    private HashMap<String, String> errors;


    public boolean hasStartAndEndNode() {
        return !(getStartNode() == null || getEndNode() == null);
    }
    public Node getStartNode() {
        for (Node node : nodes) {
            if (node.isStart()) {
                return node;
            }
        }
        errors.put("start_node", null);
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

    public boolean linkedFromStartToEnd() {
        ListIterator<Node> iterator = nodes.listIterator();
        Node current = iterator.next();

        while (iterator.hasNext()) {
            Node next = iterator.next();
            for (Link link : links) {
                if (link.existsBetweenNodes(current, next)) { break; }
                if (link == links.get(links.size() - 1)) { return false; }
            }
            current = next;
        }

        return true;
    }

    public boolean allPortsLinked() {
        ArrayList<Integer> portsIdsFromLinks = new ArrayList<>();
        ArrayList<Integer> portsIds = new ArrayList<>();

        for (Link link : links) { portsIdsFromLinks.addAll(link.getPortsIds()); }
        for (Node node : nodes) { portsIds.addAll(node.getPortsIds()); }

        List<Integer> uniqueSortedPortsIdsFromLinks = portsIdsFromLinks.stream().distinct().sorted().toList();
        List<Integer> uniqueSortedPortsIds = portsIds.stream().distinct().sorted().toList();

        return uniqueSortedPortsIdsFromLinks == uniqueSortedPortsIds;
    }

    private Node findNodeByPort(int portId) {
        for (Node node : nodes) {
            if (node.getPortsIds().contains(portId)) { return node; }
        }

        return null;
    }
}
