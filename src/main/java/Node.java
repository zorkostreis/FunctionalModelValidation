import java.util.List;
import java.util.Objects;

public class Node {
    private int id;
    private String name;
    private int level;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<Port> getPorts() {
        return ports;
    }

    public void setPorts(List<Port> ports) {
        this.ports = ports;
    }

    public boolean isStart() {
        return Objects.equals(name, "Start") && id == 0;
    }

    public boolean isEnd() {
        return id == -1 && Objects.equals(name, "End");
    }


}