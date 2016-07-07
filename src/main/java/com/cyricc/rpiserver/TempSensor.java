package com.cyricc.rpiserver;


import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

/**
 * Created by luej on 7/3/16.
 */
public class TempSensor {

    private final static int MAX_TRIES = 3;
    private final static int PERIOD = 20;
    private final static String TEMP_DEVICE = "/sys/bus/w1/devices/28-04145164bdff/w1_slave";
    private final static Path tempPath = Paths.get(TEMP_DEVICE);
    private final static Charset charset = Charset.forName("UTF-8");
    private final static ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    private static volatile int temp = 0;

    private static ScheduledFuture task;

    public static void startScheduler() {
        task = scheduler.scheduleAtFixedRate(() -> {
            if (getStatus() == Status.LIVE) {
                Optional<Integer> oTemp = Optional.empty();
                int trycount = 0;
                while (!oTemp.isPresent() && trycount < MAX_TRIES) {
                    oTemp = readTemp();
                    trycount ++;
                }
                if (oTemp.isPresent()) {
                    temp = oTemp.get();
                }
            }
        }, 0, PERIOD, TimeUnit.SECONDS);
    }

    public static Status getStatus() {
        return Files.exists(tempPath) ? Status.LIVE : Status.DEAD;
    }

    public static double getTemp() {
        return Integer.valueOf(temp).doubleValue() / 1000;
    }

    public static Optional<Integer> oGetDelay() {
        return Optional.ofNullable(task)
                .map((t) ->
                        Long.valueOf(
                                t.getDelay(TimeUnit.SECONDS)
                        ).intValue()
                );
    }

    private static Optional<Integer> readTemp() {
        try {
            final BufferedReader reader = Files.newBufferedReader(tempPath);
            /**
             * Example reading of 29.5 degC:
             * d8 01 55 00 7f ff 0c 10 6d : crc=6d YES
             * d8 01 55 00 7f ff 0c 10 6d t=29500
             */
            String line = reader.readLine();
//            System.out.println(line);
            if (!line.endsWith("YES")) {
                return Optional.empty();
            }
            line = reader.readLine();
            final Integer temp = Integer.valueOf(line.substring(line.length() - 5));
            return Optional.of(temp);
        } catch (IOException e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }
}
