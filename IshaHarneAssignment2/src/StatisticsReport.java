import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Generates formatted statistics reports from word analysis.
 */
public class StatisticsReport {
    private WordCounter wordCounter;
    private String sourceFile;

    public StatisticsReport(WordCounter wordCounter, String sourceFile) {
        this.wordCounter = wordCounter;
        this.sourceFile = sourceFile;
    }

    /**
     * Helper method to repeat a character n times.
     */
    private String repeatChar(char c, int times) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < times; i++) {
            sb.append(c);
        }
        return sb.toString();
    }

    /**
     * Generates a formatted report string with all statistics.
     */
    public String generateReport() {
        StringBuilder report = new StringBuilder();

        report.append(repeatChar('=', 60)).append("\n");
        report.append("           TEXT DOCUMENT ANALYSIS REPORT\n");
        report.append(repeatChar('=', 60)).append("\n\n");

        report.append("Source File: ").append(sourceFile).append("\n\n");

        // Basic Statistics
        report.append(repeatChar('-', 40)).append("\n");
        report.append("BASIC STATISTICS\n");
        report.append(repeatChar('-', 40)).append("\n");
        report.append("Total Word Count:      ").append(wordCounter.getTotalWordCount()).append("\n");
        report.append("Unique Word Count:     ").append(wordCounter.getUniqueWordCount()).append("\n");

        // Format average to 2 decimal places
        double avgLength = wordCounter.getAverageWordLength();
        String avgFormatted = String.format("%.2f", avgLength);
        report.append("Average Word Length:   ").append(avgFormatted).append(" characters\n");

        String longestWord = wordCounter.getLongestWord();
        report.append("Longest Word:          ").append(longestWord);
        report.append(" (").append(longestWord.length()).append(" characters)\n");

        String mostFrequent = wordCounter.getMostFrequentWord();
        int mostFrequentCount = wordCounter.getMostFrequentWordCount();
        report.append("Most Frequent Word:    ").append(mostFrequent);
        report.append(" (appears ").append(mostFrequentCount).append(" times)\n");
        report.append("\n");

        // Top 10 Words
        report.append(repeatChar('-', 40)).append("\n");
        report.append("TOP 10 MOST FREQUENT WORDS\n");
        report.append(repeatChar('-', 40)).append("\n");

        List<Map.Entry<String, Integer>> topWords = wordCounter.getTopNWords(10);
        int rank = 1;
        for (Map.Entry<String, Integer> entry : topWords) {
            // Format rank with padding
            String rankStr = String.format("%2d", rank);
            // Format word with padding
            String wordStr = String.format("%-20s", entry.getKey());
            report.append(rankStr).append(". ").append(wordStr).append(entry.getValue()).append("\n");
            rank = rank + 1;
        }
        report.append("\n");

        // Word Length Distribution
        report.append(repeatChar('-', 40)).append("\n");
        report.append("WORD LENGTH DISTRIBUTION\n");
        report.append(repeatChar('-', 40)).append("\n");

        Map<String, Integer> frequencies = wordCounter.getWordFrequency();
        int[] lengthCounts = new int[20]; // Support words up to 19 characters

        for (String word : frequencies.keySet()) {
            int len = word.length();
            int count = frequencies.get(word);
            if (len < lengthCounts.length) {
                lengthCounts[len] = lengthCounts[len] + count;
            }
        }

        for (int i = 3; i < lengthCounts.length; i++) {
            if (lengthCounts[i] > 0) {
                String lenStr = String.format("%2d", i);
                report.append(lenStr).append(" characters: ").append(lengthCounts[i]).append(" words\n");
            }
        }
        report.append("\n");

        // Stop Words Info
        report.append(repeatChar('-', 40)).append("\n");
        report.append("STOP WORDS EXCLUDED\n");
        report.append(repeatChar('-', 40)).append("\n");
        report.append("The following stop words were excluded from analysis:\n");

        // Join stop words with commas
        Set<String> stopWords = wordCounter.getStopWords();
        StringBuilder stopWordList = new StringBuilder();
        boolean first = true;
        for (String word : stopWords) {
            if (!first) {
                stopWordList.append(", ");
            }
            stopWordList.append(word);
            first = false;
        }
        report.append(stopWordList.toString());
        report.append("\n\n");

        report.append(repeatChar('=', 60)).append("\n");
        report.append("                 END OF REPORT\n");
        report.append(repeatChar('=', 60)).append("\n");

        return report.toString();
    }

    /**
     * Exports the full statistics report to a file.
     */
    public void exportReport(String outputFilePath) throws IOException {
        String report = generateReport();
        BufferedWriter writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(outputFilePath));
            writer.write(report);
        } finally {
            if (writer != null) {
                writer.close();
            }
        }
        System.out.println("Report exported to: " + outputFilePath);
    }

    /**
     * Prints the report to console.
     */
    public void printReport() {
        System.out.println(generateReport());
    }

    /**
     * Gets a summary string of key statistics.
     */
    public String getSummary() {
        String avgFormatted = String.format("%.2f", wordCounter.getAverageWordLength());
        return "Summary: " + wordCounter.getTotalWordCount() + " total words, " +
               wordCounter.getUniqueWordCount() + " unique words, avg length " +
               avgFormatted + ", most frequent: '" + wordCounter.getMostFrequentWord() +
               "' (" + wordCounter.getMostFrequentWordCount() + " times)";
    }

    /**
     * Gets the top N words as a formatted string.
     */
    public String getTopWordsFormatted(int n) {
        StringBuilder sb = new StringBuilder();
        sb.append("Top ").append(n).append(" Words:\n");

        List<Map.Entry<String, Integer>> topWords = wordCounter.getTopNWords(n);
        int rank = 1;
        for (Map.Entry<String, Integer> entry : topWords) {
            sb.append("  ").append(rank).append(". ");
            sb.append(entry.getKey()).append(" (").append(entry.getValue()).append(")\n");
            rank = rank + 1;
        }
        return sb.toString();
    }

    /**
     * Gets words starting with prefix as a formatted string.
     */
    public String getWordsWithPrefixFormatted(String prefix) {
        StringBuilder sb = new StringBuilder();
        sb.append("Words starting with '").append(prefix).append("':\n");

        List<String> words = wordCounter.getWordsStartingWith(prefix);
        if (words.isEmpty()) {
            sb.append("  No words found.\n");
        } else {
            for (int i = 0; i < words.size(); i++) {
                String word = words.get(i);
                sb.append("  - ").append(word);
                sb.append(" (").append(wordCounter.getWordCount(word)).append(")\n");
            }
        }
        return sb.toString();
    }
}
