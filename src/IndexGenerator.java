import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class IndexGenerator {

    private Map<String, Set<Integer>> keywords = new HashMap<>();

    public IndexGenerator(List<Sentence> sentences, WordFilter filter) {
        for (Sentence sentence : sentences) {
            for (String word : sentence.getWords()) {
                if (filter.accept(word)){
                keywords.putIfAbsent(word, new java.util.HashSet<>());
                keywords.get(word).add(sentence.getLineNum());
                }
            }
        }
    }

    public Map<String, Set<Integer>> getIndexMap(){
        return keywords;
    }

    public List<String[]> getAllWordCount(){
        List<String[]> result = new java.util.ArrayList<>();
        for (Map.Entry<String, Set<Integer>> entry : keywords.entrySet()) {
            String keyword = entry.getKey();
            Set<Integer> lines = entry.getValue();
            String[] temp = {keyword, lines.toString()};
            result.add(temp);
        }
        return result;
    }

    public Set<Integer> getWordLineNums(String keyword) {
        for (String storedKey : keywords.keySet()) {
            if (storedKey.equalsIgnoreCase(keyword)) {
                return keywords.get(storedKey);
            }
        }
        return new java.util.HashSet<>();
    } 
}