package com.isatimur.poroute.websocket.commands;

import com.google.maps.model.LatLng;
import com.isatimur.poroute.service.CommandsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

/**
 * Created by developer on 8/4/16.
 */
public class LocationHandler extends TextWebSocketHandler {

    @Autowired
    CommandsService commandsService;

    @Override protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        try {
            String msg = message.getPayload();
            System.out.println("received msg " + msg + " from " + session.getId());
            double lat = Double.parseDouble(msg.split(",")[0]);
            double lng = Double.parseDouble(msg.split(",")[1]);
            LatLng latLng = new LatLng(lat, lng);
            commandsService.getCommandsByLocation(session, latLng);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        System.out.println("New session opened: " + session.getId());
    }

    @Override public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        System.out.println("session closed: " + session.getId());
    }

    @Override public void handleTransportError(WebSocketSession session, Throwable exception) throws Exception {
        System.out.println("transport error: " + session.getId() + " " + exception.toString());
    }

}
