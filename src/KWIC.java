import java.util.HashSet;
import java.util.List;

public class KWIC {
    private KeywordSearch keySearch;
    private Formatter formatter;
    Output out;

    public KWIC (String filePath){
        InputProcessor textFile = new TextFileInput(filePath);
        List<Sentence> sentences = textFile.processInput(); 

        WordFilter filter = new StopWordsFilter(new HashSet<>());

        IndexGenerator indexGen = new IndexGenerator(sentences, filter);
        this.keySearch = new KeywordSearch(sentences, indexGen);

        Shift shifter = new CircularShift();
        List<Sentence> shiftedSentences = shifter.shiftSentences(sentences);

        boolean isAscending = true;
        Sorting sorter = new AlphabetSort();
        sorter.sortSentences(sentences, isAscending);

        this.formatter = new Formatter();
        this.out = new ConsoleOutput();
    }

    public String keywordSearch(String keyword) {
        List<Sentence> results = keySearch.search(keyword);
        int numResults = results.size();
        System.out.println(numResults + " sentence(s) are found. ");
        StringBuilder sb = new StringBuilder();
        sb.append("Index | Line with Keyword Bolded | Origin Line Index\n");
        for(int i = 1; i <= numResults; i++){
            String j = Integer.toString(i);
            sb.append("Index | Line with Keyword Bolded | Origin Line Index\n");
            sb.append(j + " | ");
            sb.append(formatter.boldFormatSentence(results.get(i-1), keyword));
            sb.append(" | " + results.get(i-1).getLineNum());
        }
        return sb.toString();
    }
}
