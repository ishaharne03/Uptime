import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Main class that coordinates the text analysis process.
 * Demonstrates usage of TextReader, WordCounter, and StatisticsReport.
 */
public class TextAnalyzer {
    private TextReader textReader;
    private WordCounter wordCounter;
    private StatisticsReport statisticsReport;

    public TextAnalyzer(String inputFilePath) {
        this.textReader = new TextReader(inputFilePath);
        this.wordCounter = new WordCounter();
    }

    /**
     * Analyzes the text file and prepares statistics.
     */
    public void analyze() throws IOException {
        // Read and normalize the text
        String normalizedText = textReader.readAndNormalize();

        // Process the text and count words
        wordCounter.processText(normalizedText);

        // Create the statistics report
        statisticsReport = new StatisticsReport(wordCounter, textReader.getFilePath());
    }

    /**
     * Gets the top N most frequent words.
     */
    public List<Map.Entry<String, Integer>> getTopNWords(int n) {
        return wordCounter.getTopNWords(n);
    }

    /**
     * Gets words starting with the given prefix.
     */
    public List<String> getWordsStartingWith(String prefix) {
        return wordCounter.getWordsStartingWith(prefix);
    }

    /**
     * Exports the statistics report to a file.
     */
    public void exportReport(String outputFilePath) throws IOException {
        if (statisticsReport == null) {
            throw new IllegalStateException("Analysis has not been performed. Call analyze() first.");
        }
        statisticsReport.exportReport(outputFilePath);
    }

    /**
     * Prints the statistics report to console.
     */
    public void printReport() {
        if (statisticsReport == null) {
            throw new IllegalStateException("Analysis has not been performed. Call analyze() first.");
        }
        statisticsReport.printReport();
    }

    /**
     * Gets the word counter for direct access to statistics.
     */
    public WordCounter getWordCounter() {
        return wordCounter;
    }

    /**
     * Gets the statistics report instance.
     */
    public StatisticsReport getStatisticsReport() {
        return statisticsReport;
    }

    /**
     * Main method demonstrating the text analyzer functionality.
     */
    public static void main(String[] args) {
        String inputFile = "sample_text.txt";
        String outputFile = "output_report.txt";

        // Check for command line arguments
        if (args.length >= 1) {
            inputFile = args[0];
        }
        if (args.length >= 2) {
            outputFile = args[1];
        }

        System.out.println("Text Document Analyzer");
        System.out.println("========================================");
        System.out.println("Input file: " + inputFile);
        System.out.println();

        try {
            // Create and run analyzer
            TextAnalyzer analyzer = new TextAnalyzer(inputFile);
            analyzer.analyze();

            // Print full report
            analyzer.printReport();

            // Export report to file
            analyzer.exportReport(outputFile);

            // Demonstrate additional methods
            System.out.println();
            System.out.println("========================================");
            System.out.println("ADDITIONAL DEMONSTRATIONS");
            System.out.println("========================================");

            // Get top 5 words
            System.out.println();
            System.out.println("Top 5 Words:");
            List<Map.Entry<String, Integer>> top5 = analyzer.getTopNWords(5);
            for (int i = 0; i < top5.size(); i++) {
                Map.Entry<String, Integer> entry = top5.get(i);
                System.out.println("  " + entry.getKey() + ": " + entry.getValue());
            }

            // Get words starting with specific prefixes
            String[] prefixes = {"pro", "dev", "tech"};
            for (int i = 0; i < prefixes.length; i++) {
                String prefix = prefixes[i];
                System.out.println();
                System.out.println("Words starting with '" + prefix + "':");
                List<String> words = analyzer.getWordsStartingWith(prefix);
                if (words.isEmpty()) {
                    System.out.println("  No words found.");
                } else {
                    for (int j = 0; j < words.size(); j++) {
                        String word = words.get(j);
                        int count = analyzer.getWordCounter().getWordCount(word);
                        System.out.println("  - " + word + " (" + count + ")");
                    }
                }
            }

        } catch (IOException e) {
            System.err.println("Error: " + e.getMessage());
            System.err.println("Please ensure the input file exists: " + inputFile);
            System.exit(1);
        } catch (Exception e) {
            System.err.println("Unexpected error: " + e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }
    }
}
