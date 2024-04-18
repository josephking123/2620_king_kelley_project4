import javax.swing.JProgressBar;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataHandler implements Runnable {
    private Map<String, Integer> wordFrequencyMap;
    private Set<String> encounteredWords;
    private JProgressBar progressBar;

    public DataHandler(String folderPath, JProgressBar progressBar) {
        wordFrequencyMap = new HashMap<>();
        encounteredWords = new HashSet<>();
        this.progressBar = progressBar;
        scanFolder(folderPath);
    }

    @Override
    public void run() {
        // Empty for now
    }

    private void scanFolder(String folderPath) {
        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                int totalFiles = files.length;
                int scannedFiles = 0;
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        readDataFromFile(file.getAbsolutePath());
                        scannedFiles++;
                        updateProgressBar(scannedFiles, totalFiles);
                    } else if (file.isDirectory()) {
                        scanFolder(file.getAbsolutePath()); 
                    }
                }
            }
        }
    }

    private void readDataFromFile(String filePath) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Pattern pattern = Pattern.compile("\\b\\p{L}{6,}\\b");
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String word = matcher.group().toLowerCase();
                    synchronized (wordFrequencyMap) {
                        if (!encounteredWords.contains(word)) {
                            wordFrequencyMap.merge(word, 1, Integer::sum);
                            encounteredWords.add(word);
                        }
                    }
                }
            }
            System.out.println("Successfully read data from file: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("Error reading data from file: " + filePath);
        }
    }

    private synchronized void updateProgressBar(int scannedFiles, int totalFiles) {
        if (progressBar != null) {
            int progress = (int) (((double) scannedFiles / totalFiles) * 100);
            progressBar.setValue(progress);
        }
    }

    public Map<String, Integer> getWordFrequencyMap() {
        return wordFrequencyMap;
    }

    public Set<String> getEncounteredWords() {
        return encounteredWords;
    }
}