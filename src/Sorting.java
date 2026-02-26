import java.util.List;
import java.util.Map;
import java.util.Set;

public interface Sorting {
    public List<Sentence> sortSentences(List<Sentence> sentences, boolean isAscending);

    public Map<String, Set<Integer>>  sortIndex(Map<String, Set<Integer>> indexMap, boolean isAscending);
}
