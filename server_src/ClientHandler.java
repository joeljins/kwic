import java.io.*;
import java.net.*;
import java.util.*;

class ClientHandler extends Thread {

    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket clientSocket;
    private final KeywordSearch keySearch;
    private final Formatter formatter;
    private final Logger logger;

    public ClientHandler(Socket clientSocket,
                         DataInputStream dis,
                         DataOutputStream dos,
                         KeywordSearch keySearch,
                         Formatter formatter,
                         Logger logger) {
        this.clientSocket = clientSocket;
        this.dis = dis;
        this.dos = dos;
        this.keySearch = keySearch;
        this.formatter = formatter;
        this.logger = logger;
    }

    @Override
    public void run() {
        String received;

        while (true) {
            try {
                dos.writeUTF("Enter 'keyword-search <keyword>' or 'Exit' to terminate connection.");

                received = dis.readUTF();
                String[] command = received.split(" ");

                switch (command[0]) {
                    case "Exit" -> {
                        this.clientSocket.close();
                        break;
                    }
                    case "keyword-search" -> {
                        if (command.length < 2) {
                            dos.writeUTF("Error: Please provide a keyword to search.");
                            continue;
                        }
                        logger.received();
                        String keyword = command[1];
                        List<Sentence> results = keySearch.search(keyword);
                        int numResults = results.size();
                        if (numResults == 0) {
                            dos.writeUTF("No sentences found containing the keyword: " + keyword);
                            continue;
                        }

                        StringBuilder sb = new StringBuilder();
                        sb.append("Index | Line with Keyword Bolded | Origin Line Index\n");

                        for (int i = 0; i < numResults; i++) {
                            String indexDisplay = Integer.toString(i + 1);
                            sb.append(indexDisplay)
                              .append(" | ")
                              .append(formatter.boldFormatSentence(results.get(i), keyword))
                              .append(" | ")
                              .append(results.get(i).getLineNum())
                              .append("\n");
                        }

                        dos.writeUTF(sb.toString());
                        logger.responded();
                    }

                    default -> dos.writeUTF("Invalid input");
                }

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }

        try {
            this.dis.close();
            this.dos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}