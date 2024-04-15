import java.util.regex.Pattern;

public class Filter {

    private Pattern pattern;

    public Filter(String regex){
        this.pattern = Pattern.compile(regex);
    }

    public boolean applyFilter(String word){
        return pattern.matcher(word).find();
    }
}
