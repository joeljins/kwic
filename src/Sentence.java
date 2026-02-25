import java.util.List;

class Sentence {
    private List<String> words;
    private int lineNum;

    public Sentence(List<String> words, int lineNum) {
        this.words = words;
        this.lineNum = lineNum;
    }

    public List<String> getWords(){
        return words;
    }

    public String joinSentence(){
        return String.join(" ", words);
    }

    public int getLineNum(){
        return lineNum;
    }
}