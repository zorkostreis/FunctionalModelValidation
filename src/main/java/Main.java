import static spark.Spark.*;

public class Main {
    public static void main(String[] args) {
        get("/hello", (req, res) -> {
            res.type("application/json");

            SchemaValidator validator = new SchemaValidator(req.body());
            return validator.validate();
        });
    }
}