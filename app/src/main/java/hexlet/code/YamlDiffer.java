package hexlet.code;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import java.io.File;
import java.io.IOException;
import java.util.Iterator;


public class YamlDiffer {

    public static String generate(String filePath1, String filePath2, String format) {
        try {
            ObjectMapper mapper = new ObjectMapper(new YAMLFactory());
            JsonNode yaml1Data = mapper.readTree(new File(filePath1));
            JsonNode yaml2Data = mapper.readTree(new File(filePath2));

            String diff = generateDiff(yaml1Data, yaml2Data);

            return diff;  // Возвращаем строку с различиями
        } catch (IOException e) {
            System.err.println("Error reading files: " + e.getMessage());
            return null;
        }
    }

    public static String generateDiff(JsonNode node1, JsonNode node2) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode diff = objectMapper.createObjectNode();

        // Генерация diff между node1 и node2
        // Например, можно рекурсивно обходить структуру и сравнивать значения

        // Пример рекурсивного сравнения json nodes
        compareNodes(node1, node2, "", diff);

        try {
            return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(diff);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private static void compareNodes(JsonNode node1, JsonNode node2, String path, ObjectNode diff) {
        if (!node1.equals(node2)) {
            diff.put(path, "Value in file 1: " + node1 + ", Value in file 2: " + node2);
        } else if (node1.isObject()) {
            for (Iterator<String> it = node1.fieldNames(); it.hasNext();) {
                String field = it.next();
                compareNodes(node1.get(field), node2.get(field), path + "." + field, diff);
            }
        }
    }
}
