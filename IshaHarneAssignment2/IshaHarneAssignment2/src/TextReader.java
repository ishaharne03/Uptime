import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Reads and normalizes text file content.
 * Handles file I/O and text preprocessing.
 */
public class TextReader {
    private String filePath;
    private String normalizedContent;

    public TextReader(String filePath) {
        this.filePath = filePath;
        this.normalizedContent = "";
    }

    /**
     * Reads the file and returns raw content.
     */
    public String readFile() throws IOException {
        StringBuilder content = new StringBuilder();
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(" ");
            }
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
        return content.toString();
    }

    /**
     * Normalizes text by converting to lowercase and removing punctuation.
     * Keeps only alphanumeric characters and spaces.
     */
    public String normalizeText(String text) {
        // Convert to lowercase
        String lowercased = text.toLowerCase();

        // Remove punctuation, keep only alphanumeric and spaces
        StringBuilder normalized = new StringBuilder();
        for (int i = 0; i < lowercased.length(); i++) {
            char c = lowercased.charAt(i);
            if (Character.isLetterOrDigit(c) || c == ' ') {
                normalized.append(c);
            } else {
                // Replace punctuation with space to avoid word concatenation
                normalized.append(' ');
            }
        }

        // Collapse multiple spaces into single space
        String result = normalized.toString();
        StringBuilder collapsed = new StringBuilder();
        boolean lastWasSpace = false;

        for (int i = 0; i < result.length(); i++) {
            char c = result.charAt(i);
            if (c == ' ') {
                if (!lastWasSpace) {
                    collapsed.append(c);
                }
                lastWasSpace = true;
            } else {
                collapsed.append(c);
                lastWasSpace = false;
            }
        }

        normalizedContent = collapsed.toString().trim();
        return normalizedContent;
    }

    /**
     * Reads and normalizes the file content in one operation.
     */
    public String readAndNormalize() throws IOException {
        String rawContent = readFile();
        return normalizeText(rawContent);
    }

    /**
     * Gets the file path.
     */
    public String getFilePath() {
        return filePath;
    }

    /**
     * Gets the normalized content (after readAndNormalize is called).
     */
    public String getNormalizedContent() {
        return normalizedContent;
    }
}
