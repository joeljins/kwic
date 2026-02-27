import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Client {
    private final String host;
    private final int port;
    private final int timeout;

    public Client(String host, int port, int timeout) {
        this.host = host;
        this.port = port;
        this.timeout = timeout;
    }

    public void run() {
        try (Socket socket = new Socket(host, port);
             DataInputStream dis = new DataInputStream(socket.getInputStream());
             DataOutputStream dos = new DataOutputStream(socket.getOutputStream());
             Scanner scn = new Scanner(System.in)) {

            socket.setSoTimeout(timeout);

            while (true) {
                String serverMessage;
                try {
                    serverMessage = dis.readUTF();
                } catch (SocketTimeoutException e) {
                    System.out.println("The KWIC server is not responding.");
                    break;
                }
                System.out.println(serverMessage);

                String tosend = scn.nextLine();
                dos.writeUTF(tosend);

                if ("Exit".equalsIgnoreCase(tosend)) break;

                String response;
                try {
                    response = dis.readUTF();
                } catch (SocketTimeoutException e) {
                    System.out.println("The KWIC server is not responding.");
                    break;
                }
                System.out.println(response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}