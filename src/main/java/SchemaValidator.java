import com.google.gson.Gson;

public class SchemaValidator {
    private Schema schema;

    SchemaValidator(String schema) {
        this.schema = new Gson().fromJson(schema, Schema.class);
    }

    public String validate() {
        schema.nodesHaveValidPortTypes();
        schema.nodesHavePortsOfEachType();
        schema.allPortsLinked();
        schema.allPortIdsFromLinksExist();
        schema.validateLinks();

        return new Gson().toJson(schema.getErrors());
    }
}
