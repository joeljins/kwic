import java.util.List;

class Formatter {

    // Returns a single sentence as a string
    public String formatSentence(Sentence s) {
        return s.getWords().toString(); // or s.joinSentence() if you prefer
    }

    public String boldFormatSentence(Sentence s, String keyword) {
        StringBuilder sb = new StringBuilder();
        for (String word : s.getWords()) {
            if (word.equalsIgnoreCase(keyword)) {
                sb.append("**").append(word).append("** ");
            } else {
                sb.append(word).append(" ");
            }
        }
        return sb.toString().trim();
    }

    // Returns all sentences as a single string, each on a new line
    public String formatSentences(List<Sentence> sentences) {
        StringBuilder sb = new StringBuilder();
        for (Sentence s : sentences) {
            sb.append(formatSentence(s)).append("\n");
        }
        return sb.toString();
    }

    // Returns index entries as a string, each on a new line
    public String formatIndex(List<String[]> index) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        sb.append("Index | Keyword | Origin Line Index\n");
        for (String[] entry : index) {
            sb.append(i).append(" | ").append(entry[0]).append(" | ").append(entry[1]).append("\n");
            i++;
        }
        return sb.toString();
    }

    // Returns KWIC formatted output as a string
    public String formatKWIC(List<Sentence> sentences) {
        StringBuilder sb = new StringBuilder();
        int i = 1;
        for (Sentence s : sentences) {
            sb.append(i)
              .append(" | ")
              .append(s.joinSentence())
              .append(" | ")
              .append(s.getLineNum())
              .append("\n");
            i++;
        }
        return sb.toString();
    }
}
