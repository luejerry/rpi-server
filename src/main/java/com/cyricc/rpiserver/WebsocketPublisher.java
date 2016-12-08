package com.cyricc.rpiserver;

import org.eclipse.jetty.websocket.api.Session;

import java.util.Collection;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by cyricc on 12/7/2016.
 */
public class WebsocketPublisher {
    private final Collection<ConnectHandler> connectSubscribers = new ConcurrentLinkedQueue<>();
    private final Collection<CloseHandler> closeSubscribers = new ConcurrentLinkedQueue<>();
    private final Collection<MessageHandler> messageSubscribers = new ConcurrentLinkedQueue<>();

    public Subscription<ConnectHandler> subscribeConnect(ConnectHandler handler) {
        connectSubscribers.add(handler);
        return new Subscription<>(connectSubscribers, handler);
    }

    public Subscription<CloseHandler> subscribeClose(CloseHandler handler) {
        closeSubscribers.add(handler);
        return new Subscription<>(closeSubscribers, handler);
    }

    public Subscription<MessageHandler> subscribeMessage(MessageHandler handler) {
        messageSubscribers.add(handler);
        return new Subscription<>(messageSubscribers, handler);
    }

    public void publishConnect(Session session) {
        connectSubscribers.parallelStream().forEach(handler -> handler.apply(session));
    }

    public void publishClose(Session session, int status, String reason) {
        closeSubscribers.parallelStream().forEach(handler -> handler.apply(session, status, reason));
    }

    public void publishMessage(Session session, String message) {
        messageSubscribers.parallelStream().forEach(handler -> handler.apply(session, message));
    }

    /**
     * This class is a handle returned to a subscriber, so that it can unsubscribe itself from future events by
     * invoking the `unsubscribe()` method.
     * @param <T> Type of Websocket event handler.
     */
    public class Subscription<T extends WebsocketHandler> {
        private Collection<T> list;
        private T handler;

        public Subscription(Collection<T> list, T handler) {
            this.list = list;
            this.handler = handler;
        }

        public boolean unsubscribe() {
            return list.remove(handler);
        }
    }

    public interface WebsocketHandler {}

    public interface ConnectHandler extends WebsocketHandler {
        void apply(Session session);
    }

    public interface CloseHandler extends WebsocketHandler {
        void apply(Session session, int status, String reason);
    }

    public interface MessageHandler extends WebsocketHandler {
        void apply(Session session, String message);
    }
}
