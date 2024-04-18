import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;

public class WordCloudGenerator {
    private WordCloud cloud;

    public WordCloudGenerator(Map<String,Integer> map, Set<String> words){
        final Dimension d = new Dimension(600, 600);
        cloud = new WordCloud(d, CollisionMode.PIXEL_PERFECT);
        List<WordFrequency> list = new ArrayList<>();
        for (String word : words) {
            Integer frequency = map.getOrDefault(word, 0);
            list.add(new WordFrequency(word, frequency));
        }
        System.out.println("Word frequencies before building WordCloud:");
        list.forEach(wordFrequency -> System.out.println(wordFrequency.getWord() + ": " + wordFrequency.getFrequency()));
        cloud.build(list);
    }

    public WordCloud getCloud() {
        return cloud;
    }
}
