import java.util.List;
import java.util.ArrayList;

class CircularShift implements Shift {
    @Override
    public List<Sentence> shiftSentences(List<Sentence> sentences) {
        List<Sentence> result = new ArrayList<>();

        for (Sentence s : sentences) {
            result.add(s);
            int lineNum = s.getLineNum();

            for (int i = 1; i < s.getWords().size(); i++) {
                List<String> current = new ArrayList<>(result.get(result.size() - 1).getWords());
                current.add(current.remove(0));
                result.add(new Sentence(current, lineNum));
            }
        }
        return result;
    } 
}