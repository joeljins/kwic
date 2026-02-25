import java.io.*;
import java.util.*;

class TextFileInput extends InputProcessor {
    private String filePath;

    public TextFileInput(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public List<Sentence> processInput() {
        List<Sentence> sentences = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; 

                List<String> words = new ArrayList<>(Arrays.asList(line.split("\\s+")));
                
                String idString = words.get(0);
                if (idString.endsWith(":")) {
                    idString = idString.substring(0, idString.length() - 1);
                }
                int lineNum = Integer.parseInt(idString);
                words.remove(0);
                
                sentences.add(new Sentence(words, lineNum));
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error processing line: " + e.getMessage());
        }
        return sentences;
    }
}