# Text Document Analyzer

A Java application that analyzes text documents and generates comprehensive word frequency statistics.

## Features

- **File I/O Operations**: Read text files and export formatted reports
- **Text Normalization**: Convert to lowercase, remove punctuation (keeps alphanumeric and spaces)
- **Word Tokenization**: Split text into words, filter words with fewer than 3 characters
- **Frequency Counting**: Track word occurrences using HashMap
- **Stop Word Filtering**: Exclude 30 common words (the, and, is, at, which, on, a, an, etc.)
- **Statistics Generation**: Total count, unique count, average length, longest word, most frequent word
- **Search Capabilities**: Find top N words, search by prefix

## Project Structure

```
IshaHarneAssignment2/
├── src/
│   ├── TextReader.java        # Reads and normalizes file content
│   ├── WordCounter.java       # Tokenizes and counts word frequencies
│   ├── StatisticsReport.java  # Generates formatted output reports
│   └── TextAnalyzer.java      # Main class coordinating the analysis
├── out/                       # Compiled .class files
├── sample_text.txt            # Sample input file (500+ words)
├── output_report.txt          # Generated analysis report
└── README.md
```

## Requirements

- Java JDK 8 or higher

## Compilation

```bash
javac -d out src/*.java
```

## Running the Application

### Using default files (sample_text.txt → output_report.txt)
```bash
java -cp out TextAnalyzer
```

### With custom input and output files
```bash
java -cp out TextAnalyzer <input_file> <output_file>
```

### Examples
```bash
# Analyze sample_text.txt and output to output_report.txt
java -cp out TextAnalyzer sample_text.txt output_report.txt

# Analyze a custom file
java -cp out TextAnalyzer myfile.txt myreport.txt
```

## Class Reference

### TextReader

Handles file reading and text normalization.

| Method | Description |
|--------|-------------|
| `TextReader(String filePath)` | Constructor with file path |
| `String readFile()` | Reads raw file content |
| `String normalizeText(String text)` | Converts to lowercase, removes punctuation |
| `String readAndNormalize()` | Reads and normalizes in one operation |
| `String getFilePath()` | Returns the file path |
| `String getNormalizedContent()` | Returns normalized content |

### WordCounter

Tokenizes text and manages word frequency data.

| Method | Description |
|--------|-------------|
| `WordCounter()` | Constructor, initializes stop words |
| `void processText(String text)` | Tokenizes and counts word frequencies |
| `int getTotalWordCount()` | Total words after filtering |
| `int getUniqueWordCount()` | Count of unique words |
| `double getAverageWordLength()` | Average length of all words |
| `String getLongestWord()` | Longest word in the text |
| `String getMostFrequentWord()` | Most frequently occurring word |
| `int getMostFrequentWordCount()` | Frequency of most common word |
| `List<Map.Entry<String, Integer>> getTopNWords(int n)` | Top N words sorted by frequency descending |
| `List<String> getWordsStartingWith(String prefix)` | Words matching prefix, sorted alphabetically |
| `Map<String, Integer> getWordFrequency()` | Full frequency map |
| `int getWordCount(String word)` | Frequency of specific word |
| `Set<String> getStopWords()` | Set of excluded stop words |

### StatisticsReport

Generates and exports formatted reports.

| Method | Description |
|--------|-------------|
| `StatisticsReport(WordCounter wc, String sourceFile)` | Constructor |
| `String generateReport()` | Generates full report string |
| `void exportReport(String outputFilePath)` | Writes report to file |
| `void printReport()` | Prints report to console |
| `String getSummary()` | Returns brief summary string |
| `String getTopWordsFormatted(int n)` | Formatted top N words |
| `String getWordsWithPrefixFormatted(String prefix)` | Formatted prefix search results |

### TextAnalyzer

Main class that coordinates the analysis process.

| Method | Description |
|--------|-------------|
| `TextAnalyzer(String inputFilePath)` | Constructor |
| `void analyze()` | Performs the full analysis |
| `List<Map.Entry<String, Integer>> getTopNWords(int n)` | Get top N frequent words |
| `List<String> getWordsStartingWith(String prefix)` | Search words by prefix |
| `void exportReport(String outputFilePath)` | Export report to file |
| `void printReport()` | Print report to console |

## Stop Words List

The following 30 words are excluded from analysis:

```
the, and, is, at, which, on, a, an, as, are, was, were, been, be,
have, has, had, do, does, did, for, of, to, in, it, its, this,
that, with, from
```

## Sample Output

```
============================================================
           TEXT DOCUMENT ANALYSIS REPORT
============================================================

Source File: sample_text.txt

----------------------------------------
BASIC STATISTICS
----------------------------------------
Total Word Count:      576
Unique Word Count:     374
Average Word Length:   7.73 characters
Longest Word:          containerization (16 characters)
Most Frequent Word:    software (appears 21 times)

----------------------------------------
TOP 10 MOST FREQUENT WORDS
----------------------------------------
 1. software             21
 2. development          17
 3. like                 11
 4. code                 9
 5. developers           8
 6. applications         6
 7. how                  6
 8. systems              6
 9. languages            5
10. modern               5
```

## Usage Example (Programmatic)

```java
// Create analyzer with input file
TextAnalyzer analyzer = new TextAnalyzer("sample_text.txt");

// Run analysis
analyzer.analyze();

// Get top 5 words
List<Map.Entry<String, Integer>> top5 = analyzer.getTopNWords(5);
for (Map.Entry<String, Integer> entry : top5) {
    System.out.println(entry.getKey() + ": " + entry.getValue());
}

// Search words by prefix
List<String> techWords = analyzer.getWordsStartingWith("tech");
System.out.println("Words starting with 'tech': " + techWords);

// Export report
analyzer.exportReport("output_report.txt");
```

