package com.cyricc.rpiserver;

import org.eclipse.jetty.websocket.api.Session;

import java.io.IOException;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by cyricc on 7/20/2016.
 */
public class Broadcaster {

    final static Queue<Session> sessions = new ConcurrentLinkedQueue<>();

    static void pushTemp(double temp) {
        sessions.forEach((session) -> {
            try {
                session.getRemote().sendString(String.valueOf(temp));
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
