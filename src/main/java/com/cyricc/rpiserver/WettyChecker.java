package com.cyricc.rpiserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

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
            String result = getStdout(pgrep);
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

    public static String getStdout(Process process) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        StringBuilder builder = new StringBuilder();
        String line;
        while ( (line = reader.readLine()) != null) {
            builder.append(line);
            builder.append(System.getProperty("line.separator"));
        }
        return builder.toString();
    }
}
