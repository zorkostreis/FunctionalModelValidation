import java.util.List;
import java.util.stream.Stream;

public class Link {
    public enum LinkTypes {
        external,
        internal
    }

    private int id;
    private int from;
    private int to;
    private String label;
    private LinkTypes type;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LinkTypes getType() {
        return type;
    }

    public void setType(LinkTypes type) {
        this.type = type;
    }

    public boolean isInternal() {
        return this.getType().equals(LinkTypes.internal);
    }

    public boolean isExternal() {
        return this.getType().equals(LinkTypes.external);
    }

    public List<Integer> getPortIds() {
        return Stream.of(from, to).filter(port_id -> port_id != 0).toList();
    }

    public boolean hasFrom() {
        return from != 0;
    }

    public boolean hasTo() {
        return to != 0;
    }

    public boolean lacksPortIds() {
        return from == 0 && to == 0;
    }
}
