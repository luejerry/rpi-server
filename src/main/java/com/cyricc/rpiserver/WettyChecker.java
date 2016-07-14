package com.cyricc.rpiserver;

import java.io.IOException;

/**
 * Created by luej on 7/1/16.
 */
public class WettyChecker {

    private ProcessBuilder pb;

    public WettyChecker() {
        pb = new ProcessBuilder(
                "pgrep", "-f",
                "/home/pi/wetty/app\\.js.*3000$");
//                "launchd");
    }

    public Status checkWetty() {
        try {
            Process pgrep = pb.start();
            String result = ProcessReader.getStdout(pgrep);
//            System.out.println("Output read: " + result);
            if (result.isEmpty()) {
//                System.out.println("empty");
                return Status.DEAD;
            } else {
                return Status.LIVE;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return Status.UNKNOWN;
        }

    }

}
