import java.util.*;

public class Schema {
    private List<Node> nodes;
    private List<Link> links;

    private HashMap<String, Object> errors;

    public Schema() {
        this.errors = new HashMap<>();;
    }

    public HashMap<String, Object> getErrors() { return errors; };

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

    public boolean nodesHaveValidPortTypes() {
        List<Integer> nodesWithInvalidPortTypes = new ArrayList<>();

        for(Node node: nodes) {
            if (!node.hasValidPortTypes()) {
                nodesWithInvalidPortTypes.add(node.getId());
            }
        }

        if (!nodesWithInvalidPortTypes.isEmpty()) {
            errors.put("Nodes with invalid port types", nodesWithInvalidPortTypes);
            return false;
        }

        return true;
    }

    public boolean nodesHavePortsOfEachType() {
        List<HashMap> nodesWithoutEachPort = new ArrayList<>();

        for(Node node: nodes) {
            if (!node.missingPortTypes().isEmpty()) {
                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("id", node.getId());
                map.put("missingPortTypes", node.missingPortTypes());
                nodesWithoutEachPort.add(map);
            }
        };

        if (!nodesWithoutEachPort.isEmpty()) {
            errors.put("Nodes without ports of each type", nodesWithoutEachPort);
            return false;
        }

        return true;
    }

    public boolean allPortsLinked() {
        HashSet<Integer> portIdsFromNodes = new HashSet<>(getPortIdsFromNodes());
        getPortIdsFromLinks().forEach(portIdsFromNodes::remove);

        if (!portIdsFromNodes.isEmpty()) {
            errors.put("Ports not connected by links", portIdsFromNodes);
            return false;
        }

        return true;
    }

    public boolean allPortIdsFromLinksExist() {
        HashSet<Integer> portIdsFromLinks = new HashSet<>(getPortIdsFromLinks());
        getPortIdsFromNodes().forEach(portIdsFromLinks::remove);

        if(!portIdsFromLinks.isEmpty()) {
            errors.put("Ports with these ids do not exist: ", portIdsFromLinks);
            return false;
        }

        return true;
    }

    public boolean validateLinks() {
        for (Link link: links) {
            Map<String, Port> linkPorts = getPortsFromLink(link);
            Port fromPort = linkPorts.get("from");
            Port toPort = linkPorts.get("to");

            link.hasPortIds();
            switch (link.getType()) {
                case external -> { link.hasOnlyOnePortId(); }
                case internal -> { link.hasFromAndToPortIds(); }
            };
            if (fromPort != null) { fromPortIsOutput(link, fromPort); }
            if (toPort != null) { toPortIsNotOutput(link, toPort); }
        }

        return true;
    }

    private List<Integer> getPortIdsFromNodes() {
        return nodes.stream().map(Node::getPortIds).flatMap(List::stream).toList();
    }

    private List<Integer> getPortIdsFromLinks() {
        return links.stream().map(Link::getPortIds).flatMap(List::stream).toList();
    }

    private Map<String, Port> getPortsFromLink(Link link) {
        HashMap<String, Port> result = new HashMap<>();
        for (Port port : getPortsFromNodes()) {
            if (link.getFrom() == port.getId())
                result.put("from", port);
            if (link.getTo() == port.getId())
                result.put("to", port);
            if (result.size() == 2) break;
        }
        return result;
    }

    private List<Port> getPortsFromNodes() {
        return nodes.stream().map(Node::getPorts).flatMap(List::stream).toList();
    }

    private boolean fromPortIsOutput(Link link, Port fromPort) {
        if (!fromPort.isOutput()) {
            System.out.println("From port is not output" + link.getId());
            return false;
        }

        return true;
    }

    private boolean toPortIsNotOutput(Link link, Port toPort) {
        if (toPort.isOutput()) {
            System.out.println("To port is output" + link.getId());
            return false;
        }

        return true;
    }

    /*    public boolean linkedFromStartToEnd() {
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
    }*/

    //    private HashSet<Integer> calculateDifference(HashSet<Integer> lha, HashSet<Integer> rha) {
//        HashSet<Integer> result;
//
//        if (lha.size() >= rha.size()) {
//            lha.removeAll(rha);
//            result = lha;
//        }
//        else {
//            rha.removeAll(lha);
//            result = rha;
//        }
//
//        return result;
//    }

//    private Node findNodeByPort(int portId) {
//        for (Node node : nodes) {
//            if (node.getPortIds().contains(portId)) {
//                return node;
//            }
//        }
//
//        return null;
//    }
}
