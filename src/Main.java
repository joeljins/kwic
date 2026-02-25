import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        // Read from console
        Scanner scanner = new Scanner(System.in);

        // Process input
        InputProcessor textFile = new TextFileInput(args[0]);
        List<Sentence> sentences = textFile.processInput(); 

        // Filter (possible extension)
        WordFilter filter = new StopWordsFilter(new HashSet<>());

        // Create index of keywords
        IndexGenerator indexGen = new IndexGenerator(sentences, filter);
        KeywordSearch keySeach = new KeywordSearch(sentences, indexGen);

        // Shift sentences
        Shift shifter = new CircularShift();
        List<Sentence> shiftedSentences = shifter.shiftSentences(sentences);

        // Sort
        Sorting sorter = new AlphabetSort();
        sorter.sort(shiftedSentences);

        // Output 
        Formatter formatter = new Formatter();
        Output out = new ConsoleOutput();

        System.out.println("KWIC system initialized and configured. Ready to process commands.");
        
        boolean isRunning = true;
        while (isRunning) { 
        System.out.println("Available commands: kwic, search <keyword>, index, exit");
           String [] words = scanner.nextLine().split(" ");
           String command = words[0].toLowerCase();
           switch(command){
            case "kwic" -> {
                sorter.sort(shiftedSentences);
                out.write(formatter.formatKWIC(shiftedSentences));
                }
            case "search" -> {
                if (words.length < 2){
                    System.out.println("No keyword provided");
                }
                else{
                    List<Sentence> results = keySeach.search(words[1]);
                    int numResults = results.size();
                    System.out.println(numResults + " sentence(s) are found. ");
                    for(int i = 1; i <= numResults; i++){
                        String j = Integer.toString(i);
                        out.write("Index | Line with Keyword Bolded | Origin Line Index\n");
                        out.write(j + " | ");
                        out.write(formatter.boldFormatSentence(results.get(i-1), words[1]));
                        out.write(" | " + results.get(i-1).getLineNum());
                    }
                }
            }
            case "index" -> {
                out.write(formatter.formatIndex(indexGen.getAllWordCount()));
            }
            case "exit" ->{
                isRunning = false;
                System.out.println("Exiting program.");
            }

            default -> {}
            }
            System.out.println();
           }
        }
    }
