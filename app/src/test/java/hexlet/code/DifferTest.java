import com.google.gson.JsonObject;
import hexlet.code.Differ;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class DifferTest {

    @Test
    void testGenerateDiffWithChangedValue() {
        JsonObject json1 = new JsonObject();
        json1.addProperty("age", 30);
        json1.addProperty("name", "John Doe");

        JsonObject json2 = new JsonObject();
        json2.addProperty("age", 31); // Changed value
        json2.addProperty("name", "John Doe");

        String expectedDiff1 = "- age: 30";
        String expectedDiff2 = "+ age: 31";

        String actualDiff = Differ.generateDiff(json1, json2, "stylish");
        assertTrue(actualDiff.contains(expectedDiff1));
        assertTrue(actualDiff.contains(expectedDiff2));
    }

    @Test
    void testGenerateDiffWithAddedAndRemovedFields() {
        JsonObject json1 = new JsonObject();
        json1.addProperty("age", 30);
        json1.addProperty("name", "John Doe");

        JsonObject json2 = new JsonObject();
        json2.addProperty("name", "John Doe");
        json2.addProperty("active", true);  // Added field

        String expectedDiff1 = "- age: 30";
        String expectedDiff2 = "+ active: true";

        String actualDiff = Differ.generateDiff(json1, json2, "stylish");
        assertTrue(actualDiff.contains(expectedDiff1));
        assertTrue(actualDiff.contains(expectedDiff2));
    }
}

