import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataHandler implements Runnable {
    private Map<String, Integer> wordFrequencyMap;
    private String filePath = "lib\\gutenberg-data";

    public DataHandler(String filePath) {
        this.filePath = filePath;
        wordFrequencyMap = new HashMap<>();
    }

    @Override
    public void run() {
        readDataFromFile();
    }

    private void readDataFromFile() {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            Pattern pattern = Pattern.compile("\\b\\w{6,}\\b");
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    String word = matcher.group().toLowerCase();
                    synchronized (wordFrequencyMap) {
                        wordFrequencyMap.merge(word, 1, Integer::sum);
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

}
