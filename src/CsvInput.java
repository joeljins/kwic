import java.io.*;
import java.util.*;

class CsvInput extends Input {
    private String filePath;

    public CsvInput(String filePath) {
        this.filePath = filePath;
    }

    public CsvInput() {
    }

    @Override
    public List<Sentence> processInput() {
        List<Sentence> sentences = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) continue; 
                
                String[] parts = line.split("\\."); 
                
                int i = 1;
                for (String sentence : parts) {
                    String trimmed = sentence.trim();
                    if (trimmed.isEmpty()) continue;

                    List<String> words = new ArrayList<>(Arrays.asList(trimmed.split("\\s+")));
                    sentences.add(new Sentence(words, i++));
                }
            }
        } catch (IOException | NumberFormatException e) {
            System.err.println("Error processing line: " + e.getMessage());
        }
        return sentences;
    }

    @Override
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}