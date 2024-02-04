package hexlet.code;

import com.google.gson.JsonObject;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.Map;

public class Differ {
    public static String generate(String filePath1, String filePath2) {
        try {
            String content1 = new String(Files.readAllBytes(Paths.get(filePath1)));
            String content2 = new String(Files.readAllBytes(Paths.get(filePath2)));

            JsonObject json1 = JsonParser.parseString(content1).getAsJsonObject();
            JsonObject json2 = JsonParser.parseString(content2).getAsJsonObject();

            SortedMap<String, JsonElement> diff = generateDiff(json1, json2);

            return formatDiff(diff);
        } catch (IOException e) {
            System.err.println("Error reading files: " + e.getMessage());
            return null;
        }
    }

    private static SortedMap<String, JsonElement> generateDiff(JsonObject json1, JsonObject json2) {
        SortedMap<String, JsonElement> diff = new TreeMap<>();

        for (Map.Entry<String, JsonElement> entry : json1.entrySet()) {
            String key = entry.getKey();
            JsonElement value1 = entry.getValue();
            JsonElement value2 = json2.get(key);

            if (value2 == null) {
                diff.put("- " + key, value1);
            } else if (value2.equals(value1)) {
                diff.put("  " + key, value1);
            } else {
                diff.put("- " + key, value1);
                diff.put("+ " + key, value2);
            }
        }

        for (Map.Entry<String, JsonElement> entry : json2.entrySet()) {
            String key = entry.getKey();
            JsonElement value2 = entry.getValue();
            JsonElement value1 = json1.get(key);

            if (value1 == null) {
                diff.put("+ " + key, value2);
            }
        }

        return diff;
    }

    private static String formatDiff(SortedMap<String, JsonElement> diff) {
        StringBuilder formattedOutput = new StringBuilder();

        for (Map.Entry<String, JsonElement> entry : diff.entrySet()) {
            String prefix = entry.getKey().startsWith(" ") ? "" : " ";
            formattedOutput.append(prefix).append(entry.getKey()).append(": ").append(entry.getValue()).append(System.lineSeparator());
        }

        return formattedOutput.toString().trim();
    }
}