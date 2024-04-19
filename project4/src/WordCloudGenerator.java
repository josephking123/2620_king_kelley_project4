import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.kennycason.kumo.CollisionMode;
import com.kennycason.kumo.WordCloud;
import com.kennycason.kumo.WordFrequency;
import com.kennycason.kumo.font.scale.LinearFontScalar;
import com.kennycason.kumo.palette.ColorPalette;

public class WordCloudGenerator {
    private WordCloud cloud;

    public WordCloudGenerator(Map<String,Integer> map, Set<String> words){

        Dimension d = new Dimension(600, 600);
        cloud = new WordCloud(d, CollisionMode.PIXEL_PERFECT);
        cloud.setPadding(2);
        cloud.setColorPalette(new ColorPalette(new Color(0x4055F1), new Color(0x408DF1), new Color(0x40AAF1), new Color(0x40C5F1), new Color(0x40D3F1), new Color(0xFFFFFF)));
        cloud.setFontScalar(new LinearFontScalar(10, 40));
        List<WordFrequency> list = new ArrayList<>();
        for (String word : words) {
            Integer frequency = map.getOrDefault(word, 0);
            list.add(new WordFrequency(word, frequency));
        }

        cloud.build(list);        
    }

    public WordCloud getCloud() {
        return cloud;
    }
}
