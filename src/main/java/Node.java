import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Node {
    private int id;
    private String name;
    private List<Port> ports;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    public List<Integer> getPortIds() {
        List<Integer> portIds = new ArrayList<>();
        for (Port port : ports) {
            if (port.getType() != PortTypes.mechanism) {
                portIds.add(port.getId());
            }
        }

        return portIds;
    }

    public boolean hasValidPortTypes() {
        List<PortTypes> portTypes = new ArrayList<>(Arrays.stream(PortTypes.values()).toList());
        List<PortTypes> presentPortTypes = new ArrayList<>(this.ports.stream().map(Port::getType).distinct().toList());
        presentPortTypes.removeAll(portTypes);

        return presentPortTypes.isEmpty();
    }

    public List<PortTypes> missingPortTypes() {
        List<PortTypes> portTypes = new ArrayList<>(Arrays.stream(PortTypes.values()).toList());
        List<PortTypes> presentPortTypes = new ArrayList<>(this.ports.stream().map(Port::getType).distinct().toList());
        portTypes.removeAll(presentPortTypes);

        return portTypes;
    }
}
