package scandirectory.service;

import scandirectory.model.FileData;

import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ForkJoinTask;
import java.util.concurrent.RecursiveAction;

public class TraversalPathTaskService extends RecursiveAction {
    private TreeSet<String> currentResult = new TreeSet<>();
    private Path currentDirectory;
    private InputDataHandlerService inputData;
    private List<TraversalPathTaskService> walks = new ArrayList<>();

    public TraversalPathTaskService(InputDataHandlerService inputData, Path currentDirectory) {
        this.inputData = inputData;
        this.currentDirectory = currentDirectory;
    }

    @Override
    protected void compute() {
        try {
            Files.walkFileTree(currentDirectory, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                    Date date = new Date(file.toFile().lastModified());
                    FileData myFile = new FileData(
                            file.toFile().toString(),
                            dateFormat.format(date),
                            Long.toString(file.toFile().length())
                    );
                    currentResult.add("[\n" + myFile.toString());
                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult preVisitDirectory(Path aDir, BasicFileAttributes aAttrs) {

                    if (inputData.getFoldersForNotScan() != null && inputData.getFoldersForNotScan().contains(aDir.toString())) {
                        return FileVisitResult.SKIP_SUBTREE;
                    }

                    if (!aDir.equals(TraversalPathTaskService.this.currentDirectory)) {
                        TraversalPathTaskService walk = new TraversalPathTaskService(inputData, aDir);
                        walk.fork();
                        walks.add(walk);
                        return FileVisitResult.SKIP_SUBTREE;
                    }

                    return FileVisitResult.CONTINUE;
                }

                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) {
                    if (dir.equals(TraversalPathTaskService.this.currentDirectory) && currentResult.size() != 0) {
                        File tempDir = new File("my directory" + "\\" + dir.toString());
                        tempDir.mkdirs();
                        Path file = Paths.get("my directory" + "\\" + dir.toString() + "\\"+ "result.txt");
                        try {
                            Files.write(file, currentResult, StandardOpenOption.APPEND, StandardOpenOption.CREATE);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        currentResult = null;
                    }
                    Objects.requireNonNull(dir);
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (ForkJoinTask forkJoinTask : walks) {
            forkJoinTask.join();
        }
    }
}
