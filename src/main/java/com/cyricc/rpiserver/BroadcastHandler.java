package com.cyricc.rpiserver;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import java.io.IOException;

import static com.cyricc.rpiserver.Broadcaster.sessions;

/**
 * Created by cyricc on 7/20/2016.
 */
@WebSocket
public class BroadcastHandler {
    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
        System.out.println("Websocket client connected " + session.getRemoteAddress().getHostString());
    }

    @OnWebSocketClose
    public void closed(Session session, int status, String reason) {
        sessions.remove(session);
        System.out.println("Websocket client closed " + session.getRemoteAddress().getHostString() + ": " +
                status + " " + reason);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        System.out.println("Message from " + session.getRemoteAddress().getHostString() + ": " +
                message);
    }
}
