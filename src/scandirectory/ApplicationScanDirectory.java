package scandirectory;

import scandirectory.service.InputDataHandlerService;
import scandirectory.service.WriteFileService;
import scandirectory.timer.ApplicationTimer;
import scandirectory.service.TraversalPathTaskService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.concurrent.ForkJoinPool;

public class ApplicationScanDirectory {
    public static void main(String... args) throws IOException {
        int numOfThreads = Runtime.getRuntime().availableProcessors();
        Timer applicationTimer = new Timer();
        File tempFile = new File("not for scan");

        InputDataHandlerService inputData = new InputDataHandlerService();
        inputData.scanFolders(args);

        tempFile.mkdir();
        inputData.getFoldersForNotScan().add(tempFile.getAbsolutePath());

        applicationTimer.schedule(new ApplicationTimer("."), 6000, 6000);
        applicationTimer.schedule(new ApplicationTimer("|"), 60000, 60000);

        System.out.println("Start scanning process");
        for (String includedFolder : inputData.getFoldersForScan()) {
            TraversalPathTaskService recursiveWalk = new TraversalPathTaskService(inputData, Paths.get(includedFolder));
            ForkJoinPool forkJoinPool = new ForkJoinPool(numOfThreads);
            forkJoinPool.invoke(recursiveWalk);
        }
        System.out.println("Finish scanning process");

        SimpleDateFormat format = new SimpleDateFormat("hh-mm-ss-dd-MM-yyyy");
        FileWriter fw = new FileWriter("scan from " + format.format(new Date()) + ".txt");
        WriteFileService fileProcessor = new WriteFileService();

        fileProcessor.setFileWriter(fw);
        Files.walkFileTree(Paths.get("my directory"), fileProcessor);

        applicationTimer.cancel();
    }
}
