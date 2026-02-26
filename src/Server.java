import java.io.*;
import java.net.*;

public class Server {

    private ServerSocket serverSocket;
    private final KeywordSearch keySearch;
    private final Formatter formatter;

    public Server(int port, KeywordSearch keySearch, Formatter formatter) {
        this.keySearch = keySearch;
        this.formatter = formatter;

        try {
            serverSocket = new ServerSocket(port);
            System.out.println("Server started");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void start() {
        System.out.println("Waiting for clients ...");

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                System.out.println("A new client is connected : " + clientSocket);

                DataInputStream in =
                        new DataInputStream(clientSocket.getInputStream());

                DataOutputStream out =
                        new DataOutputStream(clientSocket.getOutputStream());

                System.out.println("Assigning new thread for this client");

                Thread t = new ClientHandler(
                        clientSocket,
                        in,
                        out,
                        keySearch,
                        formatter
                );

                t.start();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}