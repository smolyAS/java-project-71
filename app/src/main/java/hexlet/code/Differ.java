package hexlet.code;
import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.TreeMap;

public class Differ {

    public static String generateDiff(JsonObject json1, JsonObject json2, String format) {
        Map<String, String> resultMap = new TreeMap<>();

        for (Map.Entry<String, JsonElement> entry : json1.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            if (json2.has(key)) {
                if (!json2.get(key).equals(value)) {
                    resultMap.put(key, "- " + key + ": " + value + System.lineSeparator()
                            + "+ " + key + ": " + json2.get(key) + System.lineSeparator());
                } else {
                    resultMap.put(key, "  " + key + ": " + value + System.lineSeparator());
                }
            } else {
                resultMap.put(key, "- " + key + ": " + value + System.lineSeparator());
            }
        }

        for (Map.Entry<String, JsonElement> entry : json2.entrySet()) {
            String key = entry.getKey();
            JsonElement value = entry.getValue();

            if (!json1.has(key)) {
                resultMap.put(key, "+ " + key + ": " + value + System.lineSeparator());
            }
        }

        StringBuilder diffOutput = new StringBuilder();
        for (String diffLine : resultMap.values()) {
            diffOutput.append(diffLine);
        }

        if ("stylish".equals(format)) {
            return diffOutput.toString();
        } else if ("json".equals(format)) {
            JsonObject diffObj = new JsonObject();
            for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                diffObj.addProperty(entry.getKey(), entry.getValue());
            }
            return diffObj.toString();
        } else {
            return "Unsupported format: " + format;
        }
    }

    public static String generate(String filePath1, String filePath2, String format) {
        try {
            String content1 = new String(Files.readAllBytes(Paths.get(filePath1)));
            String content2 = new String(Files.readAllBytes(Paths.get(filePath2)));

            JsonObject json1 = JsonParser.parseString(content1).getAsJsonObject();
            JsonObject json2 = JsonParser.parseString(content2).getAsJsonObject();

            return generateDiff(json1, json2, format);
        } catch (IOException e) {
            System.err.println("Error reading files: " + e.getMessage());
            return null;
        }
    }
}
