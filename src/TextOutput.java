import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

class TextOutput implements Output{
    private String filePath;

    public TextOutput() {
    }

    @Override
    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
public void write(String content) {
    try {
        Files.writeString(
            Path.of(filePath), 
            content, 
            StandardOpenOption.CREATE, 
            StandardOpenOption.WRITE, 
            StandardOpenOption.TRUNCATE_EXISTING
        );
    } catch (IOException e) {
        System.err.println("Error writing to file: " + e.getMessage());
    }
}
}