import java.util.Set;

class StopWordsFilter implements WordFilter{
    private Set<String> map;

    public StopWordsFilter(Set<String> map){
        this.map = map;
    }
    @Override
    public boolean accept(String word){
        return !map.contains(word);
    }
}