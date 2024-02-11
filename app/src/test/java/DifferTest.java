import com.google.gson.JsonObject;
import hexlet.code.Differ;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class DifferTest {

    @Test
    void testGenerateDiffWithChangedValue() {
        JsonObject json1 = new JsonObject();
        json1.addProperty("age", 30);
        json1.addProperty("name", "John Doe");

        JsonObject json2 = new JsonObject();
        json2.addProperty("age", 31); // Changed value
        json2.addProperty("name", "John Doe");

        String expectedDiff = "- age: 30\n+ age: 31\n  name: John Doe\n";

        String actualDiff = Differ.generateDiff(json1, json2, "stylish");

        assertEquals(expectedDiff, actualDiff);
    }

    @Test
    void testGenerateDiffWithAddedAndRemovedFields() {
        JsonObject json1 = new JsonObject();
        json1.addProperty("age", 30);
        json1.addProperty("name", "John Doe");

        JsonObject json2 = new JsonObject();
        json2.addProperty("name", "John Doe");
        json2.addProperty("active", true);  // Added field

        String expectedDiff = "- age: 30\n- name: John Doe\n+ active: true\n";

        String actualDiff = Differ.generateDiff(json1, json2, "stylish");

        assertEquals(expectedDiff, actualDiff);
    }

    @Test
    void testGenerateDiffWithNoChanges() {
        JsonObject json1 = new JsonObject();
        json1.addProperty("age", 30);
        json1.addProperty("name", "John Doe");

        JsonObject json2 = new JsonObject();
        json2.addProperty("age", 30);
        json2.addProperty("name", "John Doe");

        String expectedDiff = "  age: 30\n  name: John Doe\n";

        String actualDiff = Differ.generateDiff(json1, json2, "stylish");

        assertEquals(expectedDiff, actualDiff);
    }
}

