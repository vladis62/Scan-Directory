package scandirectory.service;

import java.util.ArrayList;
import java.util.List;

public class InputDataHandlerService {
    private List<String> foldersForScan = new ArrayList<>();
    private List<String> foldersForNotScan;

    public void scanFolders(String[] folders) {
        boolean include = true;
        for (String folder : folders) {
            if (folder.equals("-")) {
                include = false;
                foldersForNotScan = new ArrayList<>();
            }
            if (include) {
                foldersForScan.add(folder);
                continue;
            }
            foldersForNotScan.add(folder);
            foldersForScan.remove(folder);
        }

    }

    public List<String> getFoldersForScan() {
        return foldersForScan;
    }

    public List<String> getFoldersForNotScan() {
        if (foldersForNotScan == null) {
            foldersForNotScan = new ArrayList<>();
        }
        return foldersForNotScan;
    }
}
