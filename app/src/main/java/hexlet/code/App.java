package hexlet.code;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

@Command(name = "app", mixinStandardHelpOptions = true, version = "1.0",
        description = "Compares two configuration files and shows the difference.")
public class App implements Runnable {

    @Parameters(index = "0", description = "Path to the first file.")
    private String filePath1;

    @Parameters(index = "1", description = "Path to the second file.")
    private String filePath2;

    public static void main(String[] args) {
        CommandLine.run(new App(), args);
    }

    @Override
    public void run() {
        try {
            String content1 = readFileContent(filePath1);
            String content2 = readFileContent(filePath2);

            // TODO: Perform comparison and generate output

        } catch (IOException e) {
            System.err.println("Error reading input files: " + e.getMessage());
        }
    }

    private String readFileContent(String filePath) throws IOException {
        byte[] bytes = Files.readAllBytes(Paths.get(filePath));
        return new String(bytes);
    }

    public static String generate(Map<String, String> data1, Map<String, String> data2) throws Exception {
        Set<String> allKeys = new TreeSet<>(data1.keySet());
        allKeys.addAll(data2.keySet());

        StringBuilder diffBuilder = new StringBuilder();

        for (String key : allKeys) {
            String value1 = data1.get(key);
            String value2 = data2.get(key);

            if (value1 == null) {
                diffBuilder.append(key).append(" - ").append(value2).append(System.lineSeparator());
            } else if (value2 == null) {
                diffBuilder.append(key).append(" + ").append(value1).append(System.lineSeparator());
            } else if (!value1.equals(value2)) {
                diffBuilder.append(key).append(" - ").append(value2).append(System.lineSeparator());
                diffBuilder.append(key).append(" + ").append(value1).append(System.lineSeparator());
            }
        }

        return diffBuilder.toString();
    }
}