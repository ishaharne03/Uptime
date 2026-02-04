import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tokenizes text and counts word frequencies.
 * Handles stop word filtering and word statistics.
 */
public class WordCounter {
    private Map<String, Integer> wordFrequency;
    private List<String> allWords;
    private Set<String> stopWords;
    private static final int MIN_WORD_LENGTH = 3;

    public WordCounter() {
        this.wordFrequency = new HashMap<String, Integer>();
        this.allWords = new ArrayList<String>();
        this.stopWords = initializeStopWords();
    }

    /**
     * Initializes the set of stop words to be excluded.
     * Contains at least 20 common stop words.
     */
    private Set<String> initializeStopWords() {
        Set<String> stops = new HashSet<String>();
        // Required 20 stop words
        stops.add("the");
        stops.add("and");
        stops.add("is");
        stops.add("at");
        stops.add("which");
        stops.add("on");
        stops.add("a");
        stops.add("an");
        stops.add("as");
        stops.add("are");
        stops.add("was");
        stops.add("were");
        stops.add("been");
        stops.add("be");
        stops.add("have");
        stops.add("has");
        stops.add("had");
        stops.add("do");
        stops.add("does");
        stops.add("did");
        // Additional common stop words
        stops.add("for");
        stops.add("of");
        stops.add("to");
        stops.add("in");
        stops.add("it");
        stops.add("its");
        stops.add("this");
        stops.add("that");
        stops.add("with");
        stops.add("from");
        return stops;
    }

    /**
     * Tokenizes text into words and counts frequencies.
     * Ignores words with fewer than 3 characters and stop words.
     */
    public void processText(String normalizedText) {
        wordFrequency.clear();
        allWords.clear();

        String[] tokens = normalizedText.split("\\s+");

        for (int i = 0; i < tokens.length; i++) {
            String token = tokens[i];

            if (token.isEmpty()) {
                continue;
            }

            // Skip words shorter than minimum length
            if (token.length() < MIN_WORD_LENGTH) {
                continue;
            }

            // Skip stop words
            if (stopWords.contains(token)) {
                continue;
            }

            allWords.add(token);

            // Update frequency count
            if (wordFrequency.containsKey(token)) {
                int currentCount = wordFrequency.get(token);
                wordFrequency.put(token, currentCount + 1);
            } else {
                wordFrequency.put(token, 1);
            }
        }
    }

    /**
     * Gets the total count of words (after filtering).
     */
    public int getTotalWordCount() {
        return allWords.size();
    }

    /**
     * Gets the count of unique words.
     */
    public int getUniqueWordCount() {
        return wordFrequency.size();
    }

    /**
     * Calculates the average word length.
     */
    public double getAverageWordLength() {
        if (allWords.isEmpty()) {
            return 0.0;
        }
        int totalLength = 0;
        for (int i = 0; i < allWords.size(); i++) {
            totalLength = totalLength + allWords.get(i).length();
        }
        return (double) totalLength / allWords.size();
    }

    /**
     * Finds the longest word in the text.
     */
    public String getLongestWord() {
        String longest = "";
        for (String word : wordFrequency.keySet()) {
            if (word.length() > longest.length()) {
                longest = word;
            }
        }
        return longest;
    }

    /**
     * Finds the most frequent word.
     */
    public String getMostFrequentWord() {
        String mostFrequent = "";
        int maxCount = 0;

        for (String word : wordFrequency.keySet()) {
            int count = wordFrequency.get(word);
            if (count > maxCount) {
                maxCount = count;
                mostFrequent = word;
            }
        }
        return mostFrequent;
    }

    /**
     * Gets the frequency of the most frequent word.
     */
    public int getMostFrequentWordCount() {
        int maxCount = 0;
        for (String word : wordFrequency.keySet()) {
            int count = wordFrequency.get(word);
            if (count > maxCount) {
                maxCount = count;
            }
        }
        return maxCount;
    }

    /**
     * Returns the n most frequent words sorted by count descending.
     * Uses simple sorting with Comparator.
     */
    public List<Map.Entry<String, Integer>> getTopNWords(int n) {
        // Convert map entries to a list
        List<Map.Entry<String, Integer>> entryList = new ArrayList<Map.Entry<String, Integer>>();
        for (Map.Entry<String, Integer> entry : wordFrequency.entrySet()) {
            entryList.add(entry);
        }

        // Sort by count (descending), then alphabetically if counts are equal
        Collections.sort(entryList, new Comparator<Map.Entry<String, Integer>>() {
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                // Compare counts in descending order
                int countCompare = e2.getValue().compareTo(e1.getValue());
                if (countCompare != 0) {
                    return countCompare;
                }
                // If counts are equal, sort alphabetically
                return e1.getKey().compareTo(e2.getKey());
            }
        });

        // Get top N entries
        List<Map.Entry<String, Integer>> topN = new ArrayList<Map.Entry<String, Integer>>();
        for (int i = 0; i < n && i < entryList.size(); i++) {
            topN.add(entryList.get(i));
        }

        return topN;
    }

    /**
     * Returns words starting with the given prefix, sorted alphabetically.
     */
    public List<String> getWordsStartingWith(String prefix) {
        String lowerPrefix = prefix.toLowerCase();
        List<String> matchingWords = new ArrayList<String>();

        // Find all words that start with the prefix
        for (String word : wordFrequency.keySet()) {
            if (word.startsWith(lowerPrefix)) {
                matchingWords.add(word);
            }
        }

        // Sort alphabetically
        Collections.sort(matchingWords);

        return matchingWords;
    }

    /**
     * Gets the word frequency map.
     */
    public Map<String, Integer> getWordFrequency() {
        return new HashMap<String, Integer>(wordFrequency);
    }

    /**
     * Gets the frequency of a specific word.
     */
    public int getWordCount(String word) {
        String lowerWord = word.toLowerCase();
        if (wordFrequency.containsKey(lowerWord)) {
            return wordFrequency.get(lowerWord);
        }
        return 0;
    }

    /**
     * Gets the set of stop words.
     */
    public Set<String> getStopWords() {
        return new HashSet<String>(stopWords);
    }
}
