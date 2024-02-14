package hexlet.code;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.util.Map;
import java.util.LinkedHashMap;
import java.util.Iterator;
import java.util.TreeMap;

public class YamlDiffer {

    public static JsonNode generate(String filePath) {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            return mapper.readTree(new File(filePath));
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Map<String, Object> convertJsonNodeToMap(JsonNode node) {
        return new ObjectMapper().convertValue(node, new TypeReference<Map<String, Object>>() { });
    }

    private static Map<String, Object> compareNodes(JsonNode node1, JsonNode node2, String path) {
        Map<String, Object> diff = new LinkedHashMap<>();

        if (!node1.equals(node2)) {
            diff.put("- " + path, convertJsonNodeToMap(node1));
            diff.put("+ " + path, convertJsonNodeToMap(node2));
        } else if (node1.isObject()) {
            Iterator<Map.Entry<String, JsonNode>> fields = node1.fields();
            while (fields.hasNext()) {
                Map.Entry<String, JsonNode> field = fields.next();
                String fieldName = field.getKey();
                JsonNode value1 = field.getValue();
                JsonNode value2 = node2.get(fieldName);
                // Рекурсивный вызов для вложенных узлов
                Map<String, Object> nestedDiff = compareNodes(value1, value2, fieldName);
                if (!nestedDiff.isEmpty()) {
                    diff.putAll(nestedDiff);
                }
            }
        }
        return diff;
    }

    public static String generateDiff(JsonNode node1, JsonNode node2) {
        Map<String, Object> diffMap = compareNodes(node1, node2, "");
        Map<String, Object> sortedDiffMap = new TreeMap<>(diffMap);
        return printDiff(sortedDiffMap, "");
    }

    private static String printDiff(Map<String, Object> diffMap, String indent) {
        StringBuilder diff = new StringBuilder("{\n");
        for (Map.Entry<String, Object> entry : diffMap.entrySet()) {
            String path = entry.getKey();
            Object value = entry.getValue();
            if (value instanceof Map) { // Nested Difference
                diff.append(indent).append(" ").append(path).append(": ");
                diff.append(printDiff((Map<String, Object>) value, indent + " "));
            } else { // Leaf Difference
                if (path.startsWith("-") || path.startsWith("+")) {
                    diff.append(indent).append(" ").append(path).append(": ").append(value.toString()).append("\n");
                } else {
                    diff.append(indent).append(" ").append(path).append(": ").append(value.toString()).append("\n");
                }
            }
        }
        return diff.append(indent).append("}\n").toString();
    }
}
