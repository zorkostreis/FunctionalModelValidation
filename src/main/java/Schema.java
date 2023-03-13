import java.util.*;

public class Schema {
    private List<Node> nodes;
    private List<Link> links;

    private HashMap<String, HashMap<String, ArrayList<Object>>> errors;

    public Schema() {
        this.errors = new HashMap<>();
        this.errors.put("nodes", new HashMap<>());
        this.errors.put("links", new HashMap<>());
    }

    public HashMap<String, HashMap<String, ArrayList<Object>>> getErrors() { return errors; };

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
        for (Node node : nodes)
            if (!node.hasValidPortTypes())
                updateErrors("nodes", node.getId(), "invalid port types");

        return true;
    }

    private void updateErrors(String key, Integer objectId, Object errorMessage) {
        HashMap <String, ArrayList<Object>> currentCollection = this.errors.get(key);
        String id = String.valueOf(objectId);

        if (!currentCollection.containsKey(id))
            currentCollection.put(id, new ArrayList<>());

        ArrayList<Object> objectErrors = currentCollection.get(id);
        objectErrors.add(errorMessage);
    }

    public boolean nodesHavePortsOfEachType() {
        for (Node node: nodes) {
            if (!node.missingPortTypes().isEmpty()) {
                HashMap<String, List<PortTypes>> errorMap = new HashMap<>();
                errorMap.put("missingPortTypes", node.missingPortTypes());
                updateErrors("nodes", node.getId(), errorMap);
            }
        };

        return true;
    }

    public boolean allPortsLinked() {
        List<Integer> portIdsFromLinks = getPortIdsFromLinks();
        for (Node node : nodes) {
            List<Integer> nodePortIds = node.getPortIds();
            nodePortIds.removeAll(portIdsFromLinks);
            if (!nodePortIds.isEmpty()) {
                HashMap<String, List<Integer>> errorMap = new HashMap<>();
                errorMap.put("notConnectedPorts", nodePortIds);
                updateErrors("nodes", node.getId(), errorMap);
            }
        }

        return true;
    }

    public boolean allPortIdsFromLinksExist() {
        List<Integer> portIdsFromNodes = getPortIdsFromNodes();

        for (Link link : links) {
            List<Integer> linkPortIds = link.getPortIds();
            linkPortIds.removeAll(portIdsFromNodes);
            if (!linkPortIds.isEmpty()) {
                HashMap<String, List<Integer>> errorMap = new HashMap<>();
                errorMap.put("missingPorts", linkPortIds);
                updateErrors("links", link.getId(), errorMap);
            }
        }

        return true;
    }

    public boolean validateLinks() {
        for (Link link: links) {
            if (link.lacksPortIds()) {
                updateErrors("links", link.getId(), "lacks port ids");
                continue;
            }

            Map<String, Port> linkPorts = getPortsFromLink(link);
            Port fromPort = linkPorts.get("from");
            Port toPort = linkPorts.get("to");

            switch (link.getType()) {
                case external -> {
                    if (link.hasFrom() && link.hasTo()) {
                        updateErrors("links", link.getId(), "external link has FROM and TO ports");
                    }
                }
                case internal -> {
                    if (!link.hasFrom()) { updateErrors("links", link.getId(), "internal link lacks FROM port"); }
                    if (!link.hasTo()) { updateErrors("links", link.getId(), "internal link lacks TO port"); }
                }
            };
            if (fromPort != null && !fromPort.isOutput()) { updateErrors("links", link.getId(), "FROM port is not output"); }
            if (toPort != null && toPort.isOutput()) { updateErrors("links", link.getId(), "TO port is output"); }
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
            if (link.getFrom() == port.getId() && port.getType() != null)
                result.put("from", port);
            if (link.getTo() == port.getId() && port.getType() != null)
                result.put("to", port);
            if (result.size() == 2) break;
        }
        return result;
    }

    private List<Port> getPortsFromNodes() {
        return nodes.stream().map(Node::getPorts).flatMap(List::stream).toList();
    }
}
