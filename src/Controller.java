import java.util.HashSet;
import java.util.List;

public class Controller {   
    
    public static void main(String[] args) {
        // Handle missing args 
        if (args.length == 0) {
            System.out.println("Error: Please provide a command (e.g., kwic-processing)");
            return;
        }

        // Load Configuration
        OptionReader.readOptions(args[args.length - 1]);
        String inputObjStr = OptionReader.getString("Input"); 

        // Create input object and set file path    
        Input inputObj = (Input) OptionReader.getObjectFromKey(inputObjStr);
        String path = OptionReader.getString("InputFileName");
        inputObj.setFilePath(path); 

        // Process input
        List<Sentence> sentences = inputObj.processInput();

        // Filter 
        String status = OptionReader.getString("WordFiltering");
        boolean isFiltering = "Yes".equalsIgnoreCase(status);
        HashSet<String> trivialWords = new HashSet<>();
        if (isFiltering) {
            String rawWords = OptionReader.getString("TrivialWords").replaceAll("[”\"“]", "");
            String[] words = rawWords.split(",");
            for (String word : words) {
                trivialWords.add(word.trim().toLowerCase());
            }
        }
        WordFilter filter = new StopWordsFilter(trivialWords);      

        // Create index and search objects
        IndexGenerator indexGen = new IndexGenerator(sentences, filter);
        // Create a separate index generator for keyword search without filtering
        KeywordSearch keySearch = new KeywordSearch(sentences, new IndexGenerator(sentences, new StopWordsFilter(new HashSet<>()))); 

        // Shift sentences
        Shift shifter = new CircularShift();
        List<Sentence> shiftedSentences = shifter.shiftSentences(sentences);

        // Sort
        Sorting sorter = new AlphabetSort();
        String order = OptionReader.getString("Order");
        if (order.equalsIgnoreCase("Ascending")) {
            sorter.sortSentences(shiftedSentences, true);
            indexGen.setIndexMap(sorter.sortIndex(indexGen.getIndexMap(), true));
        } else {
            sorter.sortSentences(shiftedSentences, false);
            indexGen.setIndexMap(sorter.sortIndex(indexGen.getIndexMap(), false));
        }

        // Output
        Formatter formatter = new Formatter();
        String outputKey = OptionReader.getString("Output"); 
        Output out = (Output) OptionReader.getObjectFromKey(outputKey); 
        String outputPath = OptionReader.getString("OutputFileName");
        out.setFilePath(outputPath);

        switch (args[0]) {
            case "kwic-processing" -> out.write(formatter.formatKWIC(shiftedSentences));
            case "keyword-search" -> {
                if (args.length < 2) {
                    System.out.println("Error: Please provide a keyword to search.");
                    return;
                }   List<Sentence> results = keySearch.search(args[1]);
                int numResults = results.size();
                System.out.println(numResults + " sentence(s) are found. ");
                out.write("Index | Line with Keyword Bolded | Origin Line Index\n");
                for (int i = 0; i < numResults; i++) {
                    String indexDisplay = Integer.toString(i + 1);
                    out.write(indexDisplay + " | ");
                    out.write(formatter.boldFormatSentence(results.get(i), args[1]));
                    out.write(" | " + results.get(i).getLineNum() + "\n");
                }
            }
            case "index-generation" -> out.write(formatter.formatIndex(indexGen.getAllWordCount()));
            default -> System.out.println("No command given / Invalid command " + args[0]);
        }

        int portNo = 8080;
        Server server = new Server(portNo, keySearch, formatter);
        server.start();

    } 
}