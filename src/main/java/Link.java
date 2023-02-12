import java.util.Arrays;
import java.util.List;

public class Link {
    private int from;
    private int to;
    private String label;

    public List<Integer> getPortsIds() {
        return Arrays.asList(from, to);
    }
    public int getFrom() {
        return from;
    }

    public void setFrom(int from) {
        this.from = from;
    }

    public int getTo() {
        return to;
    }

    public void setTo(int to) {
        this.to = to;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public boolean existsBetweenNodes(Node from, Node to) {
        if (from == null || to == null) { return false; }

        for (Port fromPort : from.getPorts()) {
            for (Port toPort : to.getPorts()) {
                Link result = findByPorts(fromPort, toPort);

                if (result != null) { return true; }
            }
        }

        return false;
    }

    private Link findByPorts(Port from, Port to) {
        return from.getId() == this.from && to.getId() == this.to ? this : null;
    }
}
