package com.cyricc.rpiserver;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

import static com.cyricc.rpiserver.Broadcaster.sessions;
import static com.cyricc.rpiserver.Publisher.websocket;

/**
 * Created by cyricc on 7/20/2016.
 */
@WebSocket
public class BroadcastHandler {

    private static Logger log = LoggerFactory.getLogger(BroadcastHandler.class);

    @OnWebSocketConnect
    public void connected(Session session) {
        sessions.add(session);
        websocket.publishConnect(session);
//        System.out.println("Websocket client connected " + session.getRemoteAddress().getHostString());
        log.info("Websocket connection opened: {}", session.getRemoteAddress().getHostString());
    }

    @OnWebSocketClose
    public void closed(Session session, int status, String reason) {
        sessions.remove(session);
        websocket.publishClose(session, status, reason);
//        System.out.println("Websocket client closed " + session.getRemoteAddress().getHostString() + ": " +
//                status + " " + reason);
        log.info("Websocket client {} disconnected: {} {}", session.getRemoteAddress().getHostString(), status, reason);
    }

    @OnWebSocketMessage
    public void message(Session session, String message) throws IOException {
        websocket.publishMessage(session, message);
//        System.out.println("Message from " + session.getRemoteAddress().getHostString() + ": " +
//                message);
        log.info("Message from {}: {}", session.getRemoteAddress(), message);
    }
}
