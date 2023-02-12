import com.google.gson.Gson;

public class SchemaValidator {
    private Schema schema;

    SchemaValidator(String schema) {
        this.schema = new Gson().fromJson(schema, Schema.class);
    }

    public String validate() {
        return new Gson().toJson(schema.linkedFromStartToEnd());
    }

    private boolean validatePersistence() {
        return schema.hasStartAndEndNode() && schema.linkedFromStartToEnd();
    }

    private boolean validatePortTypes() {
        return schema.getStartNode().hasOnlyOutputPorts() && schema.getEndNode().hasOnlyInputPorts();
    }
}
