import java.io.*;
import java.net.*;
import java.util.*;

class ClientHandler extends Thread {

    final DataInputStream dis;
    final DataOutputStream dos;
    final Socket clientSocket;
    private final KeywordSearch keySearch;
    private final Formatter formatter;

    public ClientHandler(Socket clientSocket,
                         DataInputStream dis,
                         DataOutputStream dos,
                         KeywordSearch keySearch,
                         Formatter formatter) {
        this.clientSocket = clientSocket;
        this.dis = dis;
        this.dos = dos;
        this.keySearch = keySearch;
        this.formatter = formatter;
    }

    @Override
    public void run() {
        String received;

        while (true) {
            try {
                dos.writeUTF("Enter keyword");

                received = dis.readUTF();

                if (received.equals("Exit")) {
                    this.clientSocket.close();
                    break;
                }

                String[] command = received.split(" ");

                switch (command[0]) {
                    case "keyword-search" -> {
                        if (command.length < 2) {
                            dos.writeUTF("Error: Please provide a keyword to search.");
                            continue;
                        }

                        String keyword = command[1];
                        List<Sentence> results = keySearch.search(keyword);
                        int numResults = results.size();

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