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
                HashMap<String, Object> map = new HashMap<>();
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
        List<Integer> linksWithoutPortIds = new ArrayList<>();
        List<Integer> extLinksWithFromAndTo = new ArrayList<>();
        List<Integer> intLinksWithoutFrom = new ArrayList<>();
        List<Integer> intLinksWithoutTo = new ArrayList<>();
        List<Integer> fromPortsNotOutput = new ArrayList<>();
        List<Integer> toPortsOutput = new ArrayList<>();

        for (Link link: links) {
            if (link.lacksPortIds()) {
                linksWithoutPortIds.add(link.getId());
                continue;
            }

            Map<String, Port> linkPorts = getPortsFromLink(link);
            Port fromPort = linkPorts.get("from");
            Port toPort = linkPorts.get("to");

            switch (link.getType()) {
                case external -> {
                    if (link.hasFrom() && link.hasTo()) { extLinksWithFromAndTo.add(link.getId()); }
                }
                case internal -> {
                    if (!link.hasFrom()) { intLinksWithoutFrom.add(link.getId()); }
                    if (!link.hasTo()) { intLinksWithoutTo.add(link.getId()); }
                }
            };
            if (fromPort != null && !fromPort.isOutput()) { fromPortsNotOutput.add(link.getId()); }
            if (toPort != null && toPort.isOutput()) { toPortsOutput.add(link.getId()); }
        }

        if (!linksWithoutPortIds.isEmpty()) { errors.put("Links lack port ids", linksWithoutPortIds); }
        if (!extLinksWithFromAndTo.isEmpty()) { errors.put("External links have FROM and TO", extLinksWithFromAndTo); }
        if (!intLinksWithoutFrom.isEmpty()) { errors.put("Internal links lack FROM port", intLinksWithoutFrom); }
        if (!intLinksWithoutTo.isEmpty()) { errors.put("Internal links lack TO port", intLinksWithoutTo); }
        if (!fromPortsNotOutput.isEmpty()) { errors.put("Links have FROM ports that are not output", fromPortsNotOutput); }
        if (!toPortsOutput.isEmpty()) { errors.put("Links have TO ports that are output", toPortsOutput); }

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
