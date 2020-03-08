package scandirectory.timer;

import java.util.TimerTask;

public class ApplicationTimer extends TimerTask {
    private String strPrint;

    public ApplicationTimer(String strPrint) {
        this.strPrint = strPrint;
    }

    @Override
    public void run() {
        System.out.print(strPrint);
    }
}
