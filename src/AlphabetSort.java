import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;

class AlphabetSort implements Sorting {
    public AlphabetSort(){ 
    }

    @Override
    public List<Sentence> sortSentences(List<Sentence> sentences, boolean isAscending) {
        Comparator<Sentence> comparator = Comparator.comparing(Sentence::joinSentence, String.CASE_INSENSITIVE_ORDER);
        if (!isAscending) {
            comparator = comparator.reversed();
        }
        Collections.sort(sentences, comparator);
        return sentences;
    }
    
    @Override
    public Map<String, Set<Integer>> sortIndex(Map<String, Set<Integer>> indexMap, boolean isAscending) {
        Map<String, Set<Integer>> sortedIndex = new java.util.LinkedHashMap<>();
        Comparator<Map.Entry<String, Set<Integer>>> comparator = Map.Entry.comparingByKey(String.CASE_INSENSITIVE_ORDER);
        if (!isAscending) {
            comparator = comparator.reversed();
        }
        indexMap.entrySet().stream()
            .sorted(comparator)
            .forEachOrdered(entry -> sortedIndex.put(entry.getKey(), entry.getValue()));
        return sortedIndex;
    }
}