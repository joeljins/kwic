import java.util.Collections;
import java.util.Comparator;
import java.util.List;

class AlphabetSort implements Sorting {
    @Override
    public List<Sentence> sort(List<Sentence> sentences) {
        Collections.sort(
            sentences,
            Comparator.comparing(Sentence::joinSentence, String.CASE_INSENSITIVE_ORDER)
        );
        return sentences;
    }
}