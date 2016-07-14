package com.cyricc.rpiserver;

import java.io.IOException;

/**
 * Created by cyricc on 7/14/2016.
 */
public class Uptime {

    private ProcessBuilder pb;

    public Uptime() {
        pb = new ProcessBuilder("uptime", "-p");
    }

    @Override
    public String toString() {
        try {
            Process pup = pb.start();
            return ProcessReader.getStdout(pup);
        } catch (IOException e) {
            e.printStackTrace();
            return "Uptime unknown";
        }

    }
}
