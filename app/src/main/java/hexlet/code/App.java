package hexlet.code;
import com.fasterxml.jackson.databind.JsonNode;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;

@Command(name = "gendiff", mixinStandardHelpOptions = true, version = "gendiff 1.0",
        description = "Compares two configuration files and shows a difference.")
public class App implements Runnable {

    @Option(names = {"-h", "--help"}, usageHelp = true, description = "Show this help message and exit.")
    private boolean helpRequested;

    @Option(names = {"-V", "--version"}, versionHelp = true, description = "Print version information and exit.")
    private boolean versionRequested;

    @Option(names = {"-f", "--format"}, description = "output format [default: stylish]")
    private String format = "stylish";

    @Parameters(index = "0", description = "file1.yml")
    private String filePath1;

    @Parameters(index = "1", description = "file2.yml")
    private String filePath2;

    public static void main(String[] args) {
        CommandLine.run(new App(), args);
    }

    @Override
    public void run() {
        if (filePath1 != null && filePath2 != null) {
            JsonNode node1 = YamlDiffer.generate(filePath1);
            JsonNode node2 = YamlDiffer.generate(filePath2);
            String result = YamlDiffer.generateDiff(node1, node2);
            System.out.println(result);
        } else {
            System.out.println("Please provide file paths for comparison.");
        }
    }
}
