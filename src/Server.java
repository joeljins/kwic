import java.io.*;
import java.net.*;

public class Server {
  
    private Socket clientSocket = null;
    private ServerSocket serverSocket = null;
    private DataInputStream in = null;
    private OutputStream out = null;

    private KWIC kwic;

    public Server(int port, KWIC kwic) {
        this.kwic = kwic;
        try{
            serverSocket = new ServerSocket(port);
            System.out.println("Server started");
        }
        catch (IOException e){
            e.printStackTrace(); 
        }
    }

    public void start(){
            System.out.println("Waiting for clients ...");
            while(true){
                try{
                    clientSocket = serverSocket.accept();
                    System.out.println("Client accepted");
                    String response = this.handleRequest();
                    this.sendMessage(response);
                }
                catch (IOException e){
                    e.printStackTrace(); 
                }
            }
    }

    public String handleRequest(){
        try {
          in = new DataInputStream(new BufferedInputStream(clientSocket.getInputStream()));
          String keyword = in.readUTF();
          return kwic.keywordSearch(keyword);
        } 
        catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    
    public void sendMessage(String message) {
        try {
            out = clientSocket.getOutputStream();
            out.write(message.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }

    public void close(){
        
    }

}