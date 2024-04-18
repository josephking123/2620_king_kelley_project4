import java.util.regex.Pattern;

public class Filter {

    private String name;
    private Pattern pattern;

    public Filter(String name, String regex){
        this.name = name;
        this.pattern = Pattern.compile(regex);
    }

    public String getName() {
        return name;
    }

    public boolean applyFilter(String word){
        return pattern.matcher(word).find();
    }
}
