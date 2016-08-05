package com.isatimur.poroute.websocket.sample;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by developer on 8/4/16.
 */
public class MyHandler extends TextWebSocketHandler {
    //queue holds the list of connected clients
    private static Queue<WebSocketSession> queue = new ConcurrentLinkedQueue<>();
    private static Thread rateThread; //rate publisher thread

    static {
//rate publisher thread, generates a new value for USD rate every 2 seconds.
        rateThread = new Thread() {
            public void run() {
                DecimalFormat df = new DecimalFormat("#.####");
                while (true) {
                    double d = 2 + Math.random();
                    if (queue != null)
                        sendAll("USD Rate: " + df.format(d));
                    try {
                        sleep(2000);
                    }
                    catch (InterruptedException e) {
                    }
                }
            }

            ;
        };
        rateThread.start();
    }

    private static void sendAll(String msg) {
        try {
   /* Send the new rate to all open WebSocket sessions */
            ArrayList<WebSocketSession> closedSessions = new ArrayList<>();
            for (WebSocketSession session : queue) {
                if (!session.isOpen()) {
                    System.err.println("Closed session: " + session.getId());
                    closedSessions.add(session);
                }
                else {
                    session.sendMessage(new TextMessage(msg));
                }
            }
            queue.removeAll(closedSessions);
            System.out.println("Sending " + msg + " to " + queue.size() + " clients");
        }
        catch (Throwable e) {
            e.printStackTrace();
        }
    }

    @Override protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            System.out.println("received msg " + message.getPayload() + " from " + session.getId());
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        queue.add(session);
        System.out.println("New session opened: " + session.getId());
    }

    @Override public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        queue.remove(session);
        System.out.println("session closed: " + session.getId());
    }


}
