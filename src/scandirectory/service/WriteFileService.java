package scandirectory.service;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;

public class WriteFileService extends SimpleFileVisitor<Path> {

    private FileWriter fileWriter;

    @Override
    public FileVisitResult visitFile(Path aFile, BasicFileAttributes aAttrs) throws IOException {
        FileReader file = new FileReader(aFile.toFile());
        int c = file.read();
        while(c != -1) {
            fileWriter.write(c);
            c = file.read();
        }
        return FileVisitResult.CONTINUE;
    }

    public void setFileWriter(FileWriter fileWriter) {
        this.fileWriter = fileWriter;
    }

}
