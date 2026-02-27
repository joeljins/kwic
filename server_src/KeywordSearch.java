import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class KeywordSearch{
    private List<Sentence> sentences;
    private Map<Integer, Sentence> lookUp = new HashMap<>();
    private IndexGenerator indexGen;

    public KeywordSearch(List<Sentence> sentences, IndexGenerator indexGen){
        this.sentences = sentences;
        for(Sentence s: sentences){
            lookUp.put(s.getLineNum(), s);
        }
        this.indexGen = indexGen;
    }

    public List<Sentence> search(String keyword){
        Set<Integer> lineNums = indexGen.getWordLineNums(keyword);
        List<Sentence> sentences = new ArrayList<>();
        for (Integer num: lineNums){
            sentences.add(getSentenceByLineNum(num));
        }
        return sentences;
    }

    // Retrieve line by line number for search results
    // getSentenceByLineNum(1) = 1: Sense and Sensibility
    public Sentence getSentenceByLineNum(int lineNum){
        return lookUp.get(lineNum);
    }

}