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

    public DataHandler(String folderPath) {
        wordFrequencyMap = new HashMap<>();
        encounteredWords = new HashSet<>();
        scanFolder(folderPath);
    }

    @Override
    public void run() {
        
    }

    private void scanFolder(String folderPath) {
        File folder = new File(folderPath);
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".txt")) {
                        readDataFromFile(file.getAbsolutePath());
                    } else if (file.isDirectory()) {
                        DataHandler subfolderHandler = new DataHandler(file.getAbsolutePath());
                        Thread thread = new Thread(subfolderHandler);
                        thread.start();
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
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }

    public Map<String, Integer> getWordFrequencyMap() {
        return wordFrequencyMap;
    }

    public Set<String> getEncounteredWords() {
        return encounteredWords;
    }

}
