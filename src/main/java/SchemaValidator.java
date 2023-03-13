import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;

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

        return new Gson().toJson(buildResponse());
    }

    private HashMap<String, Object> buildResponse() {
        HashMap<String, Object> response = new HashMap<>();
        HashMap<String, HashMap<String, ArrayList<Object>>> errors = schema.getErrors();

        errors.entrySet().removeIf(ent -> ent.getValue().isEmpty());
        if (errors.isEmpty())
            response.put("success", true);
        else {
            response.put("success", false);
            response.putAll(errors);
        }

        return response;
    }
}
