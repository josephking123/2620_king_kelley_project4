import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;

public class WordCloudGenerator {

    private WordCloud cloud;

    public WordCloudGenerator(Map<String,Integer> map, Set<String> words){
        Iterator<String> it = words.iterator();
        List<WordFrequency> list = new ArrayList<WordFrequency>();
        while(it.hasNext() == true){
            String word = it.next();
            WordFrequency freq = new WordFrequency(word, map.get(word));
            list.add(freq);
        }
        cloud.build(list);
    }

    public WordCloud getCloud() {
        return cloud;
    }

}
