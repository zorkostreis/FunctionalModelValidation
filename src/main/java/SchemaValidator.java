import com.google.gson.Gson;

public class SchemaValidator {
    private Schema schema;
    SchemaValidator(String schema) {
        this.schema = new Gson().fromJson(schema, Schema.class);
    }
    public String validate() {
        return new Gson().toJson(schema.hasStartAndEndNode());
    }
}
