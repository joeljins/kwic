import java.io.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Logger {
    private final String filePath;
    private final AtomicInteger numRequests = new AtomicInteger(0);
    private final AtomicInteger successfulResponses = new AtomicInteger(0);

    public Logger(String filePath) {
        this.filePath = filePath;
        // Reset count upon new Server start
        try (FileWriter fw = new FileWriter(filePath)) {
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void received() {
        numRequests.incrementAndGet();
        writeLogFile();
    }

    public void responded() {
        successfulResponses.incrementAndGet();
        writeLogFile();
    }

    private synchronized void writeLogFile() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("Total Requests: " + numRequests.get());
            writer.newLine();
            writer.write("Successful Responses: " + successfulResponses.get());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}